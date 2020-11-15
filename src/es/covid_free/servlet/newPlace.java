package es.covid_free.servlet;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.AcudirFacade;
import es.covid_free.model.AcudirVO;
import es.covid_free.model.LugaresFacade;
import es.covid_free.model.LugaresVO;
import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

/**
 * Servlet implementation class Logged
 */
@WebServlet("/new-place")
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
		LugaresFacade dao = new LugaresFacade();
		LugaresVO nuevoLugar = new LugaresVO(request.getParameter("Lugar"), request.getParameter("Localizacion"));
		
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		
		String existe = dao.comprobarLugar(nuevoLugar);
		
		if(existe.isEmpty()) {
			dao.insertLugar(nuevoLugar);
			
		}
		Integer lugarId = dao.getLugarId(nuevoLugar);
		AcudirFacade dao2 = new AcudirFacade();
		AcudirVO nuevoAcudir = new AcudirVO(user.getCorreo_electronico(), Timestamp.valueOf(request.getParameter("Inico")),
				Timestamp.valueOf(request.getParameter("Fin")), lugarId);
		dao2.insertAcudir(nuevoAcudir);
		

		//LugaresVO lugar = new LugaresVO (request.getParameter("email"), request.getParameter("nombre"), request.getParameter("ubicación"));
		request.getRequestDispatcher("/WEB-INF/new-place.jsp").forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
