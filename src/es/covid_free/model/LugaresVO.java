package es.covid_free.model;

/**
 * tabla lugares
 * @author covid_free
 *
 */
public class LugaresVO {
	private String nombre;
	private String ubicacion;
	
	/**
	 * Constructor
	 * @param nombre
	 * @param ubicacion
	 */
	public LugaresVO(String nombre, String ubicacion) {
		this.nombre = nombre;
		this.ubicacion = ubicacion;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}
