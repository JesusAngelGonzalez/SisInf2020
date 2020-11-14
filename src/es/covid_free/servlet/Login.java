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
 * Servlet implementation class Signin
 */
@WebServlet(description = "Servlet de login del usuario", urlPatterns = { "/login" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosFacade dao = new UsuariosFacade();	
		
		
		/*if (request.getParameter("email") == null || request.getParameter("password") == null) {
			request.getRequestDispatcher("login.jsp").forward(request, response);
			
			request.setAttribute("error2", "introduzca usuario y contraseña");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {*/
		System.out.println("Ejecutando login");
		UsuariosVO user = new UsuariosVO(request.getParameter("email"), request.getParameter("password"));
		boolean valido = dao.validateUser(user);
		if (valido) {
			user.setContrasenya(null);
			request.getSession().setAttribute("user",user);
			//request.getRequestDispatcher("dashboard.jsp").forward(request, response);
			//response.sendRedirect("dashboard");
			request.getRequestDispatcher("dashboard").forward(request, response);
		} else {
			request.setAttribute("errorLogin", "invalid password or email");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		//}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
