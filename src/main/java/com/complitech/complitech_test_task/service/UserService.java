/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.service;

import com.complitech.complitech_test_task.entity.User;
import com.complitech.complitech_test_task.entity.WebSocketMessage;
import com.complitech.complitech_test_task.exception.ValidationException;
import com.complitech.complitech_test_task.repo.UserRepository;
import com.complitech.complitech_test_task.repo.specification.UserSpec;
import com.complitech.complitech_test_task.validation.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Optional<User> findByLogin(String login) {
        return userRepository.findOne(UserSpec.hasLogin(login));
    }

    public User createUser(User user) {
        UserValidator.validateUser(user);
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public User editUser(long id, User user) {
        user.setId(id);
        UserValidator.validateUser(user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @SneakyThrows
    public List<User> getAllUsersAndNotify() {
        var users = userRepository.findAll();
        String notification = new ObjectMapper().writeValueAsString(new WebSocketMessage("User", "use request GET /users"));
        messagingTemplate.convertAndSend("/topic/users", notification);
        return users;
    }

    public void deleteUsersInRange(Long startId, Long endId) {
        if (isValidId(startId) && isValidId(endId) && startId < endId) {
            userRepository.deleteUsersByIdRange(startId, endId);
            return;
        }
        throw new ValidationException("Incorrect ids: " + startId + ", " + endId);
    }

    private boolean isValidId(Long id) {
        return id != null && id >= 0;
    }
}
