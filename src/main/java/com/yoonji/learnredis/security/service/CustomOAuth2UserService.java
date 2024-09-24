package com.yoonji.learnredis.security.service;


import com.yoonji.learnredis.entity.ProviderType;
import com.yoonji.learnredis.entity.RoleType;
import com.yoonji.learnredis.entity.User;
import com.yoonji.learnredis.repository.UserRepository;
import com.yoonji.learnredis.security.dto.OAuthAttributes;
import com.yoonji.learnredis.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(userRequest, oAuth2User.getAttributes());

        User user = save(oAuthAttributes);

        return new UserPrincipal(user, oAuth2User);
    }

    private User save(OAuthAttributes oAuthAttributes) {
       User user = userRepository.findByEmail(oAuthAttributes.getEmail())
                    .orElseGet(() -> User.builder()
                            .deleted(false)
                            .nickname(oAuthAttributes.getNickname())
                            .role(RoleType.ROLE_USER)
                            .provider(ProviderType.getProviderType(oAuthAttributes.getRegistrationId()))
                            .email(oAuthAttributes.getEmail())
                            .picture(oAuthAttributes.getPicture())
                            .build());

       return userRepository.save(user);
    }

}
