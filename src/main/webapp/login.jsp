<jsp:include page="layout/header.jsp"/>

	<div class="background">
        <div class="shape"></div>
        <div class="shape"></div>
    </div>
    <form class="form-auth">
    
        <h3>Login Here</h3>

        <label class="form-auth-label" for="username">Email</label>
        <input class="form-auth-input" type="text" placeholder="example@emaple.com" id="email">

        <label class="form-auth-label" for="password">Password</label>
        <input class="form-auth-input" type="password" placeholder="Password" id="password">

        <button class="form-auth-btn" id="submitLogin" type="button" >Log In</button>
        
        <a href="${pageContext.request.contextPath}/register">Register</a>
        
    </form>
    
    <script type="text/javascript">
	    $(document).ready(function () {
	        $("#submitLogin").click(function (event) {
	            event.preventDefault();
	
	            var password = $("#password").val();
	            var email = $("#email").val();
	
	            $(".text-danger").remove();
				
	            $.ajax({
	                url: "/app-pm/login",
	                type: "POST",
	                contentType: "application/x-www-form-urlencoded",
	                data: {
	                    password: password,
	                    email: email
	                },
	                success: function (response) {
	                    console.log("login exitoso:", response);
	                    window.location.href = "/app-pm/principal.html"; 
	                },
	                error: function (xhr) {
	                    if (xhr.status === 400) { 
	                    	console.log(xhr.status);
	                    	console.log(errors);
	                        var errors = JSON.parse(xhr.responseText);
	                        
	                        if (errors.email) {
	                            $("#email").after('<p class="text-danger">' + errors.email + '</p>');
	                        }
	                        if (errors.password) {
	                            $("#password").after('<p class="text-danger">' + errors.password + '</p>');
	                        }
	                    }else if (xhr.status === 404) {                       
	                        $(".text-danger").remove();
	
	                        $("#email").after('<p class="text-danger">This email do not exists</p>');
	                    } else if(xhr.status === 401){
	                    	$(".text-danger").remove();
	
	                        $("#password").after('<p class="text-danger">Incorrect password</p>');
	                    }else{
	                        console.error("Error en el registro:", xhr.responseText);
	                    }
	                }
	            });
	            
	        });
	    });
	</script>
<jsp:include page="layout/footer.jsp"/>