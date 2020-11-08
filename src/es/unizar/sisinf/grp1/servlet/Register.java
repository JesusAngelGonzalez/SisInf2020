package es.unizar.sisinf.grp1.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.unizar.sisinf.grp1.model.UsuariosFacade;
import es.unizar.sisinf.grp1.model.UsuariosVO;

/**
 * Servlet implementation class Signin
 */
@WebServlet(description = "Servlet de autenticación del usuario", urlPatterns = { "/register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosFacade dao = new UsuariosFacade();		
		
		if (request.getParameter("email") == null || request.getParameter("telefono") == null ||
			request.getParameter("user") == null || request.getParameter("password") == null) {
			
			request.setAttribute("error3", "hay que rellenar todos los campos");
			request.getRequestDispatcher("register.jsp").forward(request, response);
		} else {
			//public UsuariosVO(String correo_electronico, String contrasenya, Integer numero_telefono, String nombre)
			UsuariosVO user = new UsuariosVO(request.getParameter("email"), request.getParameter("password"),
					Integer.valueOf(request.getParameter("telefono")), request.getParameter("user"));
			int valido = dao.insertUser(user);
			if (valido == 0) {
				request.setAttribute("exito", "Registro Exitoso");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			} else if(valido == 1){
				request.setAttribute("errorCorreo", "Correo ya registrado");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			} else { //error de la BD o de la conexion con ella
				request.setAttribute("error2", "Error en el registro");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
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
