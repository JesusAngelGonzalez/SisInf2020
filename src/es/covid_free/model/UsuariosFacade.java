package es.covid_free.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.covid_free.db.PoolConnectionManager;

/* Crear Usuario; Validar Usuario; Actualizar Usuario; Añadir Lugar; Registrar Acudir; Añadir Positivo; Dejar de ser positivo */

public class UsuariosFacade {
	
	private static String countBycorreo_electronico = "SELECT count(*) cuenta FROM web_data.usuarios WHERE correo_electronico = ?";
	private static String findBycorreo_electronico = "SELECT * FROM web_data.usuarios WHERE correo_electronico = ?";
	private static String insertUser = "INSERT INTO web_data.usuarios(correo_electronico, contrasenya, numero_telefono, nombre) " + 
			"VALUES (?, ?, ?,?);";
	private static String updateUser = "UPDATE web_data.usuarios SET contrasenya = ?, numero_telefono = ?, nombre = ? "
			+ "WHERE correo_electronico = ?;";
	
	/** * Busca un registro en la tabla DEMO por ID * 
		@param id Identificador del registro buscado * 
		@returnObjeto DemoVO con el identificador buscado, o null si no seencuentra 
	*/
	public boolean validateUser(UsuariosVO user) { 
		boolean result = false;
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement countPs = conn.prepareStatement(countBycorreo_electronico);
			PreparedStatement findPs = conn.prepareStatement(findBycorreo_electronico);
			//PreparedStatement updatePs = conn.prepareStatement(updateDate);
			countPs.setString(1, user.getCorreo_electronico());
			findPs.setString(1, user.getCorreo_electronico());
			
			// Ejecutamos la consulta 
			ResultSet findRs = findPs.executeQuery();
			ResultSet countRs = countPs.executeQuery();
			
			countRs.next();
			int n = countRs.getInt(1);
			System.out.println("Número de registros: " + n);
			
			
			// Leemos resultados 
			if(n == 1) {
				// Comparamos contraseñas
				findRs.next();
				String dbpwd = findRs.getString("contrasenya");
				if (dbpwd.equals(user.getContrasenya())) {
					result = true;
				}
			} else { 
				result = false;  
			} 
			
			// liberamos los recursos utilizados
			findRs.close();
			findPs.close();
			countRs.close();
			countPs.close();

		} catch(SQLException se) {
			se.printStackTrace();  
		
		} catch(Exception e) {
			e.printStackTrace(System.err); 
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
		
		return result;
	}
	
	public int insertUser(UsuariosVO user) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement insertU = conn.prepareStatement(insertUser);
			insertU.setString(1, user.getCorreo_electronico());
			insertU.setString(2, user.getContrasenya());
			insertU.setInt(3, user.getNumero_telefono());
			insertU.setString(4, user.getNombre());
			
			// Ejecutamos la orden
			insertU.execute();
		
			
			// liberamos los recursos utilizados
			insertU.close();
			

		} catch(SQLException se) {
			final String ss = se.getSQLState();
			if(ss.equals("23505")) { // el correo ya está cogido
				return 1;
			}
			System.err.println(ss);
			se.printStackTrace();
			return -1;
		
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return -1;
			
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
		
		return 0;
	}
	
	public boolean updateUser(UsuariosVO user) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement updateU = conn.prepareStatement(updateUser);
			updateU.setString(1, user.getContrasenya());
			updateU.setInt(2, user.getNumero_telefono());
			updateU.setString(3, user.getNombre());
			updateU.setString(4, user.getCorreo_electronico());
			
			// Ejecutamos la orden
			updateU.execute();
		
			
			// liberamos los recursos utilizados
			updateU.close();
			

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
	
	
		
	
	/*public UserVO getUser(String nombre) {
		Connection conn = null;
		UserVO user = null;

		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement ps = conn.prepareStatement("Select * from users where nombre= ?");
			ps.setString(1, nombre);
			ResultSet rset = ps.executeQuery();
			rset.next();
			user = new UserVO(rset.getString("nombre"), rset.getString("contrasenya"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return user;
	}*/
	
}

