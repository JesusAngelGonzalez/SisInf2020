package es.covid_free.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.xml.sax.SAXException;

import es.covid_free.OSM.OSMWrapperAPI;
import es.covid_free.model.AcudirFacade;
import es.covid_free.model.AcudirVO;
import es.covid_free.model.LugaresFacade;
import es.covid_free.model.LugaresVO;
import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

/**
 * Servlet para gestionar newPlace.jsp, que es la página para añadir un lugar en el que un usuario ha estado
 * @author covid_free
 */
@WebServlet(description = "Servlet de logout del usuario", urlPatterns = { "/newPlace" })

public class NewPlace extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewPlace() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if(user == null) {
			// Si no hay usuarios logeados, redirigimos a login
			response.sendRedirect("login.jsp");
			return;
		}else {
			// Preparamos un atributo para mostrar el nombre del usuario
			UsuariosFacade uf = new UsuariosFacade();
			request.setAttribute("userName", uf.getName(user));
		}
		if(request.getParameter("Lugar")!= null  && request.getParameter("Localizacion")!= null   && 
				request.getParameter("Fin")!= null   && request.getParameter("Inicio")!= null) {
			// Si se han introducido los parámetros necesarios se procede a tratar la petición
			LugaresFacade dao = new LugaresFacade();
			// Comprobamos que el lugar exista y guardar los datos de forma que dos formas de escribir
			// un mismo lugar no  afecte a que se consideren distintos lugares
			String direccion = "";
			try {
				direccion = OSMWrapperAPI.getCorrectaDireccion(
						Jsoup.clean(request.getParameter("Localizacion"), Whitelist.none()),
						Jsoup.clean(request.getParameter("Lugar"), Whitelist.none())
						);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(direccion);
			String direccionCompleta[] = direccion.split(",");
			if( direccionCompleta.length != 3) {
				// Si no existe el lugar, reenviamos newPlace.jsp mostrando un mensaje de error
				request.setAttribute("wrongDir", "");
				request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
				return;
			}
			// Adaptamos los datos para introducirlos en la BD
			String localizacionCompleta = "";
			for(int i = 1;i < direccionCompleta.length-1;i++) {
				localizacionCompleta =localizacionCompleta + direccionCompleta[i] + ", ";
			}
			localizacionCompleta =localizacionCompleta + direccionCompleta[direccionCompleta.length-1];
			LugaresVO nuevoLugar = new LugaresVO(direccionCompleta[0], localizacionCompleta);
			// Comprobamos si el lugar ya está en la BD
			Integer lugarId = dao.comprobarLugar(nuevoLugar);
			
			if(lugarId == -1) {
				// En caso de que no esté, lo introducimos
				lugarId = dao.insertLugar(nuevoLugar);
				if(lugarId == -1) {
					// En caso de error, avisamos al usuario
					request.setAttribute("wrongDir", "");
					request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
					return;		
				}		
			}
			// Ahora se procede a sacar los datos del formulario para introducir en la BD la información
			// de que un usuario ha visitado un lugar en la tabla acudir
			AcudirFacade dao2 = new AcudirFacade();
			Timestamp horaIni, horaFin;
			try {
			horaIni = Timestamp.valueOf(request.getParameter("Inicio")+":00");
			horaFin = Timestamp.valueOf(request.getParameter("Fin")+":00");
			}catch(IllegalArgumentException e) { // Sólo saltará si la petición POST se ha hecho a mano y es invalida
				request.setAttribute("lugarErr", "");
				request.getRequestDispatcher("dashboard").forward(request, response);
				return;
			}
			
			AcudirVO nuevoAcudir = new AcudirVO(user.getCorreo_electronico(), horaIni,horaFin, lugarId);
			if( dao2.insertAcudir(nuevoAcudir) ){
				request.setAttribute("lugarConf", "");
			}else {
				request.setAttribute("lugarErr", "");
			}
			
			// Una vez guardados los datos, se prepara un mensaje de confirmación y se reenvía la petición
			// al dashboard
			request.getRequestDispatcher("dashboard").forward(request, response);
			
		} else {
			// En caso contrario, reenviamos la página newPlace.jsp
			request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
		}
		
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
