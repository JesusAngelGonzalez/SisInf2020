package es.covid_free.model;

public class LugarRanking {
	String nombre, direccion;
	int casos, visitantes, posicion;
	
	
	/**
	 * Construcctor del lugar para la tabla ranking 
	 * 
	 * @param nombre Nombre del lugar
	 * @param direccion Dirección del lugar
	 * @param casos Numero de casos del lugar
	 * @param visitantes Numero de visitantes del lugar
	 * @param posicion Posicion en el ranking del lugar
	 */
	public LugarRanking(String nombre, String direccion, int casos, int visitantes, int posicion) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
		this.casos = casos;
		this.visitantes = visitantes;
		this.posicion = posicion;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getCasos() {
		return casos;
	}
	public void setCasos(int casos) {
		this.casos = casos;
	}
	public int getVisitantes() {
		return visitantes;
	}
	public void setVisitantes(int visitantes) {
		this.visitantes = visitantes;
	}
	public int getPosicion() {
		return posicion;
	}
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
}
