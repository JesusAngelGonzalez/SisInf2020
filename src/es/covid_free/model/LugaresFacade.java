package es.covid_free.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.covid_free.db.PoolConnectionManager;

/**
 * lugares facade
 * @author covid_free
 *
 */

public class LugaresFacade {
	
	// Query para insertar un lugar en la BD
	private static String insertLugar = "INSERT INTO web_data.lugares(nombre, ubicacion) " + 
			"VALUES (?, ?);";
	
	// Query para ranking por número de visitas en orden descendiente
	private static String rankingVisitas = "select l.id, l.nombre, l.ubicacion, count(*) n\n" + 
			"from web_data.acudir a, web_data.lugares l\n" + 
			"where a.id_ubicacion = l.id\n" + 
			"group by l.id \n" + 
			"order by n desc\n" + 
			"limit 100;";
	
	// Query para buscar un lugar concreto en la BD
	private static String findByName = "SELECT * FROM web_data.lugares WHERE nombre = ? AND ubicacion = ?";
	
	// Query para ranking por número de casos de covid en orden descendiente
	private static String rankingMasPositivos = "SELECT l.id, l.nombre, l.ubicacion, COUNT(*) n\n" + 
			"FROM web_data.acudir a, web_data.lugares l, web_data.positivos p\n" + 
			"WHERE a.correo_electronico = p.correo_electronico and l.id = a.id_ubicacion \n" + 
			"AND ((a.inicio between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)) OR\n" + 
			"	(a.final between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)))\n" + 
			"group by l.id\n" + 
			"ORDER BY  n desc \n" + 
			"LIMIT 100;";
	
	// Query para ranking por número de casos de covid en orden ascendiente
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
	
	/**
	 * Inserta lugar en la BD
	 * @param lugar 
	 * @return id del lugar insertado o -1 si error
	 */
	public int insertLugar(LugaresVO lugar) { 
		Connection conn = null;
		int id = -1;
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertL = conn.prepareStatement(insertLugar, PreparedStatement.RETURN_GENERATED_KEYS);
			insertL.setString(1, lugar.getNombre());
			insertL.setString(2, lugar.getUbicacion());
			
			// Ejecutamos la orden
			insertL.execute();
			ResultSet rs = insertL.getGeneratedKeys();
			if(rs.next()) {
				id = rs.getInt("id");
			}
			System.out.println("Result id = " + id);
			
			// liberamos los recursos utilizados
			insertL.close();
			

		} catch(SQLException se) {
			se.printStackTrace();
			id = -1;
		
		} catch(Exception e) {
			e.printStackTrace(System.err);
			id = -1;
			
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
		
		return id;
	}
	
	/**
	 * Busca el top 100 de los lugares registrados en la BD con más visitas 
	 * @return lista con el top 100 de los lugares registrados en la BD con más visitas,
	 * 		   su posición en el ranking y el número de visitas
	 */
	public List<LugarRanking> getRankingVisitas() {
		Connection conn = null;
		List<LugarRanking> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(rankingVisitas);
			
			// Ejecutamos la orden
			ResultSet rset = ps.executeQuery();
			int count = 0;
			// Añadimos las filas encontradas a la lista
			while(rset.next()) {
				count ++;
				lista.add(new LugarRanking(rset.getString("nombre"), rset.getString("ubicacion"), 0, rset.getInt("n"), count));
			}
			
			// liberamos los recursos utilizados
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	
	/**
	 * Busca el top 100 de los lugares registrados en la BD con más positivos registrados 
	 * @return lista con el top 100 de los lugares registrados en la BD con más casos de positivos,
	 * 		   su posición en el ranking y el número de casos
	 */
	public List<LugarRanking> getRankingMasPositivos() {
		Connection conn = null;
		List<LugarRanking> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(rankingMasPositivos);
			int count = 0;
			// Ejecutamos la orden
			ResultSet rset = ps.executeQuery();
			// Añadimos primero los lugares con número de positivos mayor que 0 a la lista
			while(rset.next()) {
				count ++;
				lista.add(new LugarRanking(rset.getString("nombre"), rset.getString("ubicacion"), rset.getInt("n"), 0, count));
			}
			// Añadimos después los lugares sin positivos a la lista en caso de que la lista tenga menos de 100 lugares
			if(count < 100) {
				PreparedStatement ps2 = conn.prepareStatement(queryFreeCovid);
				ps2.setInt(1, 100 - count);
				ResultSet rset2 = ps2.executeQuery();
				while(rset2.next()) {
					count ++;
					lista.add(new LugarRanking(rset2.getString("nombre"), rset2.getString("ubicacion"), 0, 0, count));
				}
				// Liberamos recursos
				ps2.close();
			}
			ps.close();
	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	/**
	 * Busca el top 100 de los lugares registrados en la BD con menos positivos registrados 
	 * @return lista con el top 100 de los lugares registrados en la BD con menos casos de positivos,
	 * 		   su posición en el ranking y el número de casos
	 */
	public List<LugarRanking> getRankingMenosPositivos() {
		Connection conn = null;
		List<LugarRanking> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection();		
			PreparedStatement ps = conn.prepareStatement(queryFreeCovid);
			ps.setInt(1, 100);
			int count = 0;
			// Ejecutamos la orden
			ResultSet rset = ps.executeQuery();
			// Añadimos primero los lugares sin positivos a la lista en caso de que la lista tenga menos de 100 lugares
			while(rset.next()) {
				count ++;
				lista.add(new LugarRanking(rset.getString("nombre"), rset.getString("ubicacion"), 0, 0, count));
			}
			// Añadimos después los lugares con número de positivos mayor que 0 a la lista
			if(count < 100) {
				PreparedStatement ps2 = conn.prepareStatement(rankingMenosPositivos);
				ResultSet rset2 = ps2.executeQuery();
				while(rset2.next()) {
					count ++;
					lista.add(new LugarRanking(rset2.getString("nombre"), rset2.getString("ubicacion"), rset2.getInt("n"), 0, count));
				}
				//Liberamos recursos
				ps2.close();
			}
			ps.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	
	/**
	 * Busca la id de un lugar concreto
	 * @param  lugar
	 * @return id del lugar que se le pasa como parámtro y en caso de 
	 * 		   no existir devuelve -1
	 */
	public Integer comprobarLugar(LugaresVO lugar) {
		Connection conn = null;
		Integer idLugar = -1;
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			System.out.println(lugar.getNombre() + " Direccion: " + lugar.getUbicacion());
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement findPs = conn.prepareStatement(findByName);
			findPs.setString(1, lugar.getNombre());
			findPs.setString(2, lugar.getUbicacion());
			// Ejecutamos la orden
			ResultSet rset = findPs.executeQuery();
			if(rset.next()) {
				// Asignamos el valor de la id si dicho lugar existe
				idLugar = rset.getInt("id");
			}
			// liberamos recursos
			findPs.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return idLugar;
		
		
	}
	
}

