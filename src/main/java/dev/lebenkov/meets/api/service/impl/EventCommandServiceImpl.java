package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventCommandService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.dto.event.EventRequest;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventCommandServiceImpl implements EventCommandService {

    ModelMapper modelMapper;
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

    private Event convertEventRequestToEvent(Event event, EventRequest eventRequest) {
        modelMapper.map(eventRequest, event);
        return event;
    }

    private Event findEventByUserEventIdAndUserId(long eventId) {
        return eventRepository.findByEventIdAndOrganizer_UserId(
                        eventId, sessionUserProviderService.getUserFromSession().getUserId())
                .orElseThrow(() -> {
                    log.error("Event with id {} not found", eventId);
                    return new ObjectNotFoundException("Event with " + eventId + " id not found!");
                });
    }

    @Override
    public void editEvent(Long eventId, EventRequest eventRequest) {
        Event event = findEventByUserEventIdAndUserId(eventId);

        eventRepository.save(convertEventRequestToEvent(event, eventRequest));
    }

    @Override
    public void deleteEvent(Long eventId) {
        findEventByUserEventIdAndUserId(eventId);

        eventRepository.deleteByEventIdAndOrganizer_UserId(
                eventId, sessionUserProviderService.getUserFromSession().getUserId()
        );
    }
}