package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventDetailService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.repository.EventRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventDetailServiceImpl implements EventDetailService {

    SessionUserProviderService sessionUserProviderService;
    EventRepository eventRepository;

    private Event handleEventNotFound(Optional<Event> eventOptional, Long eventId) {
        return eventOptional.orElseThrow(() -> {
            log.error("Event with id {} not found", eventId);
            return new ObjectNotFoundException("Event with " + eventId + " id not found!");
        });
    }

    @Override
    public Event findEventByUserEventIdAndUserId(long eventId) {
        return handleEventNotFound(eventRepository.findByEventIdAndOrganizer_UserId(
                eventId, sessionUserProviderService.getUserFromSession().getUserId()), eventId);
    }

    @Override
    public Event findEventById(Long eventId) {
        return handleEventNotFound(eventRepository.findById(eventId), eventId);
    }
}
