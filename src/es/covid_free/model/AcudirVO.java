package es.covid_free.model;

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
	private Integer id_ubicacion;
	
	/**
	 * Constructor
	 * @param Integer id
	 * @param String correo_electronico
	 * @param Timestamp inicio
	 * @param Timestamp fin
	 * @param Integer id_ubicacion
	 */
	public AcudirVO(String correo_electronico, Timestamp inicio, Timestamp fin, Integer id_ubicacion) {
		this.correo_electronico = correo_electronico;
		this.id_ubicacion = id_ubicacion;
		this.inicio = inicio;
		this.fin = fin;
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
