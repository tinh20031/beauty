package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/{id}")
    public String getUserName(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return "User name: " + user.getName();
        }
        return "User not found";
    }
}
