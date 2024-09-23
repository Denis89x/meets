package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.RegistrationService;
import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.dto.UserRegistrationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationServiceImpl implements RegistrationService {

    JwtUtilService jwtUtilService;

    @Override
    public AuthResponse register(UserRegistrationRequest userRegistrationRequest) {
        return null;
    }
}
