package dev.lebenkov.meets.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.TokenService;
import dev.lebenkov.meets.storage.dto.AuthResponse;
import dev.lebenkov.meets.storage.enums.TokenType;
import dev.lebenkov.meets.storage.model.Token;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.TokenRepository;
import dev.lebenkov.meets.storage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenServiceImpl implements TokenService {

    UserRepository userRepository;
    TokenRepository tokenRepository;

    JwtUtilService jwtUtilService;

    AccountDetailsService accountDetailsService;

    @Override
    public void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());

        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);

        username = jwtUtilService.extractUsername(refreshToken);

        if (username != null) {
            UserDetails userDetails = accountDetailsService.loadUserByUsername(username);
            User user = userRepository.findByUsername(username).orElseThrow();

            if (jwtUtilService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtUtilService.generateToken(userDetails);

                revokeAllUserTokens(user);

                saveUserToken(user, accessToken);

                AuthResponse authResponse = AuthResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
