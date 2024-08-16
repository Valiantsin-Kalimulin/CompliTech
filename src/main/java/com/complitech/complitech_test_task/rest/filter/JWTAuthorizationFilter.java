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
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;

import static com.complitech.complitech_test_task.constant.AppConstant.AUTH;
import static com.complitech.complitech_test_task.constant.AppConstant.BEARER;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final TokenStoreService tokenStoreService;
    private final UserService userService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String header = request.getHeader(AUTH);
        if (header == null || !header.startsWith(BEARER)) {
            loginUser(request, response, filterChain);
        } else {
            String token = header.substring(BEARER.length());
            authUser(request, token);
            filterChain.doFilter(request, response);
        }
    }

    private void authUser(HttpServletRequest request, String token) {
        try {
            checkTokenExpired(request);
            var claims = JwtUtils.validateToken(token);
            var username = claims.getBody().getSubject();
            var authentication = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (IllegalStateException e) {
            throw new AuthException("Invalid token: " + token);
        }
    }

    private void checkTokenExpired(HttpServletRequest request) {
        var userName = JwtUtils.getSubjectFromToken(extractJwtFromRequest(request));
        if (!tokenStoreService.getTokenValidity(userName)) {
            throw new AuthException("Invalid or expired token") {};
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH);
        return bearerToken.substring(BEARER.length());
    }

    @SneakyThrows
    private void loginUser(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String username = request.getParameter("login");
        String password = request.getParameter("password");
        var user = userService.findByLogin(username);

        if (user.isEmpty() || !password.equals(user.get().getPassword())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid credentials: " + username);
            return;
        }

        tokenStoreService.setTokenValidity(username, true);
        String token = JwtUtils.generateToken(username);
        response.addHeader(AUTH, BEARER + token);

        filterChain.doFilter(request, response);
    }
}
