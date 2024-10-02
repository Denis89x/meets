package dev.lebenkov.meets.storage.dto.event.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRegistrationResponse {

    @JsonProperty("event_registration_id")
    Long eventRegistrationId;

    String status;

    @JsonProperty("registered_at")
    LocalDateTime registeredAt;

    @JsonProperty("event_title")
    String eventTitle;
}