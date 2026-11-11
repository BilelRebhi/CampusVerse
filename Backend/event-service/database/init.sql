-- Run once in pgAdmin (Query Tool, connected as postgres) to set up the Event service database.
-- The service has its own database AND its own database user (database-per-service).
-- The tables (events, event_registrations) are then created automatically by Hibernate
-- (spring.jpa.hibernate.ddl-auto=update) the first time the service starts.
CREATE USER events_user WITH PASSWORD 'events_pass';
CREATE DATABASE events_db OWNER events_user;
