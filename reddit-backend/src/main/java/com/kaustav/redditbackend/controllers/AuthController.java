package com.kaustav.redditbackend.controllers;

import com.kaustav.redditbackend.dto.AuthenticationResponse;
import com.kaustav.redditbackend.dto.LoginRequest;
import com.kaustav.redditbackend.dto.RegisterRequest;
import com.kaustav.redditbackend.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest)
    {
        authService.signup(registerRequest);
        return new ResponseEntity(OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest)
    {
        return authService.login(loginRequest);
    }
}