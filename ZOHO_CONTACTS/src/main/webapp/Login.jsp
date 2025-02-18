<%@page import="com.zohocontacts.dboperation.SessionOperation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>



<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>



<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap"
	rel="stylesheet">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
	integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />

<style>
body {
	font-family: Arial, sans-serif;
	background-color: #F8F1F1;
	display: flex;
	color: white;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
}

.container {
	background-color: #025464;
	color: white;
	padding: 65px;
	border-radius: 45px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 300px;
	display: flex;
	flex-direction: column;
	align-items: center;
}

h2 {
	margin-bottom: 20px;
}

label {
	margin-top: 10px;
	font-weight: bold;
	display: block;
}

input[type="text"], input[type="password"] {
	width: 230px;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-top: 5px;
}

input[type="submit"] {
	padding: 10px;
	background-color: #DDA853;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	width: 100%;
	margin-top: 32px;
	font-size: 16px;
}

.back-btn {
	background-color: #ccc;
	margin-top: 10px;
}

.back-btn:hover {
	background-color: #bbb;
}

#prompt, #prompt1 {
	display: none;
	color: white;
	font-size: 12px;
	margin-top: 5px;
}

section {
	display: flex;
	justify-content: space-between;
	align-items: center;
	a
	{
	color
	:
	white;
}
}
</style>




</head>
<body>
	<%
	String sessionid = SessionOperation.getCustomSessionId(request.getCookies());
	if (sessionid != null) {

		response.sendRedirect("home.jsp");
	}
	%>
	<div class="container">
		<h2>Login</h2>
		<form action="/login" method="post">
			<label for="username">Username</label> <input type="text" id="user"
				name="username" autocomplete="off" required /> <span id="prompt1">
				do not use special character for username</span> <label for="password">Password</label>
			<input type="password" id="pass" name="password" autocomplete="off"
				required /> <span id="prompt"> Password must be 8 - 72
				characters </span>
			<div class="login_button">
				<input type="submit" value="Login" />

			</div>
		</form>

		<section id="loginfooter">
			<p>don't have an account?</p>
			<a href="Signup.jsp">Create account</a>
		</section>


	</div>

	
	<% if (request.getAttribute("errorMessage") != null) { %>
    <script type="text/javascript">
        alert("<%= request.getAttribute("errorMessage") %>");
    </script>
<% } %>

	<script>

  


	
		const password = document.getElementById('pass');
		const prompt = document.getElementById('prompt');

		password.addEventListener('focus', function() {
			prompt.style.display = 'block';
		});

		password.addEventListener('blur', function() {
			prompt.style.display = 'none';
		});

		const user = document.getElementById('user');
		const prompt1 = document.getElementById('prompt1');

		user.addEventListener('focus', function() {
			prompt1.style.display = 'block';
		});

		user.addEventListener('blur', function() {
			prompt1.style.display = 'none';
		});
	</script>
</body>
</html>
