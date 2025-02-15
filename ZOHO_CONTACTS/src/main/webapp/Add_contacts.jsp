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
<title>Adding Contact</title>

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

.container {
	background-color: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 550px; form { display : flex;
	flex-direction: column;
	justify-content: center;
	background-color: white;
}


.input-container {
	display: flex;
	background-color:white;
	flex-direction: row;
	justify-content: space-between;
}

}
h2 {
	background-color: white;
	color: black;
	text-align: center;
	margin-bottom: 20px;
}
.remove-btn {
	display: flex;
	padding: 20px;
	margin: 3px;
	background-color: #dda853;
	color: white;
	border: none;
	font-size: 14px;
	border-radius: 5px;
	margin: 3px;
}

label {
	background-color: white;
	color: black;
	margin-top: 10px;
	font-weight: bold;
	display: block;
}

input[type="text"], input[type="email"], textarea {
	background-color: white;
	color: black;
	width: calc(100% - 20px);
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-top: 5px;
	margin-bottom: 5px;
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
	<%
	CacheModel alive = SessionOperation.checkSessionAlive(SessionOperation.getCustomSessionId(request.getCookies()));

	if (alive == null) {

		response.sendRedirect("Login.jsp");

		return;

	}
	%>


	<div class="container">
		<h2>Add Contacts</h2>
	 <form id="contactForm">
        <label for="name">FirstName</label> 
        <input type="text" name="f_name" required /> 

        <label for="name">MiddleName</label> 
        <input type="text" name="m_name" /> 

        <label for="name">LastName</label> 
        <input type="text" name="l_name" /> 

        <label for="phoneno">Phone No</label>
        <div id="Phonelist">
            <div class="input-container">
                <input type="text" name="phones" required />
                <button type="button" class="remove-btn" onclick="removeField(this)">Remove</button>
            </div>
        </div>
        <button type="button" class="glowyellowbutton" id="addphone">Add Phone No</button>

        <label for="email">Email</label>
        <div id="emaillist">
            <div class="input-container">
                <input type="text" name="emails" required />
                <button type="button" class="remove-btn" onclick="removeField(this)">Remove</button>
            </div>
        </div>
        <button type="button" class="glowyellowbutton" id="addEmail">Add Email</button>

        <div style="display: flex; flex-direction: row; gap: 10px; align-items: center; background-color: white">
            <label for="Gender">Gender</label>  
            <input type="radio" name="gender" value="male">   
            <label for="male">Male</label><br>
            <input type="radio" name="gender" value="female">   
            <label for="female">female</label><br>   
        </div>

        <input type="hidden" id="localDateTime" name="localDateTime">
        <input type="hidden" id="timeZoneId" name="timeZoneId"> 
        <input type="hidden" id="utcDateTime" name="utcDateTime"> 

        <label for="address">Address</label>
        <textarea rows="3" cols="20" name="Address" required></textarea>

        <input type="submit" class="glowgreenbutton" value="Add" />
    </form>
		<form action="/home.jsp" method="get">
			<input type="submit" value="Back" class="back-btn" />
		</form>
	</div>


	<script type="text/javascript" src="js/addcontact.js">
	
	</script>
	<script type="text/javascript" src="js/formfunctional.js">
		function prepareFormData() {

			let localDate = new Date();

			let timeZoneId = Intl.DateTimeFormat().resolvedOptions().timeZone;

			let utcDate = localDate.toISOString();

			document.getElementById('localDateTime').value = localDate
					.toISOString();
			document.getElementById('timeZoneId').value = timeZoneId;

			document.getElementById('utcDateTime').value = utcDate;
		}
	</script>

</body>
</html>