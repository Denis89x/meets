package dev.lebenkov.meets.storage.repository;

import dev.lebenkov.meets.storage.model.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    Optional<EventRegistration> findByRegistrationIdAndEvent_Organizer_UserId(Long registrationId, Long userId);
}
