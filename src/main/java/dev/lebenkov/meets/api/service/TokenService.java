package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface TokenService {
    void saveUserToken(User user, String jwtToken);

    void revokeAllUserTokens(User user);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
