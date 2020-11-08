package es.covid_free.model;

import java.sql.Timestamp;

/**
 * tabla users
 * @author sisinf
 *
 */
public class PositivoVO {
	private Timestamp fecha;
	private String correo_electronico;
	
	/**
	 * Constructor
	 * @param id
	 * @param nombre
	 * @param ubicacion
	 */
	public PositivoVO(String correo_electronico, Timestamp fecha) {
		this.correo_electronico = correo_electronico;
		this.fecha = fecha;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setInicio(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getCorreo_electronico() {
		return correo_electronico;
	}

	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}
	
}
