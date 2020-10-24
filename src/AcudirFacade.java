package es.unizar.sisinf.grp1.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.unizar.sisinf.grp1.db.PoolConnectionManager;

public class AcudirFacade {
	
	private static String insertAcudir = "INSERT INTO web_data.acudir(id, id_ubicacion, correo_electronico, inicio, final) " + 
			"VALUES (?, ?, ?, ?, ?);";
	
	/** * Busca un registro en la tabla DEMO por ID * 
		@param id Identificador del registro buscado * 
		@returnObjeto DemoVO con el identificador buscado, o null si no seencuentra 
	*/
	public boolean insertLugar(AcudirVO acudir) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertA = conn.prepareStatement(insertAcudir);
			insertA.setInt(1, acudir.getId());
			insertA.setInt(2, acudir.getId_ubicacion());
			insertA.setString(3, acudir.getCorreo_electronico());
			insertA.setDate(4, acudir.getInicio());
			insertA.setDate(5, acudir.getFin());
			
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
		
	
	
}

