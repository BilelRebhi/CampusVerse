# Smart City — Web Platform for Urban Management and Municipal Services

AWD mini-project (Distributed Web Architecture) — ESPRIT, academic year 2026/2027.

Smart City gives residents one website to report street problems, follow municipal requests,
book public facilities, check traffic and environmental conditions, take part in local
consultations and recover lost items. Municipal staff use it to review reports, assign repairs
and follow every request until a decision is made.

The platform is built as **microservices**: each service owns its business capability and its
own PostgreSQL database (database-per-service).

## Actors

| Actor | Main role |
|---|---|
| Citizen | Manages a profile, reports incidents, books parking and facilities, follows requests, votes and comments |
| Municipal administrator | Configures services and rules, assigns resources, reviews dashboards |
| Municipal agent | Validates reports and administrative requests, sets priorities, verifies repairs |
| Technician | Carries out field repairs and updates intervention progress |
| Mobility operator | Manages road closures and parking availability |
| Environmental operator | Configures stations and thresholds, reviews readings and alerts |
| Facility manager / organizer | Manages facilities, availability, bookings and community activities |
| Lost-and-found agent | Verifies ownership claims and coordinates returns |
| System administrator | Manages infrastructure, accounts and roles |

## Microservices

| # | Microservice | Folder | Tech | Database | Owner |
|---|---|---|---|---|---|
| MS1 | Citizen & Civic Participation | `Backend/citizen-participation-service` | Spring Boot | PostgreSQL | TBD |
| MS2 | Urban Incident & Maintenance | `Backend/urban-incident-service` | Spring Boot | PostgreSQL | Sarra Guesmi |
| MS3 | Smart Mobility & Traffic | `Backend/smart-mobility-service` | Spring Boot | PostgreSQL | TBD |
| MS4 | [EcoCity & Environmental Monitoring](Documentation/MS4-EcoCity-Service.md) | `Backend/ecocity-service` | Spring Boot | PostgreSQL + PostGIS | Isra Zguir |
| MS5 | Digital Municipal Services | `Backend/new-backend` | Spring Boot | PostgreSQL | Bilel Rebhi |
| MS6 | Smart Places & Public Facilities | `Backend/smart-places-service` | Spring Boot | PostgreSQL | TBD |
| MS7 | ReLink AI — Intelligent Lost & Found | `Backend/ai-service` | Python + FastAPI | PostgreSQL | Team |

Some services are still on their own branch and will appear in `main` after their pull request is merged.

## Architecture

- **Frontend:** one Angular web application (`Frontend/campusverse-web`).
- **API Gateway:** single entry point for the frontend, routing to the backend services.
- **Eureka Discovery Server:** service registration and discovery.
- **Config Server:** centralized configuration.
- **Keycloak:** authentication and role-based access (JWT).
- **Synchronous communication:** REST / OpenFeign.
- **Asynchronous communication:** RabbitMQ events.
- **Databases:** one PostgreSQL database per microservice; no service reads another service's tables.
- **Docker:** containerization of all components.

## Repository structure

```
Backend/                    one project per microservice
Frontend/campusverse-web/   Angular website; each microservice has its pages in src/app/features/<name>/
Documentation/Presentation/ validation slides
Documentation/Diagrams/     global architecture, use case and class diagrams
README.md
```

## Team

| Member | Microservice |
|---|---|
| Isra Zguir | MS4 — EcoCity & Environmental Monitoring |
| Sarra Guesmi | MS2 — Urban Incident & Maintenance |
| Bilel Rebhi | MS5 — Digital Municipal Services |
| Nour Ammar | TBD |
| Eya Hwess | TBD |
| Eya Abdellaoui | TBD |

## How to contribute

1. Clone: `git clone https://github.com/Isra-zguir/CampusVerse.git`
2. Create your own branch: `git checkout -b feature/<your-service>`
3. Work only inside **your** folders:
   - backend: `Backend/<your-service>/`
   - frontend: `Frontend/campusverse-web/src/app/features/<your-feature>/`
   - documentation: `Documentation/` (your service file and diagrams)
4. Before pushing: `git pull origin main`
5. Push your branch and open a **Pull Request** to `main`.
