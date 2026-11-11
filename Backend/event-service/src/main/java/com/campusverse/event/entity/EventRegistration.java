package com.campusverse.event.entity;

import com.campusverse.event.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_registrations",
        // Business rule: a student cannot register twice for the same event
        uniqueConstraints = @UniqueConstraint(
                name = "uk_registration_event_student",
                columnNames = {"event_id", "student_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    /** Student user ID (reference to another service, no foreign key). */
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RegistrationStatus status = RegistrationStatus.CONFIRMED;

    @Column(nullable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}
