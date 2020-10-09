create database covid_free;
\connect covid_free;
CREATE SCHEMA web_data;
--CREATE EXTENSION IF NOT EXISTS timescaledb WITH SCHEMA web_data;
--CREATE EXTENSION IF NOT EXISTS citex WITH SCHEMA web_data;

CREATE TABLE web_data.usuarios (
	correo_electronico text NOT NULL,
	contrasenya text NOT NULL,
	numero_telefono integer NOT NULL,
	nombre text NOT NULL,
	CONSTRAINT usuarios_pk PRIMARY KEY (correo_electronico)
);
