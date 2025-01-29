package com.bilgeadam.controller;

import com.bilgeadam.entity.User;
import com.bilgeadam.service.UserService;
import com.bilgeadam.util.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/assign-role")
    public User assignRoleToUser(@PathVariable Long userId, @RequestParam Role role) {
        User user = new User(); // Fetch user from repository in a real scenario
        userService.assignRoleToUser(user, role);
        return user;
    }
}