/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.repo.specification;

import com.complitech.complitech_test_task.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpec {
    public static Specification<User> hasLogin(String login) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("login"), login);
    }
}
