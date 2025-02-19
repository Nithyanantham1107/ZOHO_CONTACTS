<%@page import="com.zohocontacts.sessionstorage.ThreadLocalStorage"%>
<%@page import="com.zohocontacts.dbpojo.EmailUser"%>
<%@page import="com.zohocontacts.dboperation.SessionOperation"%>
<%@page import="com.zohocontacts.dbpojo.ContactDetails"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.zohocontacts.dboperation.UserContactOperation"%>
<%@page import="com.zohocontacts.dbpojo.UserData"%>
<%@page import="com.zohocontacts.sessionstorage.CacheModel"%>
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
	width: 600px;
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
	background-color: white;
	color: black;
	diplay: flex;
	justify-content: space-between;
	margin-bottom: 20px;
}

label {
	background-color: white;
	color: black;
	font-weight: bold;
	display: block;
	margin-bottom: 8px;
}

input[type="text"], input[type="email"], input[type="tel"], input[type="password"],
	select {
	width: 100%;
	background-color: white;
	color: black;
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

<%
if (request.getAttribute("errorMessage") != null) {
%>
<script type="text/javascript">
        alert("<%=request.getAttribute("errorMessage")%>");
    </script>
<%
}
%>
</head>
<body>




	<%
	CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
	if (cacheModel == null || cacheModel.getUserData() == null) {

		System.out.println("hello hi");
		response.sendRedirect("Login.jsp");
		return;

	}

	UserData ud = cacheModel.getUserData();
	%>

	<div id="header">



		<a href="#"> <img src="asset/contactIcon.png" class="logo" alt="">
		</a>


		<div>


			<ul id="navbar">
				<li><a href="home.jsp">Contacts</a></li>
				<li><a href="groups.jsp">Groups</a></li>
				<li><a href="profile.jsp">Profile</a></li>
				
				<li><a href="emails.jsp"> emails</a></li>
				<li><a href="changePassword.jsp"> change Password</a></li>
				<li><a href="/logout"> <i
						class="fa-solid fa-arrow-right-from-bracket"></i>
				</a></li>

			</ul>

		</div>



	</div>


	<div id="main">







		<div class="form-container">



			<form action="/user" method="post" style="background-color: white;">
				<label for="Addemail">Enter email to Add</label> <input
					type="hidden" value="addemail" name="action" /> <input type="text"
					name="newemail" placeholder="Enter Email to add">

				<button type="submit" class="glowgreenbutton">Add email</button>


			</form>



			<section id="table">

				<%
				if (ud.getallemail() != null && ud.getallemail().size() > 0) {
				%>

				<table>

					<thead>


						<tr>
							<th>Email</th>

							<th>Delete</th>


						</tr>

					</thead>
					<tbody>

						<%
						if (ud.getallemail() != null && ud.getallemail().size() > 0) {

							for (EmailUser data : ud.getallemail()) {

								if (!data.getIsPrimary()) {
						%>




						<tr>


							<td><%=data.getEmail()%></td>





							<td>
								<form action="/user" method="post">

									<input type="hidden" value="deleteemail" name="action" /> <input
										type="hidden" value="<%=data.getID()%>" name="emailID" /> <input
										type="submit" class="glowredbutton" value="Delete" />
								</form>
							</td>
						</tr>

						<%
						}

						}
						}
						%>



					</tbody>

				</table>


				<%
				}
				%>


			</section>




		</div>
	</div>

















</body>
</html>