package com.aiAssistant.review.service.impl;

import com.aiAssistant.review.dto.auth.AuthenticationResponse;
import com.aiAssistant.review.dto.auth.LoginRequest;
import com.aiAssistant.review.dto.auth.RegisterRequest;
import com.aiAssistant.review.entity.Role;
import com.aiAssistant.review.entity.User;
import com.aiAssistant.review.repository.UserRepository;
import com.aiAssistant.review.security.JwtService;
import com.aiAssistant.review.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl  implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");        }
        User user= User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                       request.getPassword()
               )
       );
       User user=userRepository.findByEmail(request.getEmail())
               .orElseThrow(()->
                       new UsernameNotFoundException("User Not Found"));
       String token= jwtService.generateToken(user.getEmail());
       return AuthenticationResponse.builder().token(token).build();
    }
}

