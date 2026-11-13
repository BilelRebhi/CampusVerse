# MS4 — EcoCity & Environmental Monitoring

**Owner:** Houssem Hfaissi
**Backend folder:** `Backend/ecocity-service`
**Database:** `ecocity_db` (PostgreSQL + PostGIS)
**Status:** Structure and design only; service behavior is not implemented.

## Purpose

EcoCity monitors air quality and noise across the neighborhoods of the city. Virtual monitoring stations
record simulated readings, configured thresholds identify abnormal values, and pollution alerts
and environmental reports help users understand local conditions.

## Main concepts

- **Zone:** a city neighborhood being monitored; PostGIS can represent its geographic boundary.
- **MonitoringStation:** a virtual (simulated) station located in a zone.
- **Measurement:** an indicator reading recorded by a station.
- **Threshold:** a configured limit for an indicator.
- **PollutionAlert:** a notification associated with an out-of-range reading.
- **EnvReport:** a report comparing conditions across zones or time periods.

## Planned API

These are design examples from the MS4 slides, not implemented endpoints.

| Method | Endpoint | Purpose |
|---|---|---|
| GET, POST | `/api/stations` | List or register stations |
| POST | `/api/measurements` | Record a measurement |
| GET | `/api/stations/{id}/measurements` | View a station's measurements |
| PUT | `/api/thresholds/{id}` | Update a threshold |
| PUT | `/api/alerts/{id}/status` | Update an alert's status |
| GET | `/api/zones/compare` | Compare zones |
| POST | `/api/reports` | Request an environmental report |

## Planned monitoring workflow

Simulated readings are collected every five minutes. A reading over its configured
limit can create an alert with LOW, MEDIUM, or HIGH severity. The design allows one
open alert per station and indicator.

## Planned service communication

- **MS1:** synchronous neighborhood lookup for a citizen-facing query.
- **MS2:** asynchronous RabbitMQ notification for persistent HIGH alerts.
- **MS3:** asynchronous RabbitMQ notification for pollution peaks.

These integrations describe the slide design and are not implemented in this scaffold.

## Project structure

The backend folders are placeholders for future controllers, DTOs, entities,
repositories, services, configuration, and tests. The frontend EcoCity feature is
not implemented yet.

## Actors

| Actor | Role in MS4 |
|---|---|
| Municipality Responsible (Admin) | Creates virtual stations per zone, sets air-quality and noise thresholds |
| Municipality Agent | Watches dashboards, acknowledges / resolves alerts, generates reports |
| Citizen | Checks air quality and noise in his zone, compares neighborhoods, views history |

## Expected HTTP responses

`200 OK`, `201 Created`, `400 Bad Request` (invalid value / unit), `404 Not Found` (unknown station, alert or zone),
`409 Conflict` (station code already exists).

## Database choice

PostgreSQL (`ecocity_db`), owned only by MS4 (database-per-service): relational model, ACID integrity,
aggregations (AVG / MAX by zone) for dashboards and reports, and PostGIS for station locations and zone boundaries.
Other microservices never read this database directly; they use the MS4 REST API or RabbitMQ events.

## Class diagram

See `Diagrams/ms4-ecocity-class-diagram.png`.
