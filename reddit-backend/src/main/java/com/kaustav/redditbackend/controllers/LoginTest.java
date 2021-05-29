package com.kaustav.redditbackend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginTest
{
    @GetMapping(value = "/login-test")
    public String testLogin()
    {
        return ("Login successful.");
    }
}