package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.storage.dto.event.EventResponse;

import java.util.List;

public interface EventReadService {
    EventResponse fetchEventResponseById(Long eventId);

    List<EventResponse> fetchAllEventResponsesByUserId(Long userId);
}