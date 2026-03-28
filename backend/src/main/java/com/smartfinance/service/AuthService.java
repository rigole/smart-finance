package com.smartfinance.service;

import com.smartfinance.dto.request.LoginRequest;
import com.smartfinance.dto.request.RegisterRequest;
import com.smartfinance.dto.response.AuthResponse;
import com.smartfinance.model.User;
import com.smartfinance.repository.UserRepository;
import com.smartfinance.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest  request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameNotFoundException("Email already in use");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getFullName(),  user.getRole());
    }
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getFullName(),  user.getRole());
    }
}
