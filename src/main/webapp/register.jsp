<jsp:include page="layout/header.jsp"/>
	<div class="background">
        <div class="shape"></div>
        <div class="shape"></div>
    </div>
    <form class="form-auth">
    
        <h3>Register Here</h3>

        <label class="form-auth-label" for="username">Username</label>
        <input class="form-auth-input" type="text" placeholder="Email or Phone" id="username">

        <label class="form-auth-label" for="email">Email</label>
        <input class="form-auth-input" type="email" placeholder="example@example.com" id="email">
        
        <label class="form-auth-label" for="password">Password</label>
        <input class="form-auth-input" type="password" placeholder="Password" id="password">
        
        <button class="form-auth-btn" id="submitLogin" type="button">Register</button>
        <a href="${pageContext.request.contextPath}/login">Login</a>
        
    </form>
    
    <script type="text/javascript">
	    $(document).ready(function () {
	        $("#submitLogin").click(function (event) {
	            event.preventDefault();
	
	            var username = $("#username").val();
	            var password = $("#password").val();
	            var email = $("#email").val();
	
	            $(".text-danger").remove();
	
	            $.ajax({
	                url: "/app-pm/register",
	                type: "POST",
	                contentType: "application/x-www-form-urlencoded",
	                data: {
	                    username: username,
	                    password: password,
	                    email: email
	                },
	                success: function (response) {
	                    console.log("Registro exitoso:", response);
	                    window.location.href = "/app-pm/principal.html"; 
	                },
	                error: function (xhr) {
	                    if (xhr.status === 400) { 
	                    	console.log(xhr.status);
	                        var errors = JSON.parse(xhr.responseText);
	                        
	                        if (errors.username) {
	                            $("#username").before('<p class="text-danger">' + errors.username + '</p>');
	                        }
	                        if (errors.email) {
	                            $("#email").before('<p class="text-danger">' + errors.email + '</p>');
	                        }
	                        if (errors.password) {
	                            $("#password").before('<p class="text-danger">' + errors.password + '</p>');
	                        }
	                    }else if (xhr.status === 409) {                       
	                        $(".text-danger").remove();
	
	                        $("#email").after('<p class="text-danger">This email already exists</p>');
	                    } else {
	                        console.error("Error en el registro:", xhr.responseText);
	                    }
	                }
	            });
	        });
	    });
	</script>
<jsp:include page="layout/footer.jsp"/>