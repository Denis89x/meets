package dev.lebenkov.meets.storage.repository;

import dev.lebenkov.meets.storage.model.EventMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventMessageRepository extends JpaRepository<EventMessage, Long> {
}
