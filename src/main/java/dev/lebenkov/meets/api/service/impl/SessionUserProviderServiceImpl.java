package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionUserProviderServiceImpl implements SessionUserProviderService {

    UserRepository userRepository;

    @Override
    public User getUserFromSession() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> {
                    log.error("User not found in session");
                    return new ObjectNotFoundException("User not found in session");
                });
    }
}