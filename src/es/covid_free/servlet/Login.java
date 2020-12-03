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
 * Servlet para gestionar login.jsp, que es la página de inicio de sesión del sistema
 * @author covid_free
 */
@WebServlet(description = "Servlet de login del usuario", urlPatterns = { "/login" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosFacade dao = new UsuariosFacade();	
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
        if(user != null) {
        	// En caso de que hay un usuario ya logeado, redirige al servlet dashboard
        	// que gestiona la página principal del usuario
            response.sendRedirect("dashboard");
            return;
        }
        
		if (request.getParameter("email") == null || request.getParameter("password") == null) {
			// Si hay algún campo del login sin rellenar (o si se pide la página por primera vez)
			// se reenvía la página
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			//request.removeAttribute("noBack");
			// En caso contrario, se procede a validar al usuario
			user = new UsuariosVO(request.getParameter("email"), request.getParameter("password"));
			boolean valido = dao.validateUser(user);
			if (valido) {
				// Si es válido, se le crea una cookie de sesión con sus datos (aunque solo esté el correo)
				// y se le redirige al dashboard (página principal del usuario)
				user.setContrasenya(null);
				request.getSession().setAttribute("user",user);
				response.sendRedirect("dashboard");
			} else {
				// En caso de que no sea válido, se reenvía la petición con un mensaje de error
				// para que lo muestre en login.jsp
				request.setAttribute("errorLogin", "invalid password or email");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
		//}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
