package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventCommandService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.storage.dto.event.EventRequest;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventCommandServiceImpl implements EventCommandService {

    EventRepository eventRepository;
    SessionUserProviderService sessionUserProviderService;

    private Event convertEventRequestToEvent(EventRequest eventRequest) {
        return Event.builder()
                .title(eventRequest.getTitle())
                .description(eventRequest.getDescription())
                .location(eventRequest.getLocation())
                .isOnline(eventRequest.getIsOnline())
                .startTime(eventRequest.getStartTime())
                .endTime(eventRequest.getEndTime())
                .maxParticipants(eventRequest.getMaxParticipants())
                .createdAt(LocalDateTime.now())
                .organizer(sessionUserProviderService.getUserFromSession())
                .build();
    }

    @Override
    public void createEvent(EventRequest eventRequest) {
        eventRepository.save(convertEventRequestToEvent(eventRequest));
    }
}