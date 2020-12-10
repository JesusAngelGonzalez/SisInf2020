<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Perfil - CovidFree</title>
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
                    <li class="nav-item" role="presentation"><a class="nav-link" href="newPlace"><i class="far fa-building"></i><span>Añadir Lugar</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link" href="ranking"><i class="fas fa-table"></i><span>Ranking</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link active" href="profile"><i class="fas fa-user"></i><span>Perfil de Usuario</span></a></li>
                </ul>
                <div class="text-center d-none d-md-inline"><button class="btn rounded-circle border-0" id="sidebarToggle" type="button"></button></div>
            </div>
        </nav>
        <div class="d-flex flex-column" id="content-wrapper">
            <div id="content">
                <nav class="navbar navbar-light navbar-expand bg-white shadow mb-4 topbar static-top">
                    <div class="container-fluid"><button class="btn btn-link d-md-none rounded-circle mr-3" id="sidebarToggleTop" type="button"><i class="fas fa-bars"></i></button>
                        <ul class="nav navbar-nav flex-nowrap ml-auto">
                            <div class="d-none d-sm-block topbar-divider"></div>
                            <li class="nav-item dropdown no-arrow" role="presentation">
                            	<%if (request.getAttribute("user.name")!= null) { 
                            	 System.out.println(request.getAttribute("user.name"));%>
                                <div class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="d-none d-lg-inline mr-2 text-gray-600 small"><%= request.getAttribute("user.name") %></span></a>
                                 <%} %>
                                    <div class="dropdown-menu shadow dropdown-menu-right animated--grow-in"
                                        role="menu"><a class="dropdown-item" role="presentation" href="profile"><i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Perfil</a>
                                        <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="logout"><i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Cerrar Sesión</a></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
                <div class="container-fluid">
                    <h3 class="text-dark mb-4">Perfil de Usuario</h3>
                    <div class="row mb-3">
                        <div class="col-lg-8">
                            <div class="row">
                                <div class="col">
                                    <div class="card shadow mb-3">
                                        <div class="card-header py-3">
                                            <p class="text-primary m-0 font-weight-bold">Configuración de Usuario</p>
                                        </div>
                                        <div class="card-body">
                                            <form class="user" method="post" action="profile">
                                                <div class="form-row">
                                                    <div class="col">
                                                        <div class="form-group"><label for="username"><strong>Nombre de Usuario</strong></label><input class="form-control" type="text" value='<%=request.getAttribute("user.name")%>' name="username" patter="[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*" title="Nombre de la persona (Ej: Pepe)"></div>
                                                    </div>
                                                </div>
                                                <div class="form-row">
                                                    <div class="col">
                                                        <div class="form-group"><label for="email"><strong>Correo Electrónico</strong></label><input class="form-control" type="email" value="<%=request.getAttribute("actualMail")%>" name="email" readonly></div>
                                                    </div>
                                                    <div class="col">
                                                        <div class="form-group"><label for="numero_telefono"><strong>Número de Teléfono</strong></label><input class="form-control" type="number" pattern="[0-9]{9}" title="Número de teléfono (Ej: 666666666)" value="<%=request.getAttribute("numero_telefono")%>" name="numero_telefono"></div>
                                                    </div>
                                                </div>
                                                <div class="form-group"><button class="btn btn-primary" type="submit">Guardar Cambios</button></div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
							<div class="row mb-3">
								<div>
									<div class="form-inline  mx-3">
										<div class="form-row form-row-lg">
											<a class="btn btn-danger mx-3" role="button" href="delete">Eliminar Cuenta</a>
											<a class="btn btn-info mx-3" role="button" href="download">Descargar información</a>
										</div>
									</div>
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
</body>

</html>
