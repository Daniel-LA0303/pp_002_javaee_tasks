$(document).ready(function(){
	
	$("#submitLogin").click(function(event){
		
		event.preventDefault();
		
		var username = $("#username").val();
		
		var password = $("#password").val();
		
		var email = $("#email").val();
		
		
		console.log("username: ", username);
		
		console.log("password: ", password);
		
		console.log("email: ", email);
	});
	
});
