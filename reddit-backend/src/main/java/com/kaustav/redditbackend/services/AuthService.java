package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.RegisterRequest;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.models.VerificationToken;
import com.kaustav.redditbackend.repositories.UserRepository;
import com.kaustav.redditbackend.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest)
    {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(true);
        VerificationToken token = generateVerificationToken(user);
        user.setToken(token);

        userRepository.save(user);
    }

    @Transactional
    private VerificationToken generateVerificationToken(User user)
    {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    private String encodePassword(String password)
    {
        return passwordEncoder.encode(password);
    }
}