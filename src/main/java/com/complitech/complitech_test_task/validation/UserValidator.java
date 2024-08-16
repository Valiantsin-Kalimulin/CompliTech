/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.validation;

import com.complitech.complitech_test_task.entity.User;
import com.complitech.complitech_test_task.exception.ValidationException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserValidator {

    private static final int LOGIN_MAX_SIZE = 50;
    private static final int LOGIN_MIN_SIZE = 5;
    private static final int PASSWORD_MAX_SIZE = 20;
    private static final int FULL_NAME_MAX_SIZE = 256;
    private static final int FULL_NAME_MIN_SIZE = 10;
    private static final String REGEX_PASS = "^(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(?=.*\\d.*\\d.*\\d)[\\s\\S]{7,}$";

    public void validateUser(User user) {
        validateLogin(user.getLogin());
        validatePassword(user.getPassword());
        validateFullName(user.getFullName());
    }

    private void validatePassword(String password) {
        var passwordCond = password.length() >= 7 &&
                password.length() <= PASSWORD_MAX_SIZE &&
                password.matches(REGEX_PASS);
        if (!passwordCond) {
            throw new ValidationException("Invalid password");
        }
    }

    private void validateLogin(String login) {
        if (login.length() > LOGIN_MAX_SIZE || login.length() < LOGIN_MIN_SIZE) {
            throw new ValidationException("Login is too long");
        }
    }

    private void validateFullName(String fullName) {
        if (fullName.length() > FULL_NAME_MAX_SIZE || fullName.length() < FULL_NAME_MIN_SIZE) {
            throw new ValidationException("Full name is too long");
        }
    }
}
