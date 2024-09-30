package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.impl.AccountDetailsService;
import dev.lebenkov.meets.api.service.impl.TokenServiceImpl;
import dev.lebenkov.meets.storage.model.Token;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.TokenRepository;
import dev.lebenkov.meets.storage.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private AccountDetailsService accountDetailsService;

    private TokenService tokenService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .userId(1L)
                .username("testUsername")
                .build();

        tokenService = new TokenServiceImpl(userRepository, tokenRepository, jwtUtilService, accountDetailsService);
    }

    @Test
    public void TokenService_SaveUserToken_SavesTokenSuccessfully() {
        // Arrange
        String jwtToken = "testJwtToken";

        // Act
        tokenService.saveUserToken(user, jwtToken);

        // Assert
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository).save(tokenCaptor.capture());
        Token savedToken = tokenCaptor.getValue();

        assertNotNull(savedToken);
        assertEquals(user, savedToken.getUser());
        assertEquals(jwtToken, savedToken.getToken());
        assertFalse(savedToken.isExpired());
        assertFalse(savedToken.isRevoked());
    }

    @Test
    public void TokenService_RevokeAllUserTokens_MarksTokensAsExpiredAndRevoked() {
        // Arrange
        Token validToken1 = Token.builder().expired(false).revoked(false).build();
        Token validToken2 = Token.builder().expired(false).revoked(false).build();

        when(tokenRepository.findAllValidTokenByUser(user.getUserId()))
                .thenReturn(List.of(validToken1, validToken2));

        // Act
        tokenService.revokeAllUserTokens(user);

        // Assert
        assertTrue(validToken1.isExpired());
        assertTrue(validToken1.isRevoked());
        assertTrue(validToken2.isExpired());
        assertTrue(validToken2.isRevoked());

        verify(tokenRepository).saveAll(anyList());
    }

    // TODO: fix the test
    @Test
    public void TokenService_RefreshToken_GeneratesNewAccessToken() throws IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String username = user.getUsername();
        String refreshToken = "testRefreshToken";
        String accessToken = "newAccessToken";

        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtUtilService.extractUsername(refreshToken)).thenReturn(username);
        when(accountDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtUtilService.isTokenValid(refreshToken, userDetails)).thenReturn(true);
        when(jwtUtilService.generateToken(userDetails)).thenReturn(accessToken);

        // Act
        tokenService.refreshToken(request, response);

        // Assert
        verify(tokenService).saveUserToken(user, accessToken);
        verify(response).getOutputStream();
    }
}