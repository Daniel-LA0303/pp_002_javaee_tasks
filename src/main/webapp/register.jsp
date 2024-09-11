<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
    
    <script src="${pageContext.request.contextPath}/js/register.js"></script>
</body>
</html>