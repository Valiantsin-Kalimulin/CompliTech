/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task;

import com.complitech.complitech_test_task.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserGenerator userGenerator;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldCreateUser() throws Exception {
        var user = userGenerator.generateUser();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(user.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(user.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(user.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(user.getGender().name()));
    }

    @Test
    @Disabled // need to configure the server to testing work with WebSocket, out of the scope of tasks
    public void shouldReturnListUsers() throws Exception {
        userGenerator.generateUser();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        var user = userGenerator.generateUser();
        user = userRepository.findAll().get(0);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + user.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assertions.assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    public void shouldEditUser() throws Exception {
        var user = userGenerator.generateUser();
        var newName = "Edited full name";
        user.setFullName(newName);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(newName));
    }

    @Test
    public void shouldDeleteUsersInRange() throws Exception {
        userRepository.deleteAll();
        var userId = userGenerator.generateUser().getId();
        for (int i = 0; i < 5; i++) {
            userGenerator.generateUser();
        }

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/in-range")
                        .param("startId", userId.toString())
                        .param("endId", Long.toString(userId + 4)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        var users = userRepository.findAll();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(userId + 5, users.get(0).getId());
    }
}
