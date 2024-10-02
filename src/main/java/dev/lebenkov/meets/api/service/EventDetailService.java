package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.model.Event;

public interface EventDetailService {
    Event findEventByUserEventIdAndUserId(long eventId);

    Event findEventById(Long eventId);
}