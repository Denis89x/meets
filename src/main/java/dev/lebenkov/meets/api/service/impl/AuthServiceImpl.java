package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.AuthService;
import dev.lebenkov.meets.api.service.TokenService;
import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.dto.UserAuthRequest;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    JwtUtilService jwtUtilService;
    TokenService tokenService;
    AccountDetailsService accountDetailsService;
    AuthenticationManager authenticationManager;

    // TODO: relegate this method
    private User checkUserExists(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public AuthResponse authenticate(UserAuthRequest authRequest) {
        User account = checkUserExists(authRequest.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        UserDetails user = accountDetailsService.loadUserByUsername(authRequest.getUsername());

        String jwtToken = jwtUtilService.generateToken(user);
        String refreshToken = jwtUtilService.generateRefreshToken(user);

        tokenService.revokeAllUserTokens(account);
        tokenService.saveUserToken(account, jwtToken);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}