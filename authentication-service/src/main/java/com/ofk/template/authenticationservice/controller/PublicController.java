package com.ofk.template.authenticationservice.controller;

import com.ofk.template.authenticationservice.dto.UserDTO;
import com.ofk.template.authenticationservice.model.User;
import com.ofk.template.authenticationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicController {
    private final UserService userService;

    @Autowired
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("signup")
    public ResponseEntity<User> signup(@RequestBody UserDTO userDTO) {
        return userService.signup(userDTO);
    }

    @GetMapping("test")
    public String test() {
        return "hi";
    }
}
