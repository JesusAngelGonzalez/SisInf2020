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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
}

