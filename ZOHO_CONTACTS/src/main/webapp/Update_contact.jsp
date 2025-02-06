<%@page import="dbpojo.ContactDetails"%>
<%@page import="sessionstorage.CacheModel"%>
<%@page import="dbmodel.UserGroup"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dboperation.UserGroupOperation"%>
<%@page import="dboperation.UserContactOperation"%>
<%@page import="dboperation.UserOperation"%>
<%@page import="dboperation.SessionOperation"%>
<%@page import="dbmodel.UserContacts"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="dbmodel.UserData,javax.servlet.http.HttpSession"%>


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
  color:white;
	background-color:#272727;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
}

.container {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 350px;
	
	
	 form{
            display:flex;
            flex-direction:column;
            justify-content:center;
            
            background-color:white;
            }
}

h2 {
 background-color:white;
          color:black;
	text-align: center;
	margin-bottom: 20px;
	color: #333;
}

label {

 background-color:white;
          color:black;
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
	margin-bottom:5px;
	 background-color:white;
          color:black;
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

<script>
        <%if (request.getAttribute("errorMessage") != null) {%>
            alert("<%=request.getAttribute("errorMessage")%>
	");
<%}%>
	
</script>

</head>
<body>



	<div class="container">
		<h2>Update Contacts</h2>
		<form action="/updatecontact" method="post">
			<%
			
			
			   CacheModel alive =  SessionOperation.checkSessionAlive( SessionOperation.getCustomSessionId(request.getCookies()));

				if (alive == null) {

						response.sendRedirect("Login.jsp");

						return;

					}

			// upto this session check is implemented

			ContactDetails uc = (ContactDetails) request.getAttribute("user_spec_contact");
			if (uc == null) {
				response.sendRedirect("home.jsp");
				return;
			}
			System.out.println("hey" + uc.getFirstName());
			%>
			<label for="name">FirstName</label> <input type="text" name="f_name"
				value="<%=uc.getFirstName()%>" required /> <label for="name">MiddleName</label>
			<input type="text" value="<%=uc.getMiddleName()%>" name="m_name" /> <label
				for="name">LastName</label> <input type="text"
				value="<%=uc.getLastName()%>" name="l_name" /> <label for="phoneno">Phone
				No</label> <input type="text" name="phone" value="<%=uc.getContactphone().getContactPhone()%>"
				required /> <label for="email">Email</label> <input type="email"
				name="email" value="<%=uc.getContactMail().getContactMailID()%>" required />


			<div
				style="display: flex; flex-direction: row; gap: 10px; align-items: center;background-color: white;">
				<%
				if ("M".equals(uc.getGender())) {
				%>
				<label for="Gender">Gender</label>  <input type="radio"
					name="gender" value="male" checked>   <label for="male">Male</label><br>
				  <input type="radio" name="gender" value="female">   <label
					for="female">female</label><br>
				<%
				} else {
				%>
				<label for="Gender">Gender</label>  <input type="radio"
					name="gender" value="male">   <label for="male">Male</label><br>
				  <input type="radio" name="gender" value="female" checked>
				  <label for="female">female</label><br>

				<%
				}
				%>
				 
			</div>
			<label for="address">Address</label>
			<textarea rows="3" cols="20" name="Address" required><%=uc.getAddress()%></textarea>
			<input type="hidden" name="contactid" value="<%=uc.getID()%>"
				required /> 
				<input type="hidden" name="contactemailid" value="<%=uc.getContactMail().getID()%>"
				required /> 
				<input type="hidden" name="contactphoneid" value="<%=uc.getContactphone().getID()%>"
				required /> 
				
				
				<input type="submit" class="glowyellowbutton" value="Update" />
		</form>
		<form action="/home.jsp" method="get">
			<input type="submit" value="Back" class="back-btn" />
		</form>
	</div>


</body>
</html>