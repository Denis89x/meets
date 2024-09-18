package dev.lebenkov.meets.storage.repository;

import dev.lebenkov.meets.storage.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
