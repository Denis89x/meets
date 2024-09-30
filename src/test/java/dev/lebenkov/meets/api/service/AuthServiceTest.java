package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.api.security.JwtUtilService;
import dev.lebenkov.meets.api.service.impl.AccountDetailsService;
import dev.lebenkov.meets.api.service.impl.AuthServiceImpl;
import dev.lebenkov.meets.storage.dto.auth.AuthResponse;
import dev.lebenkov.meets.storage.dto.auth.UserAuthRequest;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AccountDetailsService accountDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authService = new AuthServiceImpl(userRepository, jwtUtilService, tokenService, accountDetailsService, authenticationManager);
    }

    @Test
    public void AuthService_Authenticate_ReturnsAuthResponse() {
        // Arrange
        UserAuthRequest userAuthRequest = UserAuthRequest.builder()
                .username("username")
                .password("password")
                .build();

        UserDetails userDetails = mock(UserDetails.class);

        // Act
        when(userRepository.findByUsername(userAuthRequest.getUsername())).thenReturn(Optional.of(User.builder().build()));
        when(accountDetailsService.loadUserByUsername(userAuthRequest.getUsername())).thenReturn(userDetails);
        when(jwtUtilService.generateToken(userDetails)).thenReturn("accessToken");
        when(jwtUtilService.generateRefreshToken(userDetails)).thenReturn("refreshToken");

        AuthResponse response = authService.authenticate(userAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }
}