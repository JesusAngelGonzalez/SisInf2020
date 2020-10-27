package es.unizar.sisinf.grp1.model;


/**
 * tabla users
 * @author sisinf
 *
 */
public class UsuariosVO {
	private String nombre;
	private String contrasenya;
	private String correo_electronico;
	private Integer numero_telefono; 
	
	/**
	 * Constructor
	 * @param nombre
	 * @param contrasenya
	 * @param correo_electronico
	 * @param numero_telefono
	 */
	public UsuariosVO(String correo_electronico, String contrasenya, Integer numero_telefono, String nombre) {
		this.nombre = nombre;
		this.contrasenya = contrasenya;
		this.correo_electronico = correo_electronico;
		this.numero_telefono = numero_telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public String getCorreo_electronico() {
		return correo_electronico;
	}

	public void setCorreo_electronico(String correo_electronico) {
		this.correo_electronico = correo_electronico;
	}
	
	public Integer getNumero_telefono() {
		return numero_telefono;
	}

	public void setNumero_telefono(Integer numero_telefono) {
		this.numero_telefono = numero_telefono;
	}

	
}
