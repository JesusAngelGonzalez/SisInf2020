ALTER TABLE web_data.acudir DROP CONSTRAINT lugar_acudir_fk;
ALTER TABLE web_data.acudir DROP COLUMN nombre_lugar;
ALTER TABLE web_data.acudir DROP COLUMN ubicacion;
ALTER TABLE web_data.acudir ADD id_ubicacion numeric NOT NULL;
ALTER TABLE web_data.lugares DROP CONSTRAINT lugares_pk;
ALTER TABLE web_data.lugares ADD CONSTRAINT lugares_pk PRIMARY KEY (id);
ALTER TABLE web_data.acudir ADD CONSTRAINT acudir_fk FOREIGN KEY (id_ubicacion) REFERENCES web_data.lugares(id) ON DELETE CASCADE ON UPDATE CASCADE;

