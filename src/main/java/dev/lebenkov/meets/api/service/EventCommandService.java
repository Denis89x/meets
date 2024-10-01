package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.event.EventRequest;

public interface EventCommandService {
    void createEvent(EventRequest eventRequest);

    void editEvent(Long eventId, EventRequest eventRequest);

    void deleteEvent(Long eventId);
}