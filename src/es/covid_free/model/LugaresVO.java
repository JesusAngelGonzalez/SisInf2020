package es.covid_free.model;

/**
 * tabla users
 * @author sisinf
 *
 */
public class LugaresVO {
	private String nombre;
	private String ubicacion;
	private Integer id;
	
	/**
	 * Constructor
	 * @param id
	 * @param nombre
	 * @param ubicacion
	 */
	public LugaresVO(Integer id, String nombre, String ubicacion) {
		this.id = id;
		this.nombre = nombre;
		this.ubicacion = ubicacion;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}
