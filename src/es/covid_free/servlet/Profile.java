package es.covid_free.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

/**
 * Servlet implementation class Perfil
 */
@WebServlet(description = "Servlet de perfil del usuario", urlPatterns = { "/profile"})
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		UsuariosFacade dao = new UsuariosFacade();
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		
		if(user == null) {
			System.out.println("sale de aqui");
			response.sendRedirect("login.jsp");
			return;
		}
		
		boolean actualizar = false;
		String usuario = "";
		Integer telefono = 0;
		
		request.setAttribute("user.name", dao.getName(user));
		request.setAttribute("actualMail", user.getCorreo_electronico());
		request.setAttribute("numero_telefono", dao.getNumero_telefono(user));
		
		if(request.getParameter("username") != null) {
			usuario = request.getParameter("username");
			actualizar = true;
		}
		else {
			usuario = dao.getName(user);
		}
		if((request.getParameter("numero_telefono") != null && !request.getParameter("numero_telefono").isEmpty())){
			telefono = Integer.valueOf(request.getParameter("numero_telefono"));
			actualizar = true;
		}
		else {
			telefono = dao.getNumero_telefono(user);
		}
		
		if (!usuario.isEmpty()) {
			user.setNombre(usuario);
		}
		if(telefono != 0) {
			user.setNumero_telefono(telefono);
		}
		if(actualizar) {
			dao.updateUser(user);
			request.setAttribute("user.name", dao.getName(user));
			request.setAttribute("numero_telefono", dao.getNumero_telefono(user));
		}
		request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		doGet(request, response);
	}
	
}
