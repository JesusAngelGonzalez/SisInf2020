package es.unizar.sisinf.grp1.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import es.unizar.sisinf.grp1.db.PoolConnectionManager;

public class PositivoFacade {
	
	private static String insertPositivo = "INSERT INTO web_data.acudir(fecha, correo_electronico) " + 
			"VALUES (?, ?);";
	private static String posiblesPositivos = "SELECT a3.correo_electronico, u.numero_telefono, u.contrasenya, u.nombre \n" + 
			"FROM web_data.acudir a3, web_data.acudir a4, web_data.usuarios u \n" + 
			"WHERE a3.id_ubicacion = a4.id_ubicacion AND  a3.correo_electronico = u.correo_electronico				\n" + 
			"               AND  a3.correo_electronico != a4.correo_electronico AND ? = a4.correo_electronico \n" + 
			"              AND ((a4.inicio between (?::timestamp - '3 days'::interval) and (?)) OR \n" + 
			"			 		(a4.final between (?::timestamp - '3 days'::interval) and (?))) \n" + 
			"			 	AND (((a4.inicio between a3.inicio AND a3.final) OR (a4.final between a3.inicio AND a3.final)) or \n" + 
			"			 		 ((a3.inicio between a4.inicio AND a4.final) OR (a3.final between a4.inicio AND a4.final)));";
	
	private static String posiblesPositivosN = "SELECT a3.correo_electronico, u.numero_telefono, u.contrasenya, u.nombre\n" + 
			"FROM web_data.acudir a3, web_data.acudir a4, web_data.usuarios u, web_data.positivos p \n" + 
			"WHERE a3.id_ubicacion = a4.id_ubicacion AND  a3.correo_electronico = u.correo_electronico				\n" + 
			"               AND  a3.correo_electronico != a4.correo_electronico AND p.correo_electronico = a4.correo_electronico\n" + 
			"               AND ((a4.inicio between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)) OR\n" + 
			"			 		(a4.final between (p.fecha::timestamp - '3 days'::interval) and (p.fecha)))\n" + 
			"			 	AND (((a4.inicio between a3.inicio AND a3.final) OR (a4.final between a3.inicio AND a3.final)) or \n" + 
			"			 		 ((a3.inicio between a4.inicio AND a4.final) OR (a3.final between a4.inicio AND a4.final)));";
	
	private static String getTimestamp = "SELECT * FROM web_data.positivos WHERE correo_electronico = ? ;";

			
/*
 * SELECT * 
	   FROM web_data.usuarios u
	   WHERE u.correo_electronico != ? 					#donde ? es el correo de la persona que ha dado positivo
			 AND EXISTS (SELECT * FROM
						 ((SELECT a.id_ubicacion 
						  FROM web_data.acudir a1
						  WHERE a.correo_electronico = u.correo_electronico AND				#selecciona las ubicaciones de una persona
						  		a.inicio <= fechadelpositivo and a.inicio >= fechadelpositivo.dia - 3) ub#en el rango de horario de posible contagio
						  INTERSECTS  #coinciden en espacio de contagio el positivo y la persona
						(SELECT a.id_ubicacion 
						  FROM web_data.acudir a2
						  WHERE a.correo_electronico = ? AND						#selecciona las ubicaciones de la persona que ha dado positivo
						  		a.inicio <= fechadelpositivo and a.inicio.dia >= fechadelpositivo.dia - 3)) ub, #en el rango de horario de posible contagio
						web_data.acudir a3, web_data.acudir a4 
						 WHERE ub.id_ubicacion = a3.id_ubicacion and ub.id_ubicacion = a3.id_ubicacion #y coinciden el positivo y el usuario en tiempo
								AND u.correo_electronico = a3.correo_electronico AND ? = a4.correo_electronico
								AND ((a3.inicio <= a4.final AND a3.inicio >= a4.inicio) OR (a3.final<= a4.final AND a3.final >= a4.inicio))
 */
	
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
			insertP.setTimestamp(1, positivo.getFecha());
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
	
	//El positivo ha tenido que ser registrado
	public List<UsuariosVO> getPosiblesPositivos(PositivoVO positivo) {
		Connection conn = null;
		List<UsuariosVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(posiblesPositivos);
			ps.setString(1, positivo.getCorreo_electronico());
			ps.setTimestamp(2, positivo.getFecha());
			ps.setTimestamp(3, positivo.getFecha());
			ps.setTimestamp(4, positivo.getFecha());
			ps.setTimestamp(5, positivo.getFecha());
			
			
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				//lista.add(new UsuariosVO(rset.getString("username"), rset.getString("password")); modificar cuando este hecho UsuariosVO
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	//Posibles positivos del total de posibles positivos de la BD
	public List<UsuariosVO> getPosiblesPositivos() {
		Connection conn = null;
		List<UsuariosVO> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(posiblesPositivosN);
			
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				//lista.add(new UsuariosVO(rset.getString("username"), rset.getString("password")); modificar cuando este hecho UsuariosVO
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return lista;
	}
	
	//Devuelve todas las fechas en las que una persona ha dado positivo
	public List<Timestamp> getDateAndTime(String correo) {
		Connection conn = null;
		List<Timestamp> lista = new ArrayList<>();

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement(getTimestamp);
			ps.setString(1, correo);
			
			ResultSet rset = ps.executeQuery();
			while(rset.next()) {
				lista.add(rset.getTimestamp("fecha")); 
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

