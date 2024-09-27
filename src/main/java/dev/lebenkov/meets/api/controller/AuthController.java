package dev.lebenkov.meets.api.controller;

import dev.lebenkov.meets.api.service.AuthService;
import dev.lebenkov.meets.api.service.RegistrationService;
import dev.lebenkov.meets.api.service.TokenService;
import dev.lebenkov.meets.storage.dto.auth.AuthResponse;
import dev.lebenkov.meets.storage.dto.auth.UserAuthRequest;
import dev.lebenkov.meets.storage.dto.auth.UserRegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/auth")
public class AuthController {

    TokenService tokenService;
    RegistrationService registrationService;
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(registrationService.register(userRegistrationRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody @Valid UserAuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        tokenService.refreshToken(request, response);
    }
}