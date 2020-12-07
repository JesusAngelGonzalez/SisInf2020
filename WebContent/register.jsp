<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
    <title>Registrarse - CovidFree</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i">
    <link rel="stylesheet" href="assets/fonts/fontawesome-all.min.css">
    <link rel="stylesheet" href="assets/fonts/font-awesome.min.css">
    <link rel="stylesheet" href="assets/fonts/fontawesome5-overrides.min.css">
    <link rel="stylesheet" href="assets/css/Features-Clean.css">
    <link rel="stylesheet" href="assets/css/Highlight-Blue.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css">
    <link rel="stylesheet" href="assets/css/Navigation-with-Button.css">
	
	<script type="text/javascript">
		var check = function() {
			if (document.getElementById('PasswordInput').value != document.getElementById('RepeatPasswordInput').value) {
				document.getElementById('message-error-repeat').style.visibility = 'visible';
			}else{
				document.getElementById('message-error-repeat').style.visibility = 'hidden';
			}
		}
	</script>
</head>

<body class="bg-gradient-primary">
    <div class="container">
        <div class="card shadow-lg o-hidden border-0 my-5">
            <div class="card-body p-0">
                <div class="row">
                    <div class="col-lg-5 d-none d-lg-flex">
                        <div class="flex-grow-1 bg-register-image" style="background-image: url(&quot;assets/img/logo.png&quot;);"></div>
                    </div>
                    <div class="col-lg-7">
                        <div class="p-5">
                            <div class="text-center">
                                <h4 class="text-dark mb-4">Crear una cuenta</h4>
                            </div>
                            <form class="user" method="post" action="register">
                                <div class="form-group"><input class="form-control form-control-user" type="text" id="Usuario" placeholder="Nombre de Usuario" name="user" required></div>
                                <div class="form-group"><input class="form-control form-control-user" type="email" id="InputEmail" aria-describedby="emailHelp" placeholder="Correo Electrónico" name="email" required="" pattern="[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*">
                                    <% if (request.getAttribute("errorCorreo") != null) { %> 
                                    <div class="alert alert-danger bounce animated" role="alert">
                                    <span><strong><%= request.getAttribute("errorCorreo") %></strong></span>
                                    </div>
                                    <% } %>
                                </div>
                                <div class="form-group"><input class="form-control form-control-user" type="text" id="Usuario" placeholder="Teléfono (ej: 666 66 66 66)" name="telefono" required="" pattern="[0-9]{9}"></div>
                                <div class="form-group row">
                                    <div class="col-sm-6 mb-3 mb-sm-0"><input class="form-control form-control-user" type="password" id="PasswordInput" placeholder="Contraseña" name="password" required="" minlength="8" maxlength="20" pattern="^(?=\w*\d)(?=\w*[A-Z])(?=\w*[a-z])\S{8,20}$">
                                        <small
                                            class="form-text text-muted">Longitud entre 8 y 20 caracteres. Debe contener al menos una mayuscula, una minúscula y un dígito</small>
                                    </div>
                                    <div class="col-sm-6"><input class="form-control form-control-user" type="password" id="RepeatPasswordInput" placeholder="Repita la Contraseña" name="password_repeat" required=""  onkeyup='check();'>
                                        <div class="alert alert-danger bounce animated" id="message-error-repeat" role="alert" style="visibility: hidden;"><span><strong>No coincide la contraseña</strong>.</span></div>
                                    </div>
                                </div><button class="btn btn-primary btn-block text-white btn-user" type="submit">Registrarse</button>
                                <hr>
                            </form>
                            <!-- NO IMPLEMENTADO - NO PRESUPUESTO <div class="text-center"><a class="small" href="forgot-password.jsp">¿Has olvidado la contraseña?</a></div> -->
                            <div class="text-center"><a class="small" href="login">¿Ya tienes una cuenta? No tardes en Iniciar Sesión</a></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="assets/js/jquery.min.js"></script>
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.4.1/jquery.easing.js"></script>
    <script src="assets/js/theme.js"></script>
</body>

</html>
