package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventDetailService;
import dev.lebenkov.meets.api.service.EventParticipationService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.storage.dto.event.registration.EventRegistrationRequest;
import dev.lebenkov.meets.storage.enums.EventStatus;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.model.EventRegistration;
import dev.lebenkov.meets.storage.repository.EventRegistrationRepository;
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
public class EventParticipationServiceImpl implements EventParticipationService {

    EventDetailService eventDetailService;
    SessionUserProviderService sessionUserProviderService;
    EventRegistrationRepository eventRegistrationRepository;

    private boolean hasAvailableSlots(Event event) {
        return event.getCurrentParticipants() < event.getMaxParticipants();
    }

    private EventRegistration createEventRegistration(Event event) {
        return EventRegistration.builder()
                .status(EventStatus.PENDING)
                .registeredAt(LocalDateTime.now())
                .event(event)
                .user(sessionUserProviderService.getUserFromSession())
                .build();
    }

    private void saveEventRegistration(EventRegistration eventRegistration) {
        eventRegistrationRepository.save(eventRegistration);
    }

    @Override
    public void applyForEvent(EventRegistrationRequest eventRegistrationRequest) {
        Event event = eventDetailService.findEventById(eventRegistrationRequest.getEventId());

        if (hasAvailableSlots(event))
            saveEventRegistration(createEventRegistration(event));
        else
            log.info("Max participants reached for event with id {}", eventRegistrationRequest.getEventId());
    }

    @Override
    public void processEventApplication() {

    }
}