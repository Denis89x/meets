package dev.lebenkov.meets.api.service.impl;

import dev.lebenkov.meets.api.service.EventRegistrationDetailService;
import dev.lebenkov.meets.api.service.SessionUserProviderService;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.model.EventRegistration;
import dev.lebenkov.meets.storage.repository.EventRegistrationRepository;
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
public class EventRegistrationDetailServiceImpl implements EventRegistrationDetailService {

    SessionUserProviderService sessionUserProviderService;
    EventRegistrationRepository eventRegistrationRepository;

    private EventRegistration handleEventRegistrationNotFound(Optional<EventRegistration> eventRegistrationOptional, Long eventRegistrationId) {
        return eventRegistrationOptional.orElseThrow(() -> {
            log.error("Event registration with id {} not found", eventRegistrationId);
            return new ObjectNotFoundException("Event registration with " + eventRegistrationId + " id not found!");
        });
    }

    @Override
    public EventRegistration findEventRegistrationByIdAndOrganizer(Long id) {
        return handleEventRegistrationNotFound(eventRegistrationRepository.findByRegistrationIdAndEvent_Organizer_UserId(
                id, sessionUserProviderService.getUserFromSession().getUserId()), id);
    }
}