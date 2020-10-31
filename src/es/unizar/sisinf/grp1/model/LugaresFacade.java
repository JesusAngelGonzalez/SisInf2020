package es.unizar.sisinf.grp1.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.unizar.sisinf.grp1.db.PoolConnectionManager;

public class LugaresFacade {
	
	private static String insertLugar = "INSERT INTO web_data.lugares(id, nombre, ubicacion) " + 
			"VALUES (?, ?, ?);";
	private static String rankingVisitas = "select l.id, l.nombre, l.ubicacion, count(*) n\n" + 
			"from web_data.acudir a, web_data.lugares l\n" + 
			"where a.id_ubicacion = l.id\n" + 
			"group by l.id \n" + 
			"order by n desc\n" + 
			"limit 100;";
	private static String rankingMasPositivos = "SELECT l.id, l.nombre, l.ubicacion, COUNT(*) n\n" + 
			"FROM web_data.acudir a, web_data.lugares l, web_data.positivos p\n" + 
			"WHERE a.correo_electronico = p.correo_electronico and l.id = a.id_ubicacion \n" + 
			"AND ((a.inicio between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)) OR\n" + 
			"	(a.final between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)))\n" + 
			"group by l.id\n" + 
			"ORDER BY  n desc \n" + 
			"LIMIT 100;";
	private static String rankingMenosPositivos = "SELECT l.id, l.nombre, l.ubicacion, COUNT(*) n\n" + 
			"FROM web_data.acudir a, web_data.lugares l, web_data.positivos p\n" + 
			"WHERE a.correo_electronico = p.correo_electronico and l.id = a.id_ubicacion \n" + 
			"AND ((a.inicio between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)) OR\n" + 
			"	(a.final between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)))\n" + 
			"group by l.id\n" + 
			"ORDER BY  n asc \n" + 
			"LIMIT 100;";
	private static String queryFreeCovid = "SELECT distinct l.id, l.nombre, l.ubicacion\n" + 
			"FROM web_data.lugares l\n" + 
			"WHERE  not exists\n" + 
			"		(select *\n" + 
			"		 from web_data.positivos p, web_data.acudir a\n" + 
			"		 where p.correo_electronico = a.correo_electronico and a.id_ubicacion = l.id  and\n" + 
			"		 		((a.inicio between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)) OR\n" + 
			"				(a.final between (p.fecha::timestamp - '3 days'::interval) and (p.fecha))))\n" + 
			"ORDER BY l.id\n" + 
			"LIMIT ?;";
	
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
	
	public List<LugaresVO> getRankingVisitas() {
		Connection conn = null;
		List<LugaresVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(rankingVisitas);
			
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				lista.add(new LugaresVO(rset.getInt("id"), rset.getString("nombre"),  rset.getString("ubicacion")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
		
	public List<LugaresVO> getRankingMasPositivos() {
		Connection conn = null;
		List<LugaresVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(rankingMasPositivos);
			int count = 0;
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				count ++;
				lista.add(new LugaresVO(rset.getInt("id"), rset.getString("nombre"),  rset.getString("ubicacion")));
			}
			if(count < 100) {
				PreparedStatement ps2 = conn.prepareStatement(queryFreeCovid);
				ps2.setInt(1, 100 - count);
				ResultSet rset2 = ps2.executeQuery();
				while(rset2.next()) {
					lista.add(new LugaresVO(rset2.getInt("id"), rset2.getString("nombre"),  rset2.getString("ubicacion")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	public List<LugaresVO> getRankingMenosPositivos() {
		Connection conn = null;
		List<LugaresVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(rankingMenosPositivos);
			int count = 0;
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				count ++;
				lista.add(new LugaresVO(rset.getInt("id"), rset.getString("nombre"),  rset.getString("ubicacion")));
			}
			if(count < 100) {
				PreparedStatement ps2 = conn.prepareStatement(queryFreeCovid);
				ps2.setInt(1, 100 - count);
				ResultSet rset2 = ps2.executeQuery();
				while(rset2.next()) {
					lista.add(new LugaresVO(rset2.getInt("id"), rset2.getString("nombre"),  rset2.getString("ubicacion")));
				}
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

