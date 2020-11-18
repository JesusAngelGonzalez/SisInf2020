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
 * Servlet implementation class Logged
 */
@WebServlet(description = "Servlet de dashboard", urlPatterns = { "/dashboard" })
public class Dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Dashboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("llega aqui");
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if (user == null) {
			//request.getSession().setAttribute("user",null);
			//request.getRequestDispatcher("login.jsp").forward(request, response);
			response.sendRedirect("login.jsp");
		} else {
			String correo = user.getCorreo_electronico();
			AcudirFacade af = new AcudirFacade(); 
			UsuariosFacade uf = new UsuariosFacade();
			System.out.println(correo);
			System.out.println("llega aqui");
			List<AcudirLugares> lista = af.getUltimosLugares(user);
			request.setAttribute("listaLugares", lista);
			String name = uf.getName(user);
			System.out.println(name);
			request.setAttribute("userName", name);
			
			//request.getSession().setAttribute("user", user);
			request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("llega aqui");
		doGet(request, response);
	}

}
