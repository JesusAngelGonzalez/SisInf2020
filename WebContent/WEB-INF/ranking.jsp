<!DOCTYPE html>
<html>

<head>
    <%@ page contentType="text/html; charset=UTF-8" %>
    <%@ page import="java.util.List,es.covid_free.model.LugarRanking"%>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Ranking - CovidFree</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i">
    <link rel="stylesheet" href="assets/fonts/fontawesome-all.min.css">
    <link rel="stylesheet" href="assets/fonts/font-awesome.min.css">
    <link rel="stylesheet" href="assets/fonts/fontawesome5-overrides.min.css">
    <link rel="stylesheet" href="assets/css/Features-Clean.css">
    <link rel="stylesheet" href="assets/css/Highlight-Blue.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css">
    <link rel="stylesheet" href="assets/css/Navigation-with-Button.css">
</head>

<body id="page-top">
    <div id="wrapper">
        <nav class="navbar navbar-dark align-items-start sidebar sidebar-dark accordion bg-gradient-primary p-0">
            <div class="container-fluid d-flex flex-column p-0">
                <a class="navbar-brand d-flex justify-content-center align-items-center sidebar-brand m-0" href="#">
                    <div class="sidebar-brand-icon rotate-n-15"><i class="fas fa-laugh-wink"></i></div>
                    <div class="sidebar-brand-text mx-3"><span>CovidFree</span></div>
                </a>
                <hr class="sidebar-divider my-0">
                <ul class="nav navbar-nav text-light" id="accordionSidebar">
                    <li class="nav-item" role="presentation"><a class="nav-link" href="dashboard"><i class="fas fa-tachometer-alt"></i><span>Inicio</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link" href="new-place"><i class="far fa-building"></i><span>Añadir Lugar</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link active" href="ranking"><i class="fas fa-table"></i><span>Ranking</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link" href="profile"><i class="fas fa-user"></i><span>Perfil de Usuario</span></a></li>
                </ul>
                <div class="text-center d-none d-md-inline"><button class="btn rounded-circle border-0" id="sidebarToggle" type="button"></button></div>
            </div>
        </nav>
        <div class="d-flex flex-column" id="content-wrapper">
            <div id="content">
                <nav class="navbar navbar-light navbar-expand bg-white shadow mb-4 topbar static-top">
                    <div class="container-fluid"><button class="btn btn-link d-md-none rounded-circle mr-3" id="sidebarToggleTop" type="button"><i class="fas fa-bars"></i></button>
                        <ul class="nav navbar-nav flex-nowrap ml-auto">
                            <li class="nav-item dropdown no-arrow mx-1" role="presentation">
                                <div class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="badge badge-danger badge-counter">1</span><i class="fas fa-bell fa-fw"></i></a>
                                    <div class="dropdown-menu dropdown-menu-right dropdown-list dropdown-menu-right animated--grow-in"
                                        role="menu">
                                        <h6 class="dropdown-header">Centro de notificaciones</h6>
                                        <a class="d-flex align-items-center dropdown-item" href="#">
                                            <div class="mr-3">
                                                <div class="bg-primary icon-circle"><i class="far fa-bell text-white"></i></div>
                                            </div>
                                            <div><span class="small text-gray-500">December 12, 2019</span>
                                                <p>Has estado en contacto con un positivo el XX/YY/ZZZZ a las AA:BB</p>
                                            </div>
                                        </a>
                                    </div>
                                </div>
                            </li>
                            <div class="d-none d-sm-block topbar-divider"></div>
                            <li class="nav-item dropdown no-arrow" role="presentation">
                            	<%if (request.getAttribute("userName")!= null) { 
                            	 System.out.println(request.getAttribute("userName"));%>
                                <div class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="d-none d-lg-inline mr-2 text-gray-600 small"><%= request.getAttribute("userName") %></span></a>
                                <%} %>
                                    <div class="dropdown-menu shadow dropdown-menu-right animated--grow-in"
                                        role="menu"><a class="dropdown-item" role="presentation" href="profile"><i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Perfil</a>
                                        <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="/logout"><i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Cerrar Sesión</a></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
                <div class="container-fluid">
                    <h3 class="text-dark mb-4">Estadísticas</h3>
                    <div class="card shadow">
                        <div class="card-header py-3">
                            <p class="text-primary m-0 font-weight-bold"><%= request.getAttribute("tipoRanking") %></p>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 text-nowrap">
                                    <div class="dataTables_filter" id="dataTable_filter"><label>
                                    	<select id="cambiarRanking" class="form-control form-control-sm custom-select custom-select-sm">
                                    		<option value="TopVisitas" <% if(request.getAttribute("tipoRanking").equals("TopVisitas")){ %>selected<% } %>>Más Visitados</option>
                                    		<option value="MenosPositivos" <% if(request.getAttribute("tipoRanking").equals("MenosPositivos")){ %>selected<% } %>>Menos Positivos</option>
                                    	 	<option value="MasPositivos" <% if(request.getAttribute("tipoRanking").equals("MasPositivos")){ %>selected<% } %>>Más Positivos</option>
                                    	</select>
                                    </label></div>
                                </div>
                            </div>
                            <div class="table-responsive table mt-2" id="dataTable" role="grid" aria-describedby="dataTable_info">
                                <table class="table dataTable my-0" id="dataTable">
                                    <thead>
                                        <tr>
                                        	<th>Posición</th>
                                            <th>Lugar</th>
                                            <th>Dirección</th>
                                            <% if(request.getAttribute("tipoRanking").equals("TopVisitas") ){ %>
                                            <th>Visitantes</th>
                                            <% } else {%>
                                            <th>Numero de Casos</th>
                                            <% } %>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<% //TODO: Asegurar que el comportamiento por default rankingLista no sea nula
	                                        List<LugarRanking> lista = (List<LugarRanking>) request.getAttribute("rankingLista");
                                    	
                                    		for(LugarRanking sitio : lista){
                                    	%>
	                                        <tr>
	                                            <td><%= sitio.getPosicion() %></td>
	                                            <td><%= sitio.getNombre() %></td>
	                                            <td><%= sitio.getDireccion() %></td>
                                           		<% if(request.getAttribute("tipoRanking").equals("TopVisitas") ){ %>
	                                            	<td><%= sitio.getVisitantes() %></td>
                                            	<% }else{ %>
	                                            <td><%= sitio.getCasos() %></td>
                                            	<% } %>
	                                        </tr>
                                        <% } %>
                                </table>
                            </div>
                            <div class="row">
                            	<%
                            		int n = (int)request.getAttribute("n");
                            		int max = (int)request.getAttribute("max");
                            	
                            	%>
                                                      
                                <div class="col-md-6 align-self-center">
                                    <p id="dataTable_info" class="dataTables_info" role="status" aria-live="polite">Mostrando del <%= (n-1)*10+1 %> al <%= n*10 %> de <%= max %></p>
                                </div>
                                <div class="col-md-6">
                                    <nav class="d-lg-flex justify-content-lg-end dataTables_paginate paging_simple_numbers">
                                        <ul class="pagination">
                                            <li class="page-item <%if(n == 1){%>disabled <%}%>"><a class="page-link" href="ranking?tipoRanking=<%= request.getAttribute("tipoRanking") %>&n=<%= n-1 %>" aria-label="Previous"><span aria-hidden="true">«</span></a></li>
                                            <li class="page-item active"><a class="page-link" href="ranking?tipoRanking=<%= request.getAttribute("tipoRanking") %>&n=<%= request.getAttribute("n") %>"><%= request.getAttribute("n") %></a></li>
                                           	<li class="page-item <% if(n*10 > max){ %>disabled<% } %>"><a class="page-link" href="ranking?tipoRanking=<%= request.getAttribute("tipoRanking") %>&n=<%= n+1 %>"><%= n+1 %></a></li>
                                            <li class="page-item <% if((n+1)*10 > max){ %>disabled<% } %>"><a class="page-link" href="ranking?tipoRanking=<%= request.getAttribute("tipoRanking") %>&n=<%= n+2 %>"><%= n+2 %></a></li>
                                            <li class="page-item <% if((n+2)*10 > max){ %>disabled<% } %>"><a class="page-link" href="ranking?tipoRanking=<%= request.getAttribute("tipoRanking") %>&n=<%= n+3 %>" aria-label="Next"><span aria-hidden="true">»</span></a></li>
                                        </ul>
                                    </nav>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <footer class="bg-white sticky-footer">
                <div class="container my-auto">
                    <div class="text-center my-auto copyright"><span>Copyright © CovidFree 2020</span></div>
                </div>
            </footer>
        </div><a class="border rounded d-inline scroll-to-top" href="#page-top"><i class="fas fa-angle-up"></i></a></div>
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.js"></script>
    <script src="assets/js/theme.js"></script>
    <script>
    	document.getElementById("cambiarRanking").onchange = function(){
    		document.location.href = "ranking?tipoRanking=" + this.children[this.selectedIndex].getAttribute("value");
    	}
    </script>
</body>

</html>
