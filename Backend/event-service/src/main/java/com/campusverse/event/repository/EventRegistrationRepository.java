package com.campusverse.event.repository;

import com.campusverse.event.entity.EventRegistration;
import com.campusverse.event.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    List<EventRegistration> findByEventIdOrderByRegisteredAtAsc(Long eventId);

    List<EventRegistration> findByStudentIdOrderByRegisteredAtDesc(Long studentId);

    Optional<EventRegistration> findByEventIdAndStudentId(Long eventId, Long studentId);

    boolean existsByEventIdAndStudentId(Long eventId, Long studentId);

    long countByEventIdAndStatusIn(Long eventId, List<RegistrationStatus> statuses);

    /** First student on the waiting list (first come, first served). */
    Optional<EventRegistration> findFirstByEventIdAndStatusOrderByRegisteredAtAsc(Long eventId, RegistrationStatus status);
}
