package com.kaustav.redditbackend.services;

import com.kaustav.redditbackend.dto.AuthenticationResponse;
import com.kaustav.redditbackend.dto.LoginRequest;
import com.kaustav.redditbackend.dto.RefreshTokenRequest;
import com.kaustav.redditbackend.dto.RegisterRequest;
import com.kaustav.redditbackend.models.User;
import com.kaustav.redditbackend.models.VerificationToken;
import com.kaustav.redditbackend.repositories.UserRepository;
import com.kaustav.redditbackend.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AuthService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

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


    public AuthenticationResponse login(LoginRequest loginRequest)
    {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
    {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    @Transactional
    public VerificationToken generateVerificationToken(User user)
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
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    @Transactional(readOnly = true)
    User getCurrentUser()
    {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
}