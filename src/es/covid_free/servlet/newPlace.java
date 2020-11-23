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
 * Servlet implementation class Logged
 */
@WebServlet("/newPlace")
public class newPlace extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public newPlace() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if(user == null) {
			response.sendRedirect("login.jsp");
			return;
		}
		if(request.getParameter("Localizacion") != null && request.getParameter("Lugar")!= null) {
			LugaresFacade dao = new LugaresFacade();
			String direccion = "";
			try {
				direccion = OSMWrapperAPI.getCorrectaDireccion(request.getParameter("Localizacion"), request.getParameter("Lugar"));
			} catch (IOException | ParserConfigurationException | SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String direccionCompleta[] = direccion.split(",");
			String localizacionCompleta = "";
			for(int i = 1;i < direccionCompleta.length-1;i++) {
				localizacionCompleta =localizacionCompleta + direccionCompleta[i] + " , ";
			}
			localizacionCompleta =localizacionCompleta + direccionCompleta[direccionCompleta.length-1];
			LugaresVO nuevoLugar = new LugaresVO(direccionCompleta[0], localizacionCompleta);
			Integer lugarId = dao.comprobarLugar(nuevoLugar);
			
			if(lugarId == -1) {
				dao.insertLugar(nuevoLugar);
				lugarId = dao.comprobarLugar(nuevoLugar);
				
			}
			if(request.getParameter("Lugar")!= null  && request.getParameter("Localizacion")!= null   && 
					request.getParameter("Fin")!= null   && request.getParameter("Inicio")!= null  ) {
				AcudirFacade dao2 = new AcudirFacade();
				String divisionInicio[] = request.getParameter("Inicio").split("T");
				String fechaI[] = divisionInicio[0].split("-");
				String horaI[] = divisionInicio[1].split(":");
				Instant horaInicio = LocalDateTime.of(Integer.valueOf(fechaI[0]), Month.of(Integer.valueOf(fechaI[1])), Integer.valueOf(fechaI[2]), Integer.valueOf(horaI[0]),Integer.valueOf(horaI[1] )).atZone(ZoneId.of("Europe/Paris")).toInstant();
				Timestamp horaIni = Timestamp.from(horaInicio);
				
				String divisionFinal[] = request.getParameter("Fin").split("T");
				String fechaF[] = divisionFinal[0].split("-");
				String horaF[] = divisionFinal[1].split(":");
				Instant horaFinal = LocalDateTime.of(Integer.valueOf(fechaF[0]), Month.of(Integer.valueOf(fechaF[1])), Integer.valueOf(fechaF[2]), Integer.valueOf(horaF[0]),Integer.valueOf(horaF[1] )).atZone(ZoneId.of("Europe/Paris")).toInstant();
				Timestamp horaFin = Timestamp.from(horaFinal);
				AcudirVO nuevoAcudir = new AcudirVO(user.getCorreo_electronico(), horaIni,horaFin, lugarId);
				dao2.insertAcudir(nuevoAcudir);
				request.setAttribute("exito", direccionCompleta[0] + localizacionCompleta);
				request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
			}
			else {
				request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
			}
		}
		else {
			request.getRequestDispatcher("/WEB-INF/newPlace.jsp").forward(request, response);
		}
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
