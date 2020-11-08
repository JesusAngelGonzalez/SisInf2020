package es.covid_free.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.covid_free.db.PoolConnectionManager;

public class AcudirFacade {
	
	private static String insertAcudir = "INSERT INTO web_data.acudir(id, id_ubicacion, correo_electronico, inicio, final) " + 
			"VALUES (?, ?, ?, ?, ?);";
	private static String queryUltimosLugares = "SELECT l.id, l.nombre, l.ubicacion, a.inicio, a.final \n" + 
			"FROM web_data.acudir a, web_data.lugares l \n" + 
			"WHERE a.correo_electronico = ? and l.id = a.id_ubicacion \n" + 
			"ORDER BY final \n" + 
			"LIMIT 10;";
	
	/** * Busca un registro en la tabla DEMO por ID * 
		@param id Identificador del registro buscado * 
		@returnObjeto DemoVO con el identificador buscado, o null si no se encuentra 
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
			insertA.setTimestamp(4, acudir.getInicio());
			insertA.setTimestamp(5, acudir.getFin());
			
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
		
	public List<LugaresVO> getUltimosLugares(UsuariosVO usuario) {
		Connection conn = null;
		List<LugaresVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(queryUltimosLugares);
			ps.setString(1, usuario.getCorreo_electronico());
			
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				lista.add(new LugaresVO(rset.getInt("id"), rset.getString("nombre"),  rset.getString("ubicacion")));
				//debería devolver también la fecha de inicio y de fin (el statement ya esta preparado para ello)
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

