package es.covid_free.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.covid_free.HASH.HashWrapper;
import es.covid_free.db.PoolConnectionManager;

/**
 * usuarios facade
 * @author covid_free
 *
 */

public class UsuariosFacade {
	
	// Query para contar el número de usuarios con un mismo correo 
	private static String countBycorreo_electronico = "SELECT count(*) cuenta FROM web_data.usuarios WHERE correo_electronico = ?";
	
	// Query para obtener la información de un usuario
	private static String findBycorreo_electronico = "SELECT * FROM web_data.usuarios WHERE correo_electronico = ?";
	
	// Query para insertar un usuario en la BD
	private static String insertUser = "INSERT INTO web_data.usuarios(correo_electronico, contrasenya, numero_telefono, nombre) " + 
			"VALUES (?, ?, ?,?);";
	
	// Query para actualizar los datos de un usuario en la BD
	private static String updateUser = "UPDATE web_data.usuarios SET  numero_telefono = ?, nombre = ? "
			+ "WHERE correo_electronico = ?;";
	
	// Query para borrar los datos de un usuario de la BD
	private static String deleteUser = "DELETE FROM web_data.usuarios WHERE correo_electronico = ? ";
	
	
	/** Comprueba si la contraseña introducida por el usuario es la misma que la que está en la BD 
	 *	@param objeto de la tabla usuarios 
	 *	@returnBoolean devuelve verdad en caso coincidir las contraseñas y falso si son distintas
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
			
			// Leemos resultados y nos aseguramos de que solo haya una fila
			if(n == 1) {
				// Comparamos contraseñas
				findRs.next();
				HashWrapper hw = new HashWrapper();
				String dbpwd = findRs.getString("contrasenya");
				if (hw.authenticate(user.getContrasenya().toCharArray(), dbpwd)) {
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
	
	/** Inserta una fila en la tabla usuarios
	* @param objeto de la tabla usuarios
	* @returnInt devuelve -1 en caso de eror, 0 si inserta y 1 si el correo ya está en la BD
	*/
	public int insertUser(UsuariosVO user) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection();
			HashWrapper hw = new HashWrapper();
			PreparedStatement insertU = conn.prepareStatement(insertUser);
			insertU.setString(1, user.getCorreo_electronico());
			insertU.setString(2, hw.hash(user.getContrasenya().toCharArray()));
			insertU.setInt(3, user.getNumero_telefono());
			insertU.setString(4, user.getNombre());
			
			// Ejecutamos la orden
			insertU.execute();
		
			
			// liberamos los recursos utilizados
			insertU.close();
			

		} catch(SQLException se) {
			final String ss = se.getSQLState();
			if(ss.equals("23505")) { // fallo por restricción de claves primarias distintas
				// El correo ya está en la BD
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
	
	/** Actualiza un usuario de la tabla usuarios
	* @param objeto de la tabla usuarios con los datos actualizados
	* @returnBoolean devuelve verdad en caso de éxito y false en caso de fallo
	*/
	public boolean updateUser(UsuariosVO user) { 
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement updateU = conn.prepareStatement(updateUser);
			updateU.setInt(1, user.getNumero_telefono());
			updateU.setString(2, user.getNombre());
			updateU.setString(3, user.getCorreo_electronico());
			
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
	
	
		
	/** Busca el nombre de un usuario en la BD
	* @param objeto de la tabla usuarios 
	* @returnInteger devuelve el nombre del usuario si existe y en caso contrario
	* 				 una cadena vacía
	*/
	public String getName(UsuariosVO user) {
		Connection conn = null;
		String name = "";
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement findPs = conn.prepareStatement(findBycorreo_electronico);
			findPs.setString(1, user.getCorreo_electronico());
			// Ejecutamos la orden
			ResultSet rset = findPs.executeQuery();
			rset.next();
			name = rset.getString("nombre");
			// liberamos los recursos utilizados
			findPs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return name;
	}
	
	/** Busca el número de teléfono de un usuario en la BD
	* @param objeto de la tabla usuarios con los datos actualizados
	* @returnInteger devuelve el número de teléfono del usuario si existe y 
	* 				 en caso contrario un 0
	*/
	public Integer getNumero_telefono(UsuariosVO user) {
		Connection conn = null;
		Integer numero = 0;
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement findPs = conn.prepareStatement(findBycorreo_electronico);
			findPs.setString(1, user.getCorreo_electronico());
			// Ejecutamos la orden
			ResultSet rset = findPs.executeQuery();
			rset.next();
			numero = Integer.valueOf(rset.getString("numero_telefono"));
			// liberamos los recursos utilizados
			findPs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
		return numero;
	}
	
	/** Borra un usuario de la BD
	* @param objeto de la tabla usuarios a borrar
	*/
	public void deleteUser(UsuariosVO user) {
		Connection conn = null;
		
		try {
			// Abrimos la conexión e inicializamos los parámetros 
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement psDelete = conn.prepareStatement(deleteUser);
			psDelete.setString(1, user.getCorreo_electronico());
			// Ejecutamos la orden
			psDelete.execute();
			// liberamos los recursos utilizados
			psDelete.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PoolConnectionManager.releaseConnection(conn);
		}
	}
}

