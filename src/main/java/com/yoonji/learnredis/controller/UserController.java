package com.yoonji.learnredis.controller;



import com.yoonji.learnredis.dto.request.UserUpdateRequest;
import com.yoonji.learnredis.swagger.docs.controller.UserControllerDocs;
import com.yoonji.learnredis.dto.response.CommonResponse;
import com.yoonji.learnredis.dto.response.UserResponse;
import com.yoonji.learnredis.security.principal.UserPrincipal;
import com.yoonji.learnredis.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @GetMapping("/me")
    public CommonResponse<UserResponse> getUser(@AuthenticationPrincipal UserPrincipal principal) {
        return new CommonResponse<>(HttpStatus.OK, userService.getUser(principal));
    }

    @GetMapping("/{id}/profile")
    public CommonResponse<UserResponse> getUserProfile(@PathVariable Long id) {
        return new CommonResponse<>(HttpStatus.OK, userService.getUserProfile(id));
    }

    @PatchMapping("/me")
    public CommonResponse<UserResponse> updateUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody UserUpdateRequest request) {
        return new CommonResponse<>(HttpStatus.OK, userService.updateUserProfile(userPrincipal, request));
    }


}
