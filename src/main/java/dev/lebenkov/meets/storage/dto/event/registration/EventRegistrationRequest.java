package dev.lebenkov.meets.storage.dto.event.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRegistrationRequest {

    @JsonProperty("event_id")
    Long eventId;
}