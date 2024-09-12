<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css">
<script src="${pageContext.request.contextPath}/js/jquerymin.js"></script>
<title>Insert title here</title>
</head>
<body>
	<h1>Hola</h1>
	
	<button id="logoutButton">Log Out</button>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#logoutButton').click(function() {
            	
            	console.log("exit");
                $.ajax({
                    url: '${pageContext.request.contextPath}/logout',
                    type: 'GET',
                    success: function() {
                        // Redirigir a la página de inicio o de login después de cerrar sesión
                        window.location.href = '${pageContext.request.contextPath}/login.html';
                    },
                    error: function(xhr, status, error) {
                        // Manejo de errores
                        console.error("Error en el cierre de sesión:", error);
                    }
                });
            });
        });
    </script>
</body>
</html>