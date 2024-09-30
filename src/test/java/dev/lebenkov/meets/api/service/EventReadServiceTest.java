package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.api.service.impl.EventReadServiceImpl;
import dev.lebenkov.meets.storage.dto.event.EventResponse;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventReadServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventReadService eventReadService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .userId(1L)
                .username("testUsername")
                .build();

        eventReadService = new EventReadServiceImpl(eventRepository);
    }

    @Test
    public void EventReadService_FetchEventResponseById_ReturnsEventResponse() {
        // Arrange
        long eventId = 1;

        Event event = Event.builder()
                .eventId(eventId)
                .title("testTitle")
                .organizer(user)
                .build();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        // Act
        EventResponse eventResponse = eventReadService.fetchEventResponseById(eventId);

        // Assert
        assertNotNull(eventResponse);
        assertEquals(user.getUsername(), eventResponse.getOrganizerUsername());
        assertEquals(eventId, eventResponse.getEventId());
    }

    @Test
    public void EventReadService_FetchAllEventResponsesByUserId_ReturnsEventResponses() {
        // Arrange
        long userId = 1;

        Event event1 = Event.builder()
                .title("testTitle1")
                .organizer(user)
                .build();

        Event event2 = Event.builder()
                .title("testTitle2")
                .organizer(user)
                .build();

        Event event3 = Event.builder()
                .title("testTitle3")
                .organizer(user)
                .build();

        when(eventRepository.findAllByOrganizer_UserId(userId)).thenReturn(List.of(event1, event2, event3));

        // Act
        List<EventResponse> eventResponses = eventReadService.fetchAllEventResponsesByUserId(userId);

        // Assert
        assertNotNull(eventResponses);
        assertEquals(3, eventResponses.size());
    }
}