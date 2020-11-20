create database covid_free;
\connect covid_free;
CREATE SCHEMA web_data;

CREATE TABLE web_data.usuarios (
	correo_electronico text NOT NULL,
	contrasenya text NOT NULL,
	numero_telefono integer NOT NULL,
	nombre text NOT NULL,
	CONSTRAINT usuarios_pk PRIMARY KEY (correo_electronico)
);

CREATE TABLE web_data.lugares (
	id SERIAL NOT NULL,
	nombre text NOT NULL,
	ubicacion text NOT NULL,
	CONSTRAINT lugares_pk PRIMARY KEY (id)
);

CREATE TABLE web_data.positivos (
	correo_electronico	text NOT NULL,
	fecha			timestamp NOT NULL,
	CONSTRAINT positivos_pk PRIMARY KEY (correo_electronico, fecha),
	CONSTRAINT correo_positivos_fk	FOREIGN KEY (correo_electronico) REFERENCES  web_data.usuarios(correo_electronico) ON DELETE CASCADE 
);

CREATE TABLE web_data.acudir (
	id			SERIAL NOT NULL,
	correo_electronico	text 	NOT NULL,
	id_ubicacion		integer 	NOT NULL,
	inicio		timestamp	NOT NULL,
	final		timestamp	NOT NULL,
	CONSTRAINT acurdir_pk PRIMARY KEY (id),
	CONSTRAINT correo_acudir_fk	FOREIGN KEY (correo_electronico) REFERENCES  web_data.usuarios(correo_electronico) ON DELETE CASCADE,
	CONSTRAINT acudir_fk	FOREIGN KEY (id_ubicacion) REFERENCES  web_data.lugares(id) ON DELETE CASCADE,
	CONSTRAINT fechas_ok	CHECK (inicio < final)
);
