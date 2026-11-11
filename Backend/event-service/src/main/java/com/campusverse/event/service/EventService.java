package com.campusverse.event.service;

import com.campusverse.event.dto.EventRequest;
import com.campusverse.event.dto.EventResponse;
import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventResponse> findAll(EventCategory category, EventStatus status, Long clubId,
                                LocalDateTime from, LocalDateTime to);

    EventResponse findById(Long id);

    EventResponse create(EventRequest request);

    EventResponse update(Long id, EventRequest request);

    void delete(Long id);

    EventResponse publish(Long id);

    EventResponse cancel(Long id);
}
