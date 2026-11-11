package com.campusverse.event.controller;

import com.campusverse.event.dto.RegistrationRequest;
import com.campusverse.event.dto.RegistrationResponse;
import com.campusverse.event.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /** POST /api/events/{id}/registrations  {"studentId": 12} -> 201 | 400 | 404 | 409 */
    @PostMapping("/{id}/registrations")
    public ResponseEntity<RegistrationResponse> register(@PathVariable Long id,
                                                         @Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.register(id, request.studentId()));
    }

    /** GET /api/events/{id}/registrations -> 200 | 404 */
    @GetMapping("/{id}/registrations")
    public List<RegistrationResponse> findByEvent(@PathVariable Long id) {
        return registrationService.findByEvent(id);
    }

    /** DELETE /api/events/{id}/registrations/{studentId} -> 204 | 404 | 409 */
    @DeleteMapping("/{id}/registrations/{studentId}")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @PathVariable Long studentId) {
        registrationService.cancelRegistration(id, studentId);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/events/{id}/registrations/{studentId}/attendance -> 200 | 404 | 409 */
    @PatchMapping("/{id}/registrations/{studentId}/attendance")
    public RegistrationResponse markAttendance(@PathVariable Long id, @PathVariable Long studentId) {
        return registrationService.markAttendance(id, studentId);
    }

    /** GET /api/events/students/{studentId}/registrations -> 200 */
    @GetMapping("/students/{studentId}/registrations")
    public List<RegistrationResponse> findByStudent(@PathVariable Long studentId) {
        return registrationService.findByStudent(studentId);
    }
}
