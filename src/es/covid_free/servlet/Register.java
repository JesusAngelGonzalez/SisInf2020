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
 * Servlet para gestionar register.jsp, que es la página de registro de un usuario
 * @author covid_free
 */
@WebServlet(description = "Servlet de autenticación del usuario", urlPatterns = { "/register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosFacade dao = new UsuariosFacade();
		
		if (request.getParameter("email") == null || request.getParameter("telefono") == null ||
			request.getParameter("user") == null || request.getParameter("password") == null) {
			// Si hay algún parámetro necesario nulo se reenvía la página de registro
			request.getRequestDispatcher("register.jsp").forward(request, response);
		} else {
			//public UsuariosVO(String correo_electronico, String contrasenya, Integer numero_telefono, String nombre)
			// Se crea un objeto de la tabla de usuarios y se introduce en la BD
			UsuariosVO user = new UsuariosVO(request.getParameter("email"), request.getParameter("password"),
					Integer.valueOf(request.getParameter("telefono")), request.getParameter("user"));
			int valido = dao.insertUser(user);
			if (valido == 0) {
				// Si se introduce correctamente y se reenvía petición al login junto con un mensaje de éxito
				request.setAttribute("exito", "Registro Exitoso");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			} else if(valido == 1){
				// Si el correo (clave primaria) está ya en la BD se muestra la página de registro 
				// con un mensaje de error
				request.setAttribute("errorCorreo", "Correo ya registrado");
				request.getRequestDispatcher("register.jsp").forward(request, response);
			} else {
				// Si hay un error de la BD o en la conexion con ella, se vuelve a la página de registro
				request.getRequestDispatcher("register.jsp").forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
