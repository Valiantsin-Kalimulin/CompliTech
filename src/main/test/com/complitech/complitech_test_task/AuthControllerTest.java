/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task;

import com.complitech.complitech_test_task.service.TokenStoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.complitech.complitech_test_task.TestConstant.*;
import static com.complitech.complitech_test_task.constant.AppConstant.AUTH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserGenerator userGenerator;
    @Autowired
    private TokenStoreService tokenStoreService;

    @Test
    public void shouldLoginUser() throws Exception {
        userGenerator.generateUserWithLogin(LOGIN_F);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(LOGIN, LOGIN_F)
                        .param(PASSWORD, DEFAULT_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully."));

        Assertions.assertTrue(tokenStoreService.getTokenValidity(LOGIN_F));
    }

    @Test
    public void shouldNotLoginUser() throws Exception {
        userGenerator.generateUserWithLogin(LOGIN_S);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(LOGIN, LOGIN_F)
                        .param(PASSWORD, DEFAULT_PASSWORD))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Invalid credentials: " + LOGIN_F));

        Assertions.assertFalse(tokenStoreService.getTokenValidity(LOGIN_S));
        Assertions.assertFalse(tokenStoreService.getTokenValidity(LOGIN_F));
    }

    @Test
    public void shouldLogoutUser() throws Exception {
        userGenerator.generateUserWithLogin(LOGIN_S);

        var mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(LOGIN, LOGIN_S)
                        .param(PASSWORD, DEFAULT_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully."))
                .andReturn();
        Assertions.assertTrue(tokenStoreService.getTokenValidity(LOGIN_S));

        var authorizationHeader = mvcResult.getResponse().getHeader(AUTH);
        mockMvc.perform(post("/api/auth/logout")
                        .header(AUTH, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged out successfully."));

        Assertions.assertFalse(tokenStoreService.getTokenValidity(LOGIN_S));
    }

    @Test
    public void shouldDenyAccessToApiAfterLogoutUser() throws Exception {
        var user = userGenerator.generateUserWithLogin(LOGIN_T);

        var mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(LOGIN, LOGIN_T)
                        .param(PASSWORD, DEFAULT_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged in successfully."))
                .andReturn();

        var authorizationHeader = mvcResult.getResponse().getHeader(AUTH);

        mockMvc.perform(post("/api/auth/logout")
                        .header(AUTH, authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(content().string("User logged out successfully."));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + user.getId()))
                .andExpect(status().is4xxClientError());
    }
}
