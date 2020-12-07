package es.covid_free.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

/**
 * Servlet para gestionar profile.jsp, encargado de ver y modificar datos del usuario
 * @author covid_free
 */
@WebServlet(description = "Servlet de perfil del usuario", urlPatterns = { "/profile"})
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		UsuariosFacade dao = new UsuariosFacade();
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		
		if(user == null) {
			// Si no hay usuarios logeados, redirigimos a login
			response.sendRedirect("login.jsp");
			return;
		}
		// De momento no hay nada que modificar
		boolean actualizar = false;
		String usuario = "";
		Integer telefono = 0;
		
		// Mostramos los atributos del usuario en cuestión
		request.setAttribute("user.name", dao.getName(user));
		request.setAttribute("actualMail", user.getCorreo_electronico());
		request.setAttribute("numero_telefono", dao.getNumero_telefono(user));
		
		// El usuario ha introducido un nuevo nombre
		if(request.getParameter("username") != null) {
			// Guardamos el nuevo nombre introducido
			usuario = Jsoup.clean(request.getParameter("username"), Whitelist.none());
			actualizar = true;
		}
		else {
			// Si no ha introducido nombre, cogemos el que tiene
			usuario = dao.getName(user);
		}
		
		// El usuario ha introducido un número de teléfono
		if((request.getParameter("numero_telefono") != null && !request.getParameter("numero_telefono").isEmpty())){
			// Guardamos el nuevo telefono introducido
			telefono = Integer.valueOf(request.getParameter("numero_telefono"));
			actualizar = true;
		}
		else {
			// Si no ha introducido un numero de telefono, cogemos el que tiene
			telefono = dao.getNumero_telefono(user);
		}
		
		if (!usuario.isEmpty()) {
			user.setNombre(usuario);
		}
		if(telefono != 0) {
			user.setNumero_telefono(telefono);
		}
		if(actualizar) {
			// Si hay algún dato a modificar, actualizamos el usuario en la BD
			dao.updateUser(user);
			request.setAttribute("user.name", dao.getName(user));
			request.setAttribute("numero_telefono", dao.getNumero_telefono(user));
		}
		// Por último reenviamos la petición a profile.jsp para mostrar al usuario sus datos
		request.getRequestDispatcher("/WEB-INF/profile.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
