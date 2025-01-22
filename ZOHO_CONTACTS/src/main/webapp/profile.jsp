<%@page import="dbpojo.EmailUser"%>
<%@page import="dboperation.SessionOperation"%>
<%@page import="dbpojo.ContactDetails"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dboperation.UserContactOperation"%>
<%@page import="dbpojo.Userdata"%>
<%@page import="sessionstorage.CacheModel"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width , initial-scale=1.0" />
<title>ZOHO CONTACTS</title>

<link rel="stylesheet" href="css/styles.css" />
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap"
	rel="stylesheet">

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
	integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
	crossorigin="anonymous" referrerpolicy="no-referrer" />


<script>
        <%if (request.getAttribute("errorMessage") != null) {%>
            alert("<%=request.getAttribute("errorMessage")%>
	");
<%}%>
	
</script>


<style type="text/css">
.form-container {
	max-width: 600px;
	margin: auto;
	padding: 20px;
	background-color: white;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
	text-align: center;
	font-size: 24px;
	margin-bottom: 10px;
}

p {
	text-align: center;
	font-size: 14px;
	margin-bottom: 20px;
}

section {
	diplay: flex;
	justify-content: space-between;
	margin-bottom: 20px;
}

label {
	font-weight: bold;
	display: block;
	margin-bottom: 8px;
}

input[type="text"], input[type="email"], input[type="tel"], input[type="password"],
	select {
	width: 100%;
	padding: 10px;
	margin-bottom: 12px;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 14px;
}

input[type="checkbox"], input[type="radio"] {
	margin-right: 10px;
}

.name-inputs {
	display: flex;
	gap: 10px;
}

button {
	width: 100%;
	padding: 12px;
	background-color: #4CAF50;
	color: white;
	border: none;
	border-radius: 4px;
	font-size: 16px;
	cursor: pointer;
	margin-top: 10px;
}

a {
	color: #4CAF50;
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

.radio-buttons {
	display: flex;
	gap: 10px;
}

@media ( max-width : 600px) {
	.name-inputs {
		flex-direction: column;
	}
	input[type="text"], input[type="email"], input[type="tel"], input[type="password"],
		select {
		font-size: 16px;
	}
}
</style>
</head>
<body>




	<%
	SessionOperation so = new SessionOperation();
	CacheModel cachemodel = so.checkSessionAlive(so.getCustomSessionId(request.getCookies()));

	if (cachemodel == null) {
		System.out.println("hello hi");
		response.sendRedirect("index.jsp");
		return;

	}

	Userdata ud = cachemodel.getUserData();
	%>

	<section id="header">



		<a href="#"> <img src="asset/contactIcon.png" class="logo" alt="">
		</a>


		<div>


			<ul id="navbar">
				<li><a href="home.jsp">Contacts</a></li>
				<li><a href="groups.jsp">Groups</a></li>
				<li><a href="profile.jsp">Profile</a></li>
				<li><a href="changePassword.jsp"> ChangePassword</a></li>
				<li><a href="/login"> <i
						class="fa-solid fa-arrow-right-from-bracket"></i>
				</a></li>

			</ul>

		</div>



	</section>


	<section id="main">

		<div class="form-container">
			<form action="/userupdate" method="POST" id="registration-form">

				<section>

					<label for="first-name">Full Name</label> <input type="text"
						id="first-name" name="name" value="<%=ud.getName()%>" required>
					<label for="phone">Phone Number</label> <input type="tel"
						id="phone" name="phone" value="<%=ud.getPhoneno()%>" required>
				</section>



				<section>

					<label for="primary">primary email</label> <select
						name="primaryemail" required>


						<%
						int primary=-1;
						if (ud.getallemail() != null && ud.getallemail().size() > 0) {

							for (EmailUser data : ud.getallemail()) {

								if (data.getIsPrimary()) {
									primary=data.getID();
						%>
						<option value="<%=data.getEmail()%>" selected><%=data.getEmail()%></option>


						<%
						} else {
						%>

						<option value="<%=data.getEmail()%>"><%=data.getEmail()%></option>

						<%
						}

						}
						}
						%>
						


					</select>
				</section>

				<section>

					<label for="timezone">Timezone</label> <select name="timezone"
						id="timezone" required>
						<option value="<%=ud.getTimezone()%>" selected><%=ud.getTimezone()%></option>
						<option value="Asia/Kolkata">Asia/Kolkata (Indian
							Standard Time)</option>
						<option value="America/New_York">America/New_York
							(Eastern Time)</option>
						<option value="America/Chicago">America/Chicago (Central
							Time)</option>
						<option value="America/Denver">America/Denver (Mountain
							Time)</option>


					</select>
				</section>


				<section>

					<label for="address">Street Address</label> <input type="text"
						id="address" name="address" value="<%=ud.getAddress()%>" required>


				</section>
				
				
				
					<section>

					<label for="username">UserName</label> <input type="text"
						 name="username" value="<%=ud.getLoginCredentials().getUserName()%>" required>
 <input type="hidden"
						 name="emailID" value="<%=primary%>" required>
						  <input type="hidden"
						 name="logID" value="<%=ud.getLoginCredentials().getID()%>" required>

				</section>

				<button type="submit" class="glowbutton">Update</button>
			</form>


		</div>
	</section>


















</body>
</html>