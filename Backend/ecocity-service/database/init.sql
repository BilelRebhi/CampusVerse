-- Run once in pgAdmin as a PostgreSQL administrator.
CREATE USER ecocity_user WITH PASSWORD 'ecocity_pass';
CREATE DATABASE ecocity_db OWNER ecocity_user;

\connect ecocity_db
CREATE EXTENSION IF NOT EXISTS postgis;
GRANT ALL ON SCHEMA public TO ecocity_user;
