package com.yoonji.learnredis.controller;

import com.yoonji.learnredis.swagger.docs.controller.AuthControllerDocs;
import com.yoonji.learnredis.dto.request.SignupRequest;
import com.yoonji.learnredis.dto.response.CommonResponse;
import com.yoonji.learnredis.dto.response.UserResponse;
import com.yoonji.learnredis.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;


    @PostMapping("/signup")
    public CommonResponse<UserResponse> signup(@RequestBody SignupRequest request) {
        return new CommonResponse<>(HttpStatus.OK, authService.signup(request));
    }

    @PostMapping(value = "/logout")
    public CommonResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return new CommonResponse<>(HttpStatus.OK);
    }
}
