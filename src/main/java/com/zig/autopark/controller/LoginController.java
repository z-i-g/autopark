package com.zig.autopark.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LoginController {

    @GetMapping("/login")
    String login() {
        return "login/login";
    }
}