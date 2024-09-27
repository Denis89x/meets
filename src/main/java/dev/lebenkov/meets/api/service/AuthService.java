package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.auth.AuthResponse;
import dev.lebenkov.meets.storage.dto.auth.UserAuthRequest;

public interface AuthService {
    AuthResponse authenticate(UserAuthRequest authRequest);
}