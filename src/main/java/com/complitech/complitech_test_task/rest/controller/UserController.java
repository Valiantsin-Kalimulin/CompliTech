/*
 * (c) 2024 Valiantsin Kalimulin. All Right Reserved. All information contained herein is, and remains the
 * property of Valiantsin Kalimulin and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from Valiantsin Kalimulin
 */

package com.complitech.complitech_test_task.rest.controller;

import com.complitech.complitech_test_task.entity.User;
import com.complitech.complitech_test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.complitech.complitech_test_task.constant.AppConstant.API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = API_PATH + "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.editUser(id, user));
    }

    @GetMapping()
    @SneakyThrows
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsersAndNotify());
    }

    @DeleteMapping("/in-range")
    public ResponseEntity<?> deleteUsersInRange(@RequestParam Long startId, @RequestParam Long endId) {
        userService.deleteUsersInRange(startId, endId);
        return ResponseEntity.ok().build();
    }
}
