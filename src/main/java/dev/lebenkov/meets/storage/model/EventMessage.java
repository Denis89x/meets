package dev.lebenkov.meets.storage.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_message")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventMessage {

    @Id
    @Column(name = "event_message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long eventMessageId;

    @Column(name = "message")
    String message;

    @Column(name = "sent_at")
    LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}