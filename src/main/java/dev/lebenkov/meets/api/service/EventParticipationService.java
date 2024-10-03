package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.event.registration.EventProcessRequest;
import dev.lebenkov.meets.storage.dto.event.registration.EventRegistrationRequest;

public interface EventParticipationService {
    void applyForEvent(EventRegistrationRequest eventRegistrationRequest);

    void processEventApplication(EventProcessRequest eventProcessRequest);
}