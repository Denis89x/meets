package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.dto.UserAuthRequest;

public interface AuthService {
    AuthResponse authenticate(UserAuthRequest authRequest);
}