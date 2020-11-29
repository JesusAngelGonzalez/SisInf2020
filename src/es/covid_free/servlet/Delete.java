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

import es.covid_free.model.UsuariosVO;
import es.covid_free.model.UsuariosFacade;

/**
 * @author covid_free
 * Servlet para gestionar la eliminación de un usario
 */
@WebServlet(description = "Servlet para eliminar el usuario", urlPatterns = { "/delete" })
public class Delete extends HttpServlet {

    /** 
     * @see HttpServlet#HttpServlet()
     */
	public Delete() {
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosVO usuario = (UsuariosVO) request.getSession().getAttribute("user");
		if(usuario == null) {
			// Si no hay, redirige al login
			System.out.println("Saliendo de Delete...");
			response.sendRedirect("login");
			return;
		}
		
		// Creamos el objeto enlace con la BD
		UsuariosFacade uf = new UsuariosFacade();
		
		// Ordenamos la eliminación del usuario
		uf.deleteUser(usuario);
		
		// Invalidamos y borramos la cookie de sesión y redirigimos al servlet de login
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		response.sendRedirect("login");
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
