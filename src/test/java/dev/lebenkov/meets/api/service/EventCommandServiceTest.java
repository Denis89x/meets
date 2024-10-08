package dev.lebenkov.meets.api.service;

import dev.lebenkov.meets.api.service.impl.EventCommandServiceImpl;
import dev.lebenkov.meets.api.util.exc.ObjectNotFoundException;
import dev.lebenkov.meets.storage.dto.event.EventRequest;
import dev.lebenkov.meets.storage.model.Event;
import dev.lebenkov.meets.storage.model.User;
import dev.lebenkov.meets.storage.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventCommandServiceTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SessionUserProviderService sessionUserProviderService;

    @Mock
    private EventDetailService eventDetailService;

    private EventCommandService eventCommandService;

    private User user;
    private Event event;
    private EventRequest eventRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .userId(1L)
                .username("testUser")
                .build();

        event = Event.builder()
                .eventId(1L)
                .title("Old Title")
                .organizer(user)
                .build();

        eventRequest = EventRequest.builder()
                .title("New Title")
                .build();

        eventCommandService = new EventCommandServiceImpl(modelMapper, eventRepository, eventDetailService, sessionUserProviderService);
    }

    @Test
    public void EventCommandService_CreateEvent_SavesEventSuccessfully() {
        // Arrange
        when(sessionUserProviderService.getUserFromSession()).thenReturn(user);

        // Act
        eventCommandService.createEvent(eventRequest);

        // Assert
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    public void EventCommandService_EditEvent_ModifiesWhenEventExists() {
        // Arrange
        when(eventDetailService.findEventByUserEventIdAndUserId(anyLong())).thenReturn(event);

        // Act
        eventCommandService.editEvent(1L, eventRequest);

        // Assert
        verify(eventRepository).save(event);

        assertEquals("New Title", event.getTitle(), "The event title should be updated");
    }

    @Test
    public void EventCommandService_DeleteEvent_DeletesWhenEventExists() {
        // Arrange
        when(sessionUserProviderService.getUserFromSession()).thenReturn(user);
        when(eventDetailService.findEventByUserEventIdAndUserId(anyLong())).thenReturn(event);

        // Act
        eventCommandService.deleteEvent(1L);

        // Assert
        verify(eventDetailService).findEventByUserEventIdAndUserId(anyLong());
        verify(eventRepository).deleteByEventIdAndOrganizer_UserId(1L, event.getOrganizer().getUserId());
    }

    @Test
    public void EventCommandService_DeleteEvent_ThrowExceptionWhenEventNotFound() {
        // Arrange

        // Act

        // Assert
        verify(eventRepository, never()).deleteByEventIdAndOrganizer_UserId(anyLong(), anyLong());
    }
}