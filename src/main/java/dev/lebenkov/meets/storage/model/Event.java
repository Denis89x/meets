package dev.lebenkov.meets.storage.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long eventId;

    @Column(name = "title")
    String title;

    @Column(name = "description")
    String description;

    @Column(name = "location")
    String location;

    @Column(name = "is_online")
    Boolean isOnline;

    @Column(name = "start_time")
    LocalDateTime startTime;

    @Column(name = "end_time")
    LocalDateTime endTime;

    @Column(name = "max_participants")
    Integer maxParticipants;

    @Column(name = "current_participants")
    Integer currentParticipants;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    User organizer;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    List<EventMessage> messageList = new ArrayList<>();

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    List<EventRegistration> registrationList = new ArrayList<>();
}