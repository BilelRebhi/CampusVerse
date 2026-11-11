package com.campusverse.event.service;

import com.campusverse.event.dto.RegistrationResponse;

import java.util.List;

public interface RegistrationService {

    RegistrationResponse register(Long eventId, Long studentId);

    void cancelRegistration(Long eventId, Long studentId);

    RegistrationResponse markAttendance(Long eventId, Long studentId);

    List<RegistrationResponse> findByEvent(Long eventId);

    List<RegistrationResponse> findByStudent(Long studentId);
}
