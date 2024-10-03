package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.model.EventRegistration;

public interface EventRegistrationDetailService {
    EventRegistration findEventRegistrationByIdAndOrganizer(Long id);
}
