package dev.lebenkov.meets.api.controller;

import dev.lebenkov.meets.api.service.EventCommandService;
import dev.lebenkov.meets.api.service.EventReadService;
import dev.lebenkov.meets.storage.dto.event.EventRequest;
import dev.lebenkov.meets.storage.dto.event.EventResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {

    EventCommandService eventCommandService;
    EventReadService eventReadService;

    @PostMapping
    public ResponseEntity<String> createEvent(
            @RequestBody @Valid EventRequest eventRequest) {
        eventCommandService.createEvent(eventRequest);
        return ResponseEntity.ok("Event was successfully created");
    }

    @GetMapping("/{event_id}")
    public ResponseEntity<EventResponse> fetchBookResponseById(
            @PathVariable("event_id") long eventId) {
        return new ResponseEntity<>(eventReadService.fetchEventResponseById(eventId), HttpStatus.OK);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<EventResponse>> fetchBookResponsesByUserId(
            @PathVariable("user_id") long userId) {
        return new ResponseEntity<>(eventReadService.fetchAllEventResponsesByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{event_id}")
    public ResponseEntity<String> editEvent(
            @PathVariable("event_id") long eventId,
            @RequestBody @Valid EventRequest eventRequest) {
        eventCommandService.editEvent(eventId, eventRequest);
        return new ResponseEntity<>("Event was successfully updated", HttpStatus.OK); // TODO: change
    }

    @DeleteMapping("/{event_id}")
    public ResponseEntity<String> deleteEvent(
            @PathVariable("event_id") long eventId) {
        eventCommandService.deleteEvent(eventId);
        return new ResponseEntity<>("Event was successfully deleted", HttpStatus.OK); // TODO: change
    }
}