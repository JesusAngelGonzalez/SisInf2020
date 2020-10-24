package es.unizar.sisinf.grp1.model;

import java.sql.Date;

/**
 * tabla users
 * @author sisinf
 *
 */
public class PositivoVO {
	private Date fecha;
	private String correo_electronico;
	
	/**
	 * Constructor
	 * @param id
	 * @param nombre
	 * @param ubicacion
	 */
	public PositivoVO(String correo_electronico, Date fecha) {
		this.correo_electronico = correo_electronico;
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setInicio(Date fecha) {
		this.fecha = fecha;
	}

	public String getCorreo_electronico() {
		return correo_electronico;
	}

	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}
	
}
