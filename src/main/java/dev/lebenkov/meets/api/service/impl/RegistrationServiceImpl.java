package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.RegistrationService;
import dev.lebenkov.meets.api.service.TokenService;
import dev.lebenkov.meets.api.util.exc.ObjectAlreadyExistsException;
import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.dto.UserRegistrationRequest;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegistrationServiceImpl implements RegistrationService {

    JwtUtilService jwtUtilService;
    TokenService tokenService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    AccountDetailsService accountDetailsService;

    // TODO: relegate this method
    private void checkExistingUser(String username) {
        userRepository.findByUsername(username)
                .ifPresent(existingAccount -> {
                    throw new ObjectAlreadyExistsException("User with username " + username + " already exists.");
                });
    }

    @Override
    @Transactional
    public AuthResponse register(UserRegistrationRequest userRegistrationRequest) {
        checkExistingUser(userRegistrationRequest.getUsername());

        User user = User.builder()
                .username(userRegistrationRequest.getUsername().toLowerCase())
                .email(userRegistrationRequest.getEmail())
                .password(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        UserDetails userDetails = accountDetailsService.loadUserByUsername(user.getUsername());

        String jwtToken = jwtUtilService.generateToken(userDetails);
        String refreshToken = jwtUtilService.generateRefreshToken(userDetails);

        tokenService.saveUserToken(savedUser, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
