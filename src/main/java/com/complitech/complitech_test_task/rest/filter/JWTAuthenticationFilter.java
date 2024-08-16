/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.rest.filter;

import com.complitech.complitech_test_task.exception.AuthException;
import com.complitech.complitech_test_task.rest.util.JwtUtils;
import com.complitech.complitech_test_task.service.TokenStoreService;
import com.complitech.complitech_test_task.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.complitech.complitech_test_task.constant.AppConstant.AUTH;
import static com.complitech.complitech_test_task.constant.AppConstant.BEARER;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenStoreService tokenStoreService;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        userService.findByLogin(username).orElseThrow(() -> new AuthException("Invalid credentials: " + username));
        tokenStoreService.setTokenValidity(username, true);
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        String token = JwtUtils.generateToken(authResult.getName());
        response.addHeader(AUTH, BEARER + token);
    }
}
