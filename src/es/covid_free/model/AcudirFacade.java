package es.covid_free.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.covid_free.db.PoolConnectionManager;

/**
 * acudir facade
 * @author covid_free
 *
 */

public class AcudirFacade {
	// Query para insertar una fila en acudir
	private static String insertAcudir = "INSERT INTO web_data.acudir(id_ubicacion, correo_electronico, inicio, final) " + 
			"VALUES (?, ?, ?, ?);";
	// Query para buscar los lugares más recientes en los que ha estado un usuario
	private static String queryUltimosLugares = "SELECT l.id, l.nombre, l.ubicacion, a.inicio, a.final \n" + 
			"FROM web_data.acudir a, web_data.lugares l \n" + 
			"WHERE a.correo_electronico = ? and l.id = a.id_ubicacion \n" + 
			"ORDER BY final DESC \n" + 
			"LIMIT 10;";
	
	private static String queryLugarContacto = "SELECT a3.final, a3.inicio, l.nombre, l.ubicacion \n" + 
			"FROM web_data.acudir a3, web_data.acudir a4, web_data.lugares l \n" + 
			"WHERE a3.id_ubicacion = a4.id_ubicacion AND  a3.correo_electronico = ?	AND l.id = a3.id_ubicacion \n" + 
			"      AND  a3.correo_electronico != a4.correo_electronico AND ? = a4.correo_electronico \n" + 
			"      AND ((a4.inicio between (?::timestamp - '3 days'::interval) and (?)) OR \n" + 
			"			(a4.final between (?::timestamp - '3 days'::interval) and (?))) \n" + 
			"		AND (((a4.inicio between a3.inicio AND a3.final) OR (a4.final between a3.inicio AND a3.final)) or \n" + 
			"			((a3.inicio between a4.inicio AND a4.final) OR (a3.final between a4.inicio AND a4.final)));";
	
	/** Inserta una fila en la tabla acudir 
		@param objeto de la tabla acudir 
		@returnBoolean devuelve verdad si inserta y falso en caso contrario 
	*/
	public boolean insertAcudir(AcudirVO acudir) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertA = conn.prepareStatement(insertAcudir);
			insertA.setInt(1, acudir.getId_ubicacion());
			insertA.setString(2, acudir.getCorreo_electronico());
			insertA.setTimestamp(3, acudir.getInicio());
			insertA.setTimestamp(4, acudir.getFin());
			
			// Ejecutamos la orden
			insertA.execute();
			
			// liberamos los recursos utilizados
			insertA.close();
			

		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
			
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
		
		return true;
	}
	
	/** Busca en la base de datos los últimos lugares en los que ha estado un usuario
		@param objeto de la tabla usuarios
		@returnList devuelve la lista con los últimos lugares en los que ha estado un usuario
		 			y la fecha de inicio y fin de su estancia en dicho lugar
	 */
	public List<AcudirLugares> getUltimosLugares(UsuariosVO usuario) {
		Connection conn = null;
		List<AcudirLugares> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(queryUltimosLugares);
			ps.setString(1, usuario.getCorreo_electronico());
			
			ResultSet rset = ps.executeQuery();
			// Añadimos todas las filas encontradas a la lista que se devuelve
			while(rset.next()) {
				lista.add(new AcudirLugares(rset.getTimestamp("inicio"), rset.getTimestamp("final"), 
											rset.getString("nombre"),  rset.getString("ubicacion")));
			}
			// Liberamos recursos
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return lista;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	/** Busca en la base de datos los lugares de contacto (puede ser un solo lugar) de un usuario con un positivo
	@param objeto de la tabla usuarios
	@param objeto de la tabla positivo
	@returnList devuelve la lista con los lugares de contacto de un usuario con un positivo
 */
	public List<AcudirLugares> getLugarContacto(UsuariosVO usuario, PositivoVO positivo) {
		Connection conn = null;
		List<AcudirLugares> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(queryLugarContacto);
			ps.setString(1, usuario.getCorreo_electronico());
			ps.setString(2, positivo.getCorreo_electronico());
			ps.setTimestamp(3, positivo.getFecha());
			ps.setTimestamp(4, positivo.getFecha());
			ps.setTimestamp(5, positivo.getFecha());
			ps.setTimestamp(6, positivo.getFecha());
			
			ResultSet rset = ps.executeQuery();
			// Añadimos todas las filas encontradas a la lista que se devuelve
			while(rset.next()) {
				lista.add(new AcudirLugares(rset.getTimestamp("inicio"), rset.getTimestamp("final"), 
											rset.getString("nombre"),  rset.getString("ubicacion")));
			}
			// Liberamos recursos
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return lista;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
}

