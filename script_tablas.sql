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

CREATE TABLE web_data.lugares (
	nombre text NOT NULL,
	ubicacion text NOT NULL,
	CONSTRAINT lugares_pk PRIMARY KEY (nombre, ubicacion)
);

CREATE TABLE web_data.positivos (
	correo_electronico	text NOT NULL,
	fecha			timestamp NOT NULL,
	CONSTRAINT positivos_pk PRIMARY KEY (correo_electronico, fecha),
	CONSTRAINT correo_positivos_fk	FOREIGN KEY (correo_electronico) REFERENCES  web_data.usuarios(correo_electronico) ON DELETE CASCADE 
);

CREATE TABLE web_data.acudir (
	id			integer NOT NULL,
	correo_electronico	text 	NOT NULL,
	nombre_lugar		text 	NOT NULL,
	ubicacion		text 	NOT NULL,
	inicio		timestamp	NOT NULL,
	final		timestamp	NOT NULL,
	CONSTRAINT acurdir_pk PRIMARY KEY (id),
	CONSTRAINT correo_acudir_fk	FOREIGN KEY (correo_electronico) REFERENCES  web_data.usuarios(correo_electronico) ON DELETE CASCADE,
	CONSTRAINT lugar_acudir_fk	FOREIGN KEY (nombre_lugar, ubicacion) REFERENCES  web_data.lugares(nombre, ubicacion) ON DELETE CASCADE,
	CONSTRAINT fechas_ok	CHECK (inicio < final)
);
