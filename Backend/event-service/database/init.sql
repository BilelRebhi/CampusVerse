-- Run once in pgAdmin (or psql) as the postgres user to create the Event service database.
-- The tables (events, event_registrations) are then created automatically by Hibernate
-- (spring.jpa.hibernate.ddl-auto=update) the first time the service starts.
CREATE DATABASE events_db;
