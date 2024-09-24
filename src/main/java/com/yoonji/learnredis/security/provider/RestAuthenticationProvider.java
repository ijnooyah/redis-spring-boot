package com.yoonji.learnredis.security.provider;

import com.yoonji.learnredis.exception.CustomException;
import com.yoonji.learnredis.exception.ErrorCode;
import com.yoonji.learnredis.security.principal.UserPrincipal;
import com.yoonji.learnredis.security.service.CustomUserDetailsService;
import com.yoonji.learnredis.security.token.RestAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserPrincipal customUserPrincipal = (UserPrincipal) customUserDetailsService.loadUserByUsername(loginId);

        if(!passwordEncoder.matches(password, customUserPrincipal.getPassword())){
            throw new BadCredentialsException("Invalid credentials", new CustomException(ErrorCode.INVALID_CREDENTIALS));
        }

        return new RestAuthenticationToken(customUserPrincipal.getAuthorities(), customUserPrincipal, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RestAuthenticationToken.class);
    }
}
