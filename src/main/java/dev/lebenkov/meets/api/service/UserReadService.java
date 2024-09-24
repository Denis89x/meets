package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.user.UserResponse;

public interface UserReadService {
    UserResponse fetchUserResponseById(Long userId);
}