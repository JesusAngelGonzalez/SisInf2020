package es.covid_free.model;

import java.sql.Timestamp;

/**
 * join de la tabla lugar y acudir (excluyendo ambas id)
 * @author covid_free
 *
 */
public class AcudirLugares {
	private Timestamp inicio;
	private Timestamp fin;
	private String nombre; 
	private String ubicacion;
	
	/**
	 * Constructor
	 * @param inicio
	 * @param fin
	 * @param nombre
	 * @param ubicacion
	 */
	public AcudirLugares(Timestamp inicio, Timestamp fin, String nombre, String ubicacion) {
		this.ubicacion = ubicacion;
		this.nombre = nombre;
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


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getUbicacion() {
		return ubicacion;
	}


	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	
}

