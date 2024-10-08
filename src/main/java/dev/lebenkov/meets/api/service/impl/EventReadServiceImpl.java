package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventDetailService;
import dev.lebenkov.meets.api.service.EventReadService;
import dev.lebenkov.meets.storage.dto.event.EventResponse;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventReadServiceImpl implements EventReadService {

    EventRepository eventRepository;
    EventDetailService eventDetailService;

    private EventResponse convertEventToEventResponse(Event event) {
        return EventResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .isOnline(event.getIsOnline())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .maxParticipants(event.getMaxParticipants())
                .createdAt(event.getCreatedAt())
                .organizerUsername(event.getOrganizer().getUsername())
                .build();
    }

    @Override
    public EventResponse fetchEventResponseById(Long eventId) {
        return convertEventToEventResponse(eventDetailService.findEventById(eventId));
    }

    @Override
    public List<EventResponse> fetchAllEventResponsesByUserId(Long userId) {
        return eventRepository.findAllByOrganizer_UserId(userId).stream()
                .map(this::convertEventToEventResponse)
                .toList();
    }
}