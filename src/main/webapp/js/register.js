$(document).ready(function(){
	
	$("#submitLogin").click(function(event){
		
		event.preventDefault();
		
		var username = $("#username").val();
		
		var password = $("#password").val();
		
		var email = $("#email").val();
		
		
		console.log("username: ", username);
		console.log("password: ", password);
		console.log("email: ", email);
		
		$.ajax({
			url: "http://localhost:8080/app-pm/register",  // URL del Servlet
			type: "POST",  // Enviamos los datos usando POST
			data: {	
				username: username,
				password: password,
				email: email
			},
			success: function (response) {
			                
				console.log("Registro exitoso:", response);
			},
			error: function (xhr, status, error) {
			                // Manejo de errores
							
				console.error("Error en el registro:", error);
			}
		});
	});
	
});
