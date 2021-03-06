<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>A�adir Lugar - CovidFree</title>
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
                    <li class="nav-item" role="presentation"><a class="nav-link active" href="newPlace"><i class="far fa-building"></i><span>A�adir Lugar</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link" href="ranking"><i class="fas fa-table"></i><span>Ranking</span></a></li>
                    <li class="nav-item" role="presentation"><a class="nav-link" href="profile"><i class="fas fa-user"></i><span>Perfil de Usuario</span></a></li>
                </ul>
                <div class="text-center d-none d-md-inline"><button class="btn rounded-circle border-0" id="sidebarToggle" type="button"></button></div>
            </div>
        </nav>
        <div class="d-flex flex-column" id="content-wrapper">
            <div id="content">
                <nav class="navbar navbar-light navbar-expand bg-white shadow mb-4 topbar static-top">
                    <div class="container-fluid">
                        <ul class="nav navbar-nav flex-nowrap ml-auto">
                            <div class="d-none d-sm-block topbar-divider"></div>
                            <li class="nav-item dropdown no-arrow" role="presentation">
                            	<%if (request.getAttribute("userName")!= null) { 
                            	 System.out.println(request.getAttribute("userName"));%>
                                <div class="nav-item dropdown no-arrow"><a class="dropdown-toggle nav-link" data-toggle="dropdown" aria-expanded="false" href="#"><span class="d-none d-lg-inline mr-2 text-gray-600 small"><%= request.getAttribute("userName") %></span></a>
                                <%}%>
                                    <div class="dropdown-menu shadow dropdown-menu-right animated--grow-in"
                                        role="menu"><a class="dropdown-item" role="presentation" href="profile"><i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Perfil</a>
                                        <div class="dropdown-divider"></div><a class="dropdown-item" role="presentation" href="logout"><i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>&nbsp;Cerrar Sesi�n</a></div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
                <div class="container-fluid">
                    <div class="text-center">
                        <h4 class="text-dark mb-4">A�adir un lugar</h4>
                    </div>
                    <form class="user" method="post" action="newPlace">
                        <div class="form-group"><label for="Lugar">Lugar</label><input class="form-control" type="text" name="Lugar" value="" placeholder="Lugar"></div>
                        <div class="form-group"><label for="Localizacion">Localizaci�n</label><input class="form-control" type="text" name="Localizacion" value="" placeholder="Localizacion">
                            <% if( request.getAttribute("wrongDir") != null ){ %>
                            <div class="alert alert-danger" role="alert"><span><strong>No existe este lugar</strong></span></div>
                        	<%} %>
                        </div>
                        <div class="form-group">
                            <div class="form-row">
                                <div class="col"><label>Llegada</label><input class="form-control" type="text" required pattern="[0-9]{4}-(0[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01]?[0-9]):[0-5][0-9]" placeholder="a�o-mes-dia HH:MM" title="a�o-mes-dia HH:MM (24h)" name="Inicio"></div>
                                <div class="col"><label>Salida</label><input class="form-control" type="text" required pattern="[0-9]{4}-(0[1-9]|1[0-2])-(0?[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01]?[0-9]):[0-5][0-9]" placeholder ="a�o-mes-dia HH:MM" title="a�o-mes-dia HH:MM (24h)" name="Fin"></div>
                            </div>
                        </div>
                        <div class="form-group"><label for="Lugar">Informaci�n Extra</label><input class="form-control" type="text" name="Extra" value="" placeholder="Lugar"></div>
                        <div class="form-row">
                            <div class="col"><button class="btn btn-primary" type="submit">Guardar Cambios</button></div>
                            <div class="col"><button class="btn btn-secondary" type="reset">Borrar Todo</button></div>
                        </div>
                        <hr>
                    </form>
                </div>
            </div>
            <footer class="bg-white sticky-footer">
                <div class="container my-auto">
                    <div class="text-center my-auto copyright"><span>Copyright � CovidFree 2020</span></div>
                </div>
            </footer>
        </div><a class="border rounded d-inline scroll-to-top" href="#page-top"><i class="fas fa-angle-up"></i></a></div>
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.js"></script>
    <script src="assets/js/theme.js"></script>
</body>

</html>
