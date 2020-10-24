package es.unizar.sisinf.grp1.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.unizar.sisinf.grp1.db.PoolConnectionManager;

public class PositivoFacade {
	
	private static String insertPositivo = "INSERT INTO web_data.acudir(fecha, correo_electronico) " + 
			"VALUES (?, ?, ?, ?, ?);";
	
	/** * Busca un registro en la tabla DEMO por ID * 
		@param id Identificador del registro buscado * 
		@returnObjeto DemoVO con el identificador buscado, o null si no seencuentra 
	*/
	public boolean insertLugar(PositivoVO positivo) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertP = conn.prepareStatement(insertPositivo);
			insertP.setDate(1, positivo.getFecha());
			insertP.setString(2, positivo.getCorreo_electronico());
			
			// Ejecutamos la orden
			insertP.execute();
		
			
			// liberamos los recursos utilizados
			insertP.close();
			

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

