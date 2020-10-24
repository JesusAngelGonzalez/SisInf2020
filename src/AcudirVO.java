package es.unizar.sisinf.grp1.model;

import java.sql.Date;

/**
 * tabla users
 * @author sisinf
 *
 */
public class AcudirVO {
	private Date inicio;
	private Date fin;
	private String correo_electronico;
	private Integer id;
	private Integer id_ubicacion;
	
	/**
	 * Constructor
	 * @param id
	 * @param nombre
	 * @param ubicacion
	 */
	public AcudirVO(Integer id, String correo_electronico, Date inicio, Date fin, Integer id_ubicacion) {
		this.id = id;
		this.correo_electronico = correo_electronico;
		this.id_ubicacion = id_ubicacion;
		this.inicio = inicio;
		this.fin = fin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public String getCorreo_electronico() {
		return correo_electronico;
	}

	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}

	public Integer getId_ubicacion() {
		return id_ubicacion;
	}

	public void setId_ubicacion(Integer id_ubicacion) {
		this.id_ubicacion = id_ubicacion;
	}

	
}
