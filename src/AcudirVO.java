package es.unizar.sisinf.grp1.model;

import java.sql.Timestamp;

/**
 * tabla users
 * @author sisinf
 *
 */
public class AcudirVO {
	private Timestamp inicio;
	private Timestamp fin;
	private String correo_electronico;
	private Integer id;
	private Integer id_ubicacion;
	
	/**
	 * Constructor
	 * @param id
	 * @param nombre
	 * @param ubicacion
	 */
	public AcudirVO(Integer id, String correo_electronico, Timestamp inicio, Timestamp fin, Integer id_ubicacion) {
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

	public Timestamp getInicio() {
		return inicio;
	}

	public void setInicio(Timestamp inicio) {
		this.inicio = inicio;
	}

	public Timestamp getFin() {
		return fin;
	}

	public void setFin(Timestamp fin) {
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
