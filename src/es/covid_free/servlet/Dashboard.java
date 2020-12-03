package es.covid_free.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.AcudirFacade;
import es.covid_free.model.AcudirLugares;
import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

/**
 * Servlet para gestionar dashboard.jsp, que es la página principal de los usuarios
 * @author covid_free
 */
@WebServlet(description = "Servlet de dashboard", urlPatterns = { "/dashboard" })
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /** 
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Coge información del usuario logeado
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if (user == null) {
			// Si no hay, redirige al login
			response.sendRedirect("login");
		} else {
			// En caso de que exista, coge de la BD la lista de los últimos lugares
			// que ha visitado
			String correo = user.getCorreo_electronico();
			AcudirFacade af = new AcudirFacade(); 
			UsuariosFacade uf = new UsuariosFacade();
			System.out.println(correo);
			List<AcudirLugares> lista = af.getUltimosLugares(user);
			// Establece un atributo con la lista mencionada para que pueda ser
			// usado por dashboard.jsp para mostrar dicha lista
			request.setAttribute("listaLugares", lista);
			// Coge el nombre de usuario correspondiente  de la BD
			String name = uf.getName(user);
			// y se lo manda al .jsp para que lo muestre
			request.setAttribute("userName", name);
			
			//request.getSession().setAttribute("user", user);
			// Por ultimo reenvía la petición a dashboard.jsp para mostrar la pagina
			// al usuario
			request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
