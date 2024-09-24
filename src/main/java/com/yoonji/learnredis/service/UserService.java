package com.yoonji.learnredis.service;



import com.yoonji.learnredis.dto.request.UserUpdateRequest;
import com.yoonji.learnredis.dto.response.UserResponse;
import com.yoonji.learnredis.entity.User;
import com.yoonji.learnredis.exception.CustomException;
import com.yoonji.learnredis.exception.ErrorCode;
import com.yoonji.learnredis.repository.UserRepository;
import com.yoonji.learnredis.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getUser(UserPrincipal principal) {
        User findUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.builder()
                .nickname(findUser.getNickname())
                .email(findUser.getEmail())
                .role(findUser.getRole().name())
                .picture(findUser.getPicture())
                .build();
    }

    @Cacheable(cacheNames = "userCache", key = "#id + ':profile'", cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public UserResponse getUserProfile(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.builder()
                .nickname(findUser.getNickname())
                .email(findUser.getEmail())
                .role(findUser.getRole().name())
                .picture(findUser.getPicture())
                .build();
    }

    @CacheEvict(cacheNames = "userCache", key = "#principal.getId() + ':profile'")
    @Transactional
    public UserResponse updateUserProfile(UserPrincipal principal, UserUpdateRequest request) {
        User findUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User updateUser = findUser.updateProfile(request);

        return UserResponse.builder()
                .nickname(updateUser.getNickname())
                .email(updateUser.getEmail())
                .role(updateUser.getRole().name())
                .picture(updateUser.getPicture())
                .build();
    }
}
