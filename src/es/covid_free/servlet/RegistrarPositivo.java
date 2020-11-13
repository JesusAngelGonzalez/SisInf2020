package es.covid_free.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.AcudirFacade;
import es.covid_free.model.LugaresVO;
import es.covid_free.model.PositivoFacade;
import es.covid_free.model.PositivoVO;
import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

@WebServlet(description = "Servlet de dashboard", urlPatterns = { "/positivo" })
public class RegistrarPositivo  extends HttpServlet {
	public RegistrarPositivo() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if (user == null) {
			//request.getSession().setAttribute("user",null);
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			String correo = user.getCorreo_electronico();
			PositivoFacade pf = new PositivoFacade(); 
			UsuariosFacade uf = new UsuariosFacade();
			AcudirFacade af = new AcudirFacade();
			
			System.out.println(correo);
			Date now = new Date();
			PositivoVO positivo = new PositivoVO(user.getCorreo_electronico(), new Timestamp(now.getTime()) );
			pf.insertPositivo(positivo);
			
			List<UsuariosVO> lista = pf.getPosiblesPositivos(positivo);
			for(UsuariosVO usuario: lista) {
				/* TODO:
				 * Enviar correo electrónico
				 * Enviar SMS
				 * Añadir notificacion*/
			}
			
			List<LugaresVO> list = af.getUltimosLugares(user);
			
			request.setAttribute("listaLugares", list);
			request.setAttribute("userName", uf.getName(user));
			request.setAttribute("positivoConf", "klk manin");
			
			//request.getSession().setAttribute("user", user);
			request.getRequestDispatcher("dashboard.jsp").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
