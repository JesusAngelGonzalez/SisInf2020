package es.unizar.sisinf.grp1.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.unizar.sisinf.grp1.db.PoolConnectionManager;

public class LugaresFacade {
	
	private static String insertLugar = "INSERT INTO web_data.lugares(id, nombre, ubicacion) " + 
			"VALUES (?, ?, ?);";
	
	/** * Busca un registro en la tabla DEMO por ID * 
		@param id Identificador del registro buscado * 
		@returnObjeto DemoVO con el identificador buscado, o null si no seencuentra 
	*/
	public boolean insertLugar(LugaresVO lugar) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertL = conn.prepareStatement(insertLugar);
			insertL.setInt(1, lugar.getId());
			insertL.setString(2, lugar.getNombre());
			insertL.setString(3, lugar.getUbicacion());
			
			// Ejecutamos la orden
			insertL.execute();
		
			
			// liberamos los recursos utilizados
			insertL.close();
			

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
		
	
	
}

