package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.UserReadService;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.dto.user.UserResponse;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserReadServiceImpl implements UserReadService {

    UserRepository userRepository;
    ModelMapper modelMapper;

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id {} not found", userId);
            return new ObjectNotFoundException("User with " + userId + " id not found!");
        });
    }

    @Override
    public UserResponse fetchUserResponseById(Long userId) {
        return modelMapper.map(findUserById(userId), UserResponse.class);
    }
}