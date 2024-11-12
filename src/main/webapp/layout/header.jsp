<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta charset="UTF-8">
	<link rel="icon" href="${pageContext.request.contextPath}/img/icon.png" type="image/x-icon">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
 	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/global.css">
 	<script src="${pageContext.request.contextPath}/js/jquerymin.js"></script>
  	<script src="${pageContext.request.contextPath}/js/utils.js"></script>
 	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js" integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+" crossorigin="anonymous"></script>
 	<title>Project Management</title>
</head>
<body>
	<nav class="bg-dark-nav navbar navbar-expand-lg bg-body-tertiary z-3">
	  <div class="container-fluid">
	    <a class="navbar-brand" href="#">Navbar</a>
	    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
	      <span class="navbar-toggler-icon"></span>
	    </button>
	    <div class="collapse navbar-collapse" id="navbarSupportedContent">
	      <ul class="navbar-nav me-auto mb-2 mb-lg-0" id="nav">
	        <li class="nav-item">
	          <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/home">Home</a>
	        </li>
	      </ul>
			<div id="authButtonContainer" class="auth-details">
        	</div>
	    </div>
	  </div>
	</nav>
	<div class="container">
	<script>
	 	const email = getCookie('email');
	    console.log("Email:", email);
	    const authButtonContainer = document.getElementById('authButtonContainer');
	    const nav = document.getElementById('nav');
	    if (email) {

	        authButtonContainer.innerHTML = '<p class="text-black mr-2">Welcome: ' + email + '</p>' +
	        '<div><a id="logoutButton" href="#" class="btn btn-outline-danger">Logout</a></div>' ;
	        
	        nav.innerHTML += '<li class="nav-item"><a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/principal">Projects</a></li>'

	        document.getElementById('logoutButton').addEventListener('click', function(event) {
	          event.preventDefault(); 
	          console.log("exit");

	          $.ajax({
	            url: '${pageContext.request.contextPath}/logout',
	            type: 'GET',
	            success: function() {
	              window.location.href = '${pageContext.request.contextPath}/login.html';
	            },
	            error: function(xhr, status, error) {
	              console.error("Error en el cierre de sesión:", error);
	            }
	          });
	        });
	      } else {
	        authButtonContainer.innerHTML = '<a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary">Login</a>';
	      }
	</script>
	</script>
	