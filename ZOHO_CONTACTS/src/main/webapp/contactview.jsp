<%@page import="com.zohocontacts.sessionstorage.ThreadLocalStorage"%>
<%@page import="com.zohocontacts.dbpojo.ContactPhone"%>
<%@page import="com.zohocontacts.dbpojo.ContactMail"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.time.Instant"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.zohocontacts.dbpojo.ContactDetails"%>
<%@page import="com.zohocontacts.sessionstorage.CacheModel"%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.zohocontacts.dboperation.UserGroupOperation"%>
<%@page import="com.zohocontacts.dboperation.UserContactOperation"%>
<%@page import="com.zohocontacts.dboperation.UserOperation"%>
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
<title>Update Contact</title>

<link rel="stylesheet" href="css/styles.css" />


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


<style>
body {
	font-family: Arial, sans-serif;
	color: white;
	background-color: #FBF5E5;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
}

.contactfooter {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
	background-color: white;
}

.container {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 550px; form { display : flex;
	flex-direction: column;
	justify-content: center;
	background-color: white;
}

}
h1 {
	background-color: white;
	color: black;
	text-align: center;
	margin-bottom: 20px;
	color: #333;
}

div, span {
	background-color: white;
	width: 100%;
}

label {
	background-color: white;
	color: black;
	margin-top: 10px;
	font-weight: bold;
	display: block;
}

input[type="text"], input[type="email"], textarea {
	width: calc(100% - 20px);
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-top: 5px;
	margin-bottom: 5px;
	background-color: white;
	color: black;
}

textarea {
	resize: none; /* Prevent resizing */
}

.updatelogobutton{
i{
background-color:white;
	color:#0b57d0;
}

	
	border: none;

	background-color: white;
	font-size: 20px;


}

.deletelogobutton{
i{
background-color:white;
	color:#ff0000;
}

	
	border: none;

	background-color: white;
	font-size: 20px;


}
.back-btn {
	padding: 10px;
	background-color: #dda853;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	width: 100px;
	margin-top: 15px;
	font-size: 16px;
	align-self: center;
}


</style>

	<% if (request.getAttribute("errorMessage") != null) { %>
    <script type="text/javascript">
        alert("<%= request.getAttribute("errorMessage") %>");
    </script>
<% } %>

</head>
<body>

	<%
	CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
	if (cacheModel == null || cacheModel.getUserData() == null) {

		response.sendRedirect("Login.jsp");

		return;

	}

	ContactDetails uc = (ContactDetails) request.getAttribute("user_spec_contact");
	if (uc == null) {
		response.sendRedirect("home.jsp");
		return;
	}

	Instant instant = Instant.ofEpochMilli(uc.getModifiedAt());

	LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a");
	String lastModified = dateTime.format(formatter);

	instant = Instant.ofEpochMilli(uc.getModifiedAt());
	dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

	formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a");

	String createdAt = dateTime.format(formatter);

	System.out.println("hey" + uc.getFirstName() + uc.getID());
	%>

	<div class="container">
	<section class="contactfooter">

		
				<form action="/contact" method="post">
					<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
					<input type="hidden" value="specificContact" name="action" />
								
					<button type="submit" class="updatelogobutton" >
					<i class="fa-solid fa-pen-to-square"></i>
					</button>

				</form>
		
			
			
			<div>
			
			<h1>
			Contact
			</h1>
			</div>
			
				<form action="/contact" method="post">
					<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
			
			
								<input type="hidden" value="deletecontact" name="action" />
								
					
					<button type="submit" class="deletelogobutton" >
				<i class="fa-regular fa-trash-can"></i>	</button>
					
						</form>


		</section>

		<div>
			<label> First Name </label> <span> <%=uc.getFirstName()%>

			</span>
		</div>

		<div>
			<label> Middle Name </label> <span> <%=uc.getMiddleName()%>

			</span>
		</div>


		<div>
			<label> Last Name </label> <span> <%=uc.getLastName()%>

			</span>
		</div>


		<%
		if (uc.getAllContactMail() != null && uc.getAllContactMail().size() > 0) {
		%>

		<label> Mail </label>


		<%
		for (ContactMail mail : uc.getAllContactMail()) {
		%>


		<div>
			<span> <%=mail.getContactMailID()%>

			</span>


		</div>

		<%
		}
		}
		%>


		<%
		if (uc.getAllContactphone() != null && uc.getAllContactphone().size() > 0) {
		%>

		<label> Phone No </label>
		<%
		for (ContactPhone phone : uc.getAllContactphone()) {
		%>


		<div>
			<span> <%=phone.getContactPhone()%>

			</span>


		</div>

		<%
		}
		}
		%>


		<div>
			<label> Gender </label> <span> <%=uc.getGender()%>

			</span>
		</div>

		<div>
			<label> Address </label> <span> <%=uc.getAddress()%>

			</span>
		</div>
		<div>
			<label> Last Modified </label> <span> <%=lastModified%>

			</span>
		</div>


		<div>
			<label> CreatedAt </label> <span> <%=createdAt%>

			</span>
		</div>
		




<form action="/home.jsp" method="get">
			<input type="submit" value="Back" class="back-btn" />
		</form>


	</div>


	

</body>
</html>