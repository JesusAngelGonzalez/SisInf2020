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
		
		System.out.print(user.getCorreo_electronico());
		
		request.setAttribute("user.name", user.getNombre());
		request.getRequestDispatcher("profile.jsp").forward(request, response);
		request.setAttribute("actualMail", "aaaaaaa");
		request.getRequestDispatcher("profile.jsp").forward(request, response);
		
		String usuario = request.getParameter("username");
		Integer telefono = Integer.valueOf(request.getParameter("first_name"));
		if (!usuario.isEmpty()) {
			user.setNombre(usuario);
		}
		if(telefono != 0) {
			user.setNumero_telefono(telefono);
		}
		dao.updateUser(user);
		request.getRequestDispatcher("perfil.jsp").forward(request, response);
		System.out.println(user.getNombre());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet(request, response);
	}
	
}
