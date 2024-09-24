package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.model.User;

public interface SessionUserProviderService {
    User getUserFromSession();
}