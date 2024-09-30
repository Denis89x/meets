package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.api.service.impl.SessionUserProviderServiceImpl;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionUserProviderServiceTest {

    @Mock
    private UserRepository userRepository;

    private SessionUserProviderService sessionUserProviderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sessionUserProviderService = new SessionUserProviderServiceImpl(userRepository);
    }

    @Test
    public void SessionUserProviderService_GetUserFromSession_ReturnsUser() {
        // Arrange
        String username = "testUser";
        User expectedUser = User.builder()
                .username(username)
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = sessionUserProviderService.getUserFromSession();

        // Assert
        assertNotNull(actualUser);
        assertEquals(username, actualUser.getUsername());
    }

    @Test
    public void SessionUserProviderService_GetUserFromSession_ThrowsExceptionWhenUserNotFound() {
        // Arrange
        String username = "testUser";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(ObjectNotFoundException.class, () -> sessionUserProviderService.getUserFromSession());

        // Assert
        assertEquals(ObjectNotFoundException.class, exception.getClass());
        assertEquals("User not found in session", exception.getMessage());
    }
}