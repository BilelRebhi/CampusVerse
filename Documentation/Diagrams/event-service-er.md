# MS4 — Event Service: Entity Relationship Diagram

Database: **`events_db`** (PostgreSQL, owned only by the Event service)

```mermaid
erDiagram
    EVENTS ||--o{ EVENT_REGISTRATIONS : "has"

    EVENTS {
        bigint id PK
        varchar title "NOT NULL, max 150"
        varchar description "max 2000"
        varchar category "ACADEMIC | CULTURAL | SPORTS | CLUB | CONFERENCE | WORKSHOP | SOCIAL | OTHER"
        varchar status "DRAFT | PENDING_ROOM | PUBLISHED | CANCELLED | COMPLETED"
        timestamp start_date_time "NOT NULL"
        timestamp end_date_time "NOT NULL"
        timestamp registration_deadline
        int max_capacity "NOT NULL"
        bigint organizer_id "ref -> user (external)"
        bigint club_id "ref -> Student Clubs service (external)"
        bigint space_id "ref -> Campus Spaces service (external)"
        bigint reservation_id "ref -> Reservations service (external)"
        timestamp created_at
        timestamp updated_at
    }

    EVENT_REGISTRATIONS {
        bigint id PK
        bigint event_id FK "-> events.id"
        bigint student_id "ref -> user (external)"
        varchar status "CONFIRMED | WAITLISTED | CANCELLED | ATTENDED"
        timestamp registered_at
    }
```

**Constraint:** `UNIQUE (event_id, student_id)`, so a student cannot register twice for the same event.

**Relationship:** one `Event` has 0..* `EventRegistration`s. Deleting an event deletes its registrations (cascade).

**External references** (`organizer_id`, `club_id`, `space_id`, `reservation_id`, `student_id`) are plain IDs.
They are **not** foreign keys, because those records live in other microservices' databases.

## UML class diagram

```mermaid
classDiagram
    class Event {
        -Long id
        -String title
        -String description
        -EventCategory category
        -EventStatus status
        -LocalDateTime startDateTime
        -LocalDateTime endDateTime
        -LocalDateTime registrationDeadline
        -Integer maxCapacity
        -Long organizerId
        -Long clubId
        -Long spaceId
        -Long reservationId
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
    }
    class EventRegistration {
        -Long id
        -Long studentId
        -RegistrationStatus status
        -LocalDateTime registeredAt
    }
    class EventCategory {
        <<enumeration>>
        ACADEMIC
        CULTURAL
        SPORTS
        CLUB
        CONFERENCE
        WORKSHOP
        SOCIAL
        OTHER
    }
    class EventStatus {
        <<enumeration>>
        DRAFT
        PENDING_ROOM
        PUBLISHED
        CANCELLED
        COMPLETED
    }
    class RegistrationStatus {
        <<enumeration>>
        CONFIRMED
        WAITLISTED
        CANCELLED
        ATTENDED
    }
    Event "1" --> "0..*" EventRegistration : registrations
    Event --> EventCategory
    Event --> EventStatus
    EventRegistration --> RegistrationStatus
```
