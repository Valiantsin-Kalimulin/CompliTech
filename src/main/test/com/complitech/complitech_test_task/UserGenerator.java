/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task;

import com.complitech.complitech_test_task.entity.User;
import com.complitech.complitech_test_task.repo.UserRepository;
import com.complitech.complitech_test_task.repo.specification.UserSpec;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.complitech.complitech_test_task.TestConstant.DEFAULT_PASSWORD;
import static com.complitech.complitech_test_task.entity.Gender.NOT_SPECIFIED;

@Component
public class UserGenerator {

    @Autowired
    private UserRepository userRepository;

    public User generateUser() {
        User user = new User();
        user.setLogin(RandomStringUtils.randomAlphabetic(5, 50));
        user.setPassword(DEFAULT_PASSWORD);
        user.setFullName(RandomStringUtils.randomAlphabetic(10, 256));
        user.setGender(NOT_SPECIFIED);
        return userRepository.save(user);
    }

    public User generateUserWithLogin(String login) {
        var user = userRepository.findOne(UserSpec.hasLogin(login));
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setLogin(login);
            newUser.setPassword(DEFAULT_PASSWORD);
            newUser.setFullName(RandomStringUtils.randomAlphabetic(10, 256));
            newUser.setGender(NOT_SPECIFIED);
            return userRepository.save(newUser);
        }
        return user.get();
    }
}
