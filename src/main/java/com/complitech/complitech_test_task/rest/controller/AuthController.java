/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.rest.controller;

import com.complitech.complitech_test_task.service.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.complitech.complitech_test_task.constant.AppConstant.API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = API_PATH + "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final TokenStoreService tokenStoreService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String login, @RequestParam String password) {
        return ResponseEntity.ok("User logged in successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser() {
        tokenStoreService.invalidateToken(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok("User logged out successfully.");
    }
}
