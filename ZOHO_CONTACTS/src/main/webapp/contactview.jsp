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

.back-btn {
	padding: 10px;
	background-color: black;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	width: 100%;
	margin-top: 15px;
	font-size: 16px;
}

.back-btn:hover {
	background-color: white;
	color: black;
}
</style>



</head>
<body>

	<%
	CacheModel alive = SessionOperation.checkSessionAlive(SessionOperation.getCustomSessionId(request.getCookies()));

	if (alive == null) {

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
		<div>

			<h1>Contact</h1>
		</div>


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
		<section class="contactfooter">

			<section class="contactbutton">
				<form  action="/GetAndUpdatecontact" method="post">
					<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
					<input type="submit" class="glowyellowbutton" value="Update" />
				</form>
			</section>
			<section class="contactbutton">
				<form  action="/deletecontacts" method="post">
					<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
					<input type="submit" class="glowredbutton" value="Delete" />
				</form>

			</section>

		</section>







	</div>

       
<script type="text/javascript" src="js/contactdelete.js">
        <%if (request.getAttribute("errorMessage") != null) {%>
            alert("<%=request.getAttribute("errorMessage")%>
	");
<%}%>
	
</script>

</body>
</html>