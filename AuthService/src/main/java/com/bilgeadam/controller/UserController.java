package com.bilgeadam.controller;

import com.bilgeadam.entity.User;
import com.bilgeadam.service.UserService;
import com.bilgeadam.util.enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/assign-role")
    public User assignRoleToUser(@PathVariable Long userId, @RequestParam ERole role) {
        User user = new User(); // Fetch user from repository in a real scenario
        userService.assignRoleToUser(user, role);
        return user;
    }
}