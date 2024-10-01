package dev.lebenkov.meets.storage.repository;

import dev.lebenkov.meets.storage.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByOrganizer_UserId(Long userId);

    Optional<Event> findByEventIdAndOrganizer_UserId(Long eventId, Long organizerUserId);

    void deleteByEventIdAndOrganizer_UserId(Long eventId, Long organizerUserId);
}