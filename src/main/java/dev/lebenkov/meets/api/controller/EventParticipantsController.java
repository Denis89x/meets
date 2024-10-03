package dev.lebenkov.meets.api.controller;

import dev.lebenkov.meets.api.service.EventParticipationService;
import dev.lebenkov.meets.storage.dto.event.registration.EventProcessRequest;
import dev.lebenkov.meets.storage.dto.event.registration.EventRegistrationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event/participants")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventParticipantsController {

    EventParticipationService eventParticipationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForEvent(@RequestBody EventRegistrationRequest eventRegistrationRequest) {
        eventParticipationService.applyForEvent(eventRegistrationRequest);
        return ResponseEntity.ok("Application for the event has been successfully submitted");
    }

    @PatchMapping("/process/{event_registration_id}")
    public ResponseEntity<String> processEventRegistration(
            @PathVariable("event_registration_id") Long eventRegistrationId,
            @RequestBody EventProcessRequest eventProcessRequest) {
        eventParticipationService.processEventApplication(eventRegistrationId, eventProcessRequest);
        return ResponseEntity.ok("The participation status of the event has been successfully changed");
    }
}