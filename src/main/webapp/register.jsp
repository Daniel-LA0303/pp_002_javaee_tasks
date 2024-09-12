<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css">
<script src="${pageContext.request.contextPath}/js/jquerymin.js"></script>
</head>
<body>
	<div class="background">
        <div class="shape"></div>
        <div class="shape"></div>
    </div>
    <form>
        <h3>Register Here</h3>

        <label for="username">Username</label>
        <input type="text" placeholder="Email or Phone" id="username">

        <label for="email">Email</label>
        <input type="email" placeholder="example@example.com" id="email">
        
        <label for="password">Password</label>
        <input type="password" placeholder="Password" id="password">
        
        <button id="submitLogin" type="button">Register</button>
        <a href="${pageContext.request.contextPath}/login">Login</a>
    </form>
    
    <script type="text/javascript">
	    $(document).ready(function () {
	
	        $("#submitLogin").click(function (event) {
	
	            event.preventDefault();
	
	            var username = $("#username").val();
	            var password = $("#password").val();
	            var email = $("#email").val();
	
	            $.ajax({
	                url: "${pageContext.request.contextPath}/register",  // URL del Servlet
	                type: "POST",  // Enviamos los datos usando POST
	                data: {
	                    username: username,
	                    password: password,
	                    email: email
	                },
	                success: function (response) {
	                    // Aquí manejas la respuesta del servlet (por ejemplo, redirigir a una página o mostrar un mensaje)
	                    console.log("Registro exitoso:", response);
	                    window.location.href = "${pageContext.request.contextPath}/principal.html";
	                },
	                error: function (xhr, status, error) {
	                    // Manejo de errores
	                    console.error("Error en el registro:", error);
	                }
	            });
	        });
	    });
    </script>
</body>
</html>