package es.covid_free.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import es.covid_free.db.PoolConnectionManager;

public class InformacionFacade {
	private static final String INFO_USUARIO = "SELECT correo_electronico, numero_telefono, nombre FROM web_data.usuarios WHERE correo_electronico = ?";
	private static final String INFO_ACUDIR =	"SELECT A.correo_electronico, L.nombre, L.ubicacion, A.inicio, A.final " +
												"FROM web_data.acudir A, web_data.lugares L " + 
												"WHERE A.correo_electronico = ? AND A.id_ubicacion = L.id";
	private static final String INFO_POSITIVOS = "SELECT * FROM web_data.positivos WHERE correo_electronico = ?";
	
	public String prepararFichero(UsuariosVO usuario) {
		try {
			//CREAR UNA CARPETA TEMPORAL
			Path ruta = Files.createTempDirectory(usuario.getCorreo_electronico()+"_");

			//CREAR LOS FICHEROS EN LA CARPÈTA TEMPÒRAL
			makeUsuario(ruta, usuario);
			makeAcudir(ruta, usuario);
			makePositivos(ruta, usuario);
			
			//COMPRIMIR LA CARPETA TEMPORAL
			String fichero = ruta.toString() + ".zip";
			FileOutputStream fos = new FileOutputStream(fichero);
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			File toZip = new File(ruta.toString());
			
			zip(toZip, ruta.toString(), zipOut);
			
			zipOut.close();
			fos.close();
			
			//BORRAR LA CARPETA TEMPORAL
			for(String s : toZip.list()) {
				File tmp = new File(toZip.getPath(),s);
				tmp.delete();
			}
			toZip.delete();
			
			return fichero;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	private static void zip(File toZip, String ruta, ZipOutputStream zipOut) throws IOException{
		if(toZip.isHidden()) {
			return;
		}
		if(toZip.isDirectory()) {
			zipOut.putNextEntry(new ZipEntry(ruta + "/"));
			for(File childFile : toZip.listFiles()) {
				zip(childFile, ruta+"/"+childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(toZip);
		ZipEntry zipEntry = new ZipEntry(ruta);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

	private void makeUsuario(Path ruta, UsuariosVO usuario) {
		Connection conn = null;
		try {
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement prepared = conn.prepareStatement(INFO_USUARIO);
			prepared.setString(1, usuario.getCorreo_electronico());

			ResultSet resultado = prepared.executeQuery();
			File data = new File(ruta.toString() + "/usuario.csv");
			data.createNewFile();
			
			/*
			 * SELECT correo_electronico, numero_telefono, nombre
			 * FROM web_data.usuarios
			 * WHERE correo_electronico = ?
			 */
			FileOutputStream fos = new FileOutputStream(data.toString());
			while(resultado.next()) {
				String out = "";
				out += resultado.getString("correo_electronico") + ";";
				out += resultado.getString("numero_telefono") + ";";
				out += resultado.getString("nombre") + "\n";
				fos.write(out.getBytes());
			}
			fos.close();
			
		} catch(SQLException se) {
			se.printStackTrace();  
		
		} catch(Exception e) {
			e.printStackTrace(System.err); 
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
	}
	
	private void makePositivos(Path ruta, UsuariosVO usuario) {
		Connection conn = null;
		try {
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement prepared = conn.prepareStatement(INFO_POSITIVOS);
			prepared.setString(1, usuario.getCorreo_electronico());

			ResultSet resultado = prepared.executeQuery();
			File data = new File(ruta.toString() + "/positivo.csv");

			/*
			 * SELECT * 
			 * FROM web_data.positivos 
			 * WHERE correo_electronico = ?
			 */
			FileOutputStream fos = new FileOutputStream(data.toString());
			while(resultado.next()) {
				String out = "";
				out += resultado.getString("correo_electronico") + ";";
				out += resultado.getString("fecha") + "\n";
				fos.write(out.getBytes());
			}
			fos.close();
		} catch(SQLException se) {
			se.printStackTrace();  
		
		} catch(Exception e) {
			e.printStackTrace(System.err); 
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
	}
	
	private void makeAcudir(Path ruta, UsuariosVO usuario) {
		Connection conn = null;
		try {
			conn = PoolConnectionManager.getConnection(); 
			PreparedStatement prepared = conn.prepareStatement(INFO_ACUDIR);
			prepared.setString(1, usuario.getCorreo_electronico());

			ResultSet resultado = prepared.executeQuery();
			File data = new File(ruta.toString() + "/acudido.csv");
			
			/*
			 * SELECT A.correo_electronico, L.nombre, L.ubicacion, A.inicio, A.final
			 * FROM web_data.acudir A, web_data.lugares L
			 * WHERE A.correo_electronico = ? AND A.id_ubicacion = L.id"
			 */
			FileOutputStream fos = new FileOutputStream(data.toString());
			while(resultado.next()) {
				String out = "";
				out += resultado.getString("correo_electronico") + ";";
				out += resultado.getString("nombre") + ";";
				out += resultado.getString("ubicacion") + ";";
				out += resultado.getString("inicio") + ";";
				out += resultado.getString("final") + "\n";
				fos.write(out.getBytes());
			}
			fos.close();
		} catch(SQLException se) {
			se.printStackTrace();  
		
		} catch(Exception e) {
			e.printStackTrace(System.err); 
		} finally {
			PoolConnectionManager.releaseConnection(conn); 
		}
	}
}





