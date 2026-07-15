package com.aiAssistant.review.service;

import com.aiAssistant.review.dto.auth.AuthenticationResponse;
import com.aiAssistant.review.dto.auth.LoginRequest;
import com.aiAssistant.review.dto.auth.RegisterRequest;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(LoginRequest request);
}
