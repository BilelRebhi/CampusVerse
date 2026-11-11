package com.campusverse.event.controller;

import com.campusverse.event.dto.EventRequest;
import com.campusverse.event.dto.EventResponse;
import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;
import com.campusverse.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /** GET /api/events?category=WORKSHOP&status=PUBLISHED&clubId=7&from=...&to=... -> 200 */
    @GetMapping
    public List<EventResponse> findAll(
            @RequestParam(required = false) EventCategory category,
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) Long clubId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return eventService.findAll(category, status, clubId, from, to);
    }

    /** GET /api/events/{id} -> 200 | 404 */
    @GetMapping("/{id}")
    public EventResponse findById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    /** POST /api/events -> 201 | 400 | 403 | 409 */
    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        EventResponse created = eventService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** PUT /api/events/{id} -> 200 | 400 | 404 | 409 */
    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventRequest request) {
        return eventService.update(id, request);
    }

    /** DELETE /api/events/{id} -> 204 | 404 | 409 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/events/{id}/publish -> 200 | 404 | 409 | 503 */
    @PatchMapping("/{id}/publish")
    public EventResponse publish(@PathVariable Long id) {
        return eventService.publish(id);
    }

    /** PATCH /api/events/{id}/cancel -> 200 | 404 | 409 */
    @PatchMapping("/{id}/cancel")
    public EventResponse cancel(@PathVariable Long id) {
        return eventService.cancel(id);
    }
}
