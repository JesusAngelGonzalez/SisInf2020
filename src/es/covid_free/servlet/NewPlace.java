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
			response.sendRedirect("login.jsp");
			return;
		}else {
			UsuariosFacade uf = new UsuariosFacade();
			request.setAttribute("userName", uf.getName(user));
		}
		if(request.getParameter("Lugar")!= null  && request.getParameter("Localizacion")!= null   && 
				request.getParameter("Fin")!= null   && request.getParameter("Inicio")!= null) {
			LugaresFacade dao = new LugaresFacade();
			String direccion = "";
			try {
				direccion = OSMWrapperAPI.getCorrectaDireccion(request.getParameter("Localizacion"), request.getParameter("Lugar"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(direccion);
			String direccionCompleta[] = direccion.split(",");
			if( direccionCompleta.length != 3) {
				request.setAttribute("wrongDir", "");
				request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
			}
			
			String localizacionCompleta = "";
			for(int i = 1;i < direccionCompleta.length-1;i++) {
				localizacionCompleta =localizacionCompleta + direccionCompleta[i] + ", ";
			}
			localizacionCompleta =localizacionCompleta + direccionCompleta[direccionCompleta.length-1];
			LugaresVO nuevoLugar = new LugaresVO(direccionCompleta[0], localizacionCompleta);
			Integer lugarId = dao.comprobarLugar(nuevoLugar);
			
			if(lugarId == -1) {
				lugarId = dao.insertLugar(nuevoLugar);				
			}
			
			AcudirFacade dao2 = new AcudirFacade();
			Timestamp horaIni = Timestamp.valueOf(request.getParameter("Inicio")+":00");
			Timestamp horaFin = Timestamp.valueOf(request.getParameter("Fin")+":00");
			AcudirVO nuevoAcudir = new AcudirVO(user.getCorreo_electronico(), horaIni,horaFin, lugarId);
			dao2.insertAcudir(nuevoAcudir);
			request.setAttribute("lugarConf", "");
			request.getRequestDispatcher("dashboard").forward(request, response);
			
		} else {
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
