package com.campusverse.event.repository;

import com.campusverse.event.entity.Event;
import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    List<Event> findByCategory(EventCategory category);

    List<Event> findByClubId(Long clubId);

    List<Event> findByStartDateTimeBetween(LocalDateTime from, LocalDateTime to);

    /**
     * Loads the event and locks its row until the end of the transaction.
     * Used when registering, so two students cannot take the last seat at the same time.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.id = :id")
    Optional<Event> findByIdForUpdate(@Param("id") Long id);
}
