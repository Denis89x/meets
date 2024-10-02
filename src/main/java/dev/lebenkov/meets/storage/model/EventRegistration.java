package dev.lebenkov.meets.storage.model;

import dev.lebenkov.meets.storage.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_registration")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRegistration {

    @Id
    @Column(name = "registration_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long registrationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    EventStatus status;

    @Column(name = "registered_at")
    LocalDateTime registeredAt;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}