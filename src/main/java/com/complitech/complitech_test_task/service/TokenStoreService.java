/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenStoreService {

    private final ConcurrentHashMap<String, Boolean> TOKEN_VALIDITY_STORE = new ConcurrentHashMap<>();

    public void setTokenValidity(String userName, boolean isValid) {
        TOKEN_VALIDITY_STORE.put(userName, isValid);
    }

    public boolean getTokenValidity(String userName) {
        return TOKEN_VALIDITY_STORE.getOrDefault(userName, false);
    }

    public void invalidateToken(String userName) {
        TOKEN_VALIDITY_STORE.put(userName, false);
    }
}
