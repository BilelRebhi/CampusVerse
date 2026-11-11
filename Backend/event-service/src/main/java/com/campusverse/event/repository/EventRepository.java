package com.campusverse.event.repository;

import com.campusverse.event.entity.Event;
import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(EventStatus status);

    List<Event> findByCategory(EventCategory category);

    List<Event> findByClubId(Long clubId);

    List<Event> findByStartDateTimeBetween(LocalDateTime from, LocalDateTime to);
}
