package com.campusverse.event.entity;

import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private EventStatus status = EventStatus.DRAFT;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    /** Last moment students can register. */
    private LocalDateTime registrationDeadline;

    @Column(nullable = false)
    private Integer maxCapacity;

    // ---- References to other microservices: IDs only, never foreign keys across services ----

    /** User ID of the organizer. */
    @Column(nullable = false)
    private Long organizerId;

    /** Club ID from the Student Clubs service (null if it is not a club event). */
    private Long clubId;

    /** Space (room) ID from the Campus Spaces service. */
    private Long spaceId;

    /** Reservation ID from the Reservations service, once the room is booked. */
    private Long reservationId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventRegistration> registrations = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
