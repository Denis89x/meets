package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.auth.AuthResponse;
import dev.lebenkov.meets.storage.dto.auth.UserRegistrationRequest;

public interface RegistrationService {
    AuthResponse register(UserRegistrationRequest userRegistrationRequest);
}
