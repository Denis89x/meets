package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.dto.UserRegistrationRequest;

public interface RegistrationService {
    AuthResponse register(UserRegistrationRequest userRegistrationRequest);
}
