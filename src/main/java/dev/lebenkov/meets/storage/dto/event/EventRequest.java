package dev.lebenkov.meets.storage.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.lebenkov.meets.api.util.validation.ValidDateRange;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidDateRange(startField = "startTime", endField = "endTime")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequest {

    @JsonProperty("title")
    String title;

    @JsonProperty("description")
    String description;

    @JsonProperty("location")
    String location;

    @JsonProperty("is_online")
    Boolean isOnline;

    @JsonProperty("start_time")
    LocalDateTime startTime;

    @JsonProperty("end_time")
    LocalDateTime endTime;

    @JsonProperty("max_participants")
    Integer maxParticipants;
}