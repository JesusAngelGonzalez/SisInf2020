package es.covid_free.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.covid_free.model.LugarRanking;
import es.covid_free.model.LugaresFacade;
import es.covid_free.model.UsuariosFacade;
import es.covid_free.model.UsuariosVO;

@WebServlet(description = "Servlet de perfil del usuario", urlPatterns = { "/ranking"})
public class Ranking extends HttpServlet  {
	private static final long serialVersionUID = 1L;
	private static final String TOP_VISITAS = "TopVisitas";
	private static final String MAS_POSITIVOS = "MasPositivos";
	private static final String MENOS_POSITIVOS = "MenosPositivos";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Ranking() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuariosVO user = (UsuariosVO) request.getSession().getAttribute("user");
		if (user == null) {
			System.out.println("sale de aqui");
			response.sendRedirect("login.jsp");
		} else {
			LugaresFacade dao = new LugaresFacade();
			UsuariosFacade uf = new UsuariosFacade();
			String tipo;
			Integer n;
			//TODO no definido
			
			if (request.getParameter("tipoRanking") == null) {
				tipo = TOP_VISITAS;
			} else {
				tipo = request.getParameter("tipoRanking");
			}
			
			System.out.println("Tipo = " + tipo + "		tipoRanking = " + request.getParameter("tipoRanking"));
			
			if (request.getParameter("n") == null) {
				n = 1;
			} else {
				n =  Integer.valueOf(request.getParameter("n"));
				if(n == 0) n = 1;
			}
			
			List<LugarRanking> lista = new ArrayList<LugarRanking>();
			
			if(tipo.equals(TOP_VISITAS)) {
				System.out.println("top visitas");
				lista = dao.getRankingVisitas();
			}else if(tipo.equals(MAS_POSITIVOS)) {
				lista = dao.getRankingMasPositivos();
				System.out.println("mas positivos");
			}else if(tipo.equals(MENOS_POSITIVOS)) {
				lista = dao.getRankingMenosPositivos();
				System.out.println("menos positivos");
			}
			/*
			switch(tipo) {
				case TOP_VISITAS:
					System.out.println("top visitas");
					lista = dao.getRankingVisitas();
					break;
				case MAS_POSITIVOS:	
					lista = dao.getRankingMasPositivos();
					System.out.println("mas positivos");
					break;
				case MENOS_POSITIVOS:
					lista = dao.getRankingMenosPositivos();
					System.out.println("menos positivos");
					break;
				default:
					break;
			}*/
			int max = lista.size();
			
			List<LugarRanking> list = new ArrayList<LugarRanking>();
			for(int i = (n-1)*10; i<n*10 && i<lista.size(); i++) {
				list.add(lista.get(i));
			}
			request.setAttribute("rankingLista", list);
			request.setAttribute("tipoRanking", tipo);
			request.setAttribute("userName", uf.getName(user));
			request.setAttribute("n", n);
			request.setAttribute("max", max);
			
			
			request.getRequestDispatcher("/WEB-INF/ranking.jsp").forward(request, response);
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
