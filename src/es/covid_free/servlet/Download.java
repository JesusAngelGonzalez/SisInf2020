/**
 * 
 */
package es.covid_free.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.InformacionFacade;
import es.covid_free.model.UsuariosVO;

/**
 * @author covid_free
 *
 */
@WebServlet(description = "Servlet para descargar la información del usuario", urlPatterns = { "/download" })
public class Download extends HttpServlet {

	/**
	 * 
	 */
	public Download() {
		super();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosVO usuario = (UsuariosVO) request.getSession().getAttribute("user");
		if(usuario == null) {
			// Si no hay, redirige al login
			response.sendRedirect("login");
			return;
		}
		
		InformacionFacade info = new InformacionFacade();
		//Generamos el fichero zip y se obtiene su dirección
		String fichero = info.prepararFichero(usuario);
		request.setAttribute("fichero", fichero);

		request.getRequestDispatcher("/WEB-INF/download.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
