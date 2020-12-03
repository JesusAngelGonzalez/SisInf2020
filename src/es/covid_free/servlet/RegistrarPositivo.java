package es.covid_free.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import es.covid_free.MAIL.MailWrapper;


/**
 * Servlet para gestionar cuando un usuario da positivo en covid
 * @author covid_free
 */
@WebServlet(description = "Servlet de dashboard", urlPatterns = { "/positivo" })
public class RegistrarPositivo  extends HttpServlet {
	public RegistrarPositivo() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Se carga la información del usuario
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if (user == null) {
			// Si no existe (está logeado), se le redirige a la página de inicio de sesión  
			response.sendRedirect("login.jsp");
		} else {
			// Si existe, se le registra como positivo en la BD
			String correo = user.getCorreo_electronico();
			PositivoFacade pf = new PositivoFacade(); 
			UsuariosFacade uf = new UsuariosFacade();
			AcudirFacade af = new AcudirFacade();
			
			System.out.println(correo);
			Date now = new Date();
			PositivoVO positivo = new PositivoVO(user.getCorreo_electronico(), new Timestamp(now.getTime()) );
			pf.insertPositivo(positivo);
			
			// y se coge la lista de personas que hayan estado recientemente en contacto con el usuario
			// que ha dado positivo
			List<UsuariosVO> lista = pf.getPosiblesPositivos(positivo);
			/*for(UsuariosVO usuario: lista) {
				// y se le envía un email informándole de que puede que tenga el covid
				// a cada persona que haya estado en contacto con el positivo
				MailWrapper.sendEmail(usuario.getCorreo_electronico());
			}*/
			List<String> correos = new ArrayList<String>();
			for(UsuariosVO usuario : lista) {
				correos.add(usuario.getCorreo_electronico());
			}
			String[] arrCorreos = new String[correos.size()];
			arrCorreos = correos.toArray(arrCorreos);
			MailWrapper.sendEmail(arrCorreos);
			
			// Por último se carga un mensaje de confirmación del registro como positivo y
			// se reenvía la petición a dashboard
			request.setAttribute("positivoConf", "");
			
			request.getRequestDispatcher("dashboard").forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
