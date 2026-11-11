# CampusVerse — Smart University Ecosystem

An all-in-one platform for university life: room booking, student clubs, campus events,
equipment borrowing, maintenance requests, and lost-and-found.

**Special feature:** smart room allocation based on availability, capacity, and scheduling conflicts.

## Microservices

| #   | Microservice      | Folder                               | Tech                         | Database   | Owner |
|-----|-------------------|--------------------------------------|------------------------------|------------|-------|
| MS1 | Campus Spaces     | `Backend/campus-spaces-service`      | Spring Boot                  | PostgreSQL | TBD   |
| MS2 | Reservations      | `Backend/reservation-service`        | Spring Boot                  | PostgreSQL | TBD   |
| MS3 | Student Clubs     | `Backend/student-clubs-service`      | Spring Boot                  | PostgreSQL | TBD   |
| MS4 | Events            | `Backend/event-service`              | Spring Boot                  | PostgreSQL | isra  |
| MS5 | Equipment Lending | `Backend/equipment-lending-service`  | Spring Boot                  | PostgreSQL | TBD   |
| MS6 | Lost & Found      | `Backend/lost-found-service`         | Spring Boot                  | PostgreSQL | TBD   |
| MS7 | AI Brain (shared) | `Backend/ai-service`                 | Python + FastAPI             | PostgreSQL | Team  |

Each microservice owns its own PostgreSQL database (database-per-service).

## Repository structure

```
Backend/                    one project per microservice
Frontend/                   web client
Documentation/Presentation/ validation slides
Documentation/Diagrams/     architecture, ER / UML class diagrams
README.md
```

## Team

| Member | Microservice |
|--------|--------------|
| isra   | MS4 — Events |
|        |              |
|        |              |
|        |              |
|        |              |
|        |              |

## How to contribute

1. Clone: `git clone <repo-url>` and open the `CampusVerse` folder in IntelliJ.
2. Work only inside **your** microservice folder to avoid conflicts.
3. Before starting: `git pull`
4. When done: `git add .` → `git commit -m "MS4: short description"` → `git pull --rebase` → `git push`
