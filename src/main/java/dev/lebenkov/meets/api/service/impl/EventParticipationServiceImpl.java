package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventDetailService;
import dev.lebenkov.meets.api.service.EventParticipationService;
import dev.lebenkov.meets.api.service.EventRegistrationDetailService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.api.util.exc.MaxParticipantsReachedException;
import dev.lebenkov.meets.storage.dto.event.registration.EventProcessRequest;
import dev.lebenkov.meets.storage.dto.event.registration.EventRegistrationRequest;
import dev.lebenkov.meets.storage.enums.EventStatus;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.model.EventRegistration;
import dev.lebenkov.meets.storage.repository.EventRegistrationRepository;
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
public class EventParticipationServiceImpl implements EventParticipationService {

    EventRepository eventRepository;
    EventDetailService eventDetailService;
    SessionUserProviderService sessionUserProviderService;
    EventRegistrationDetailService eventRegistrationDetailService;
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

    private void throwMaxParticipantsReachedException(Event event) {
        log.info("Max participants reached for event with id {}. Current participants: {}, Max participants: {}",
                event.getEventId(), event.getCurrentParticipants(), event.getMaxParticipants());

        throw new MaxParticipantsReachedException(event.getEventId());
    }

    @Override
    public void applyForEvent(EventRegistrationRequest eventRegistrationRequest) {
        Event event = eventDetailService.findEventById(eventRegistrationRequest.getEventId());

        if (hasAvailableSlots(event))
            saveEventRegistration(createEventRegistration(event));
        else {
            throwMaxParticipantsReachedException(event);
        }
    }

    private Event addNewParticipantToEventCounter(Event event) {
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        return event;
    }

    private void updateParticipantCounter(EventRegistration eventRegistration) {
        Event event = addNewParticipantToEventCounter(eventRegistration.getEvent());
        eventRepository.save(event);
    }

    private void confirmEventRegistration(EventRegistration eventRegistration) {
        if (hasAvailableSlots(eventRegistration.getEvent())) {
            updateParticipantCounter(eventRegistration);

        } else {
            throwMaxParticipantsReachedException(eventRegistration.getEvent());
        }
    }

    private void updateEventRegistration(EventRegistration eventRegistration, EventStatus eventStatus) {
        eventRegistration.setStatus(eventStatus);
        eventRegistrationRepository.save(eventRegistration);
    }

    private void changeEventRegistrationStatus(EventRegistration eventRegistration, String status) {
        switch (status) {
            case "CONFIRMED":
                confirmEventRegistration(eventRegistration);
                updateEventRegistration(eventRegistration, EventStatus.CONFIRMED);
                break;
            case "CANCELLED":
                updateEventRegistration(eventRegistration, EventStatus.CANCELLED);
                break;
        }
    }

    @Override
    public void processEventApplication(EventProcessRequest eventProcessRequest) {
        EventRegistration eventRegistration = eventRegistrationDetailService
                .findEventRegistrationByIdAndOrganizer(eventProcessRequest.getEventRegistrationId());

        changeEventRegistrationStatus(eventRegistration, eventProcessRequest.getStatus());
    }
}