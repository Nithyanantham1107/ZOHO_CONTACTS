<%@page import="com.zohocontacts.sessionstorage.ThreadLocalStorage"%>
<%@page import="com.zohocontacts.dbpojo.ContactMail"%>
<%@page import="com.zohocontacts.dbpojo.ContactPhone"%>
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

.container {
	background: white;
	padding: 20px;
	border-radius: 8px;
	box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
	width: 550px;
	max-height: 973px;
	overflow: scroll; form { display : flex;
	flex-direction: column;
	justify-content: center;
	background-color: white;
	overflow: scroll;
}

div {
	background-color: white;
}

.remove-btn {
	display: flex;
	padding: 20px;
	margin: 3px;
	font-size: large;
	background-color: white;
	color: red;
	border: none;
	font-size: 14px;
	border-radius: 5px;
	margin: 3px;
	color: red;
}

.input-container {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

}
h2 {
	background-color: white;
	color: black;
	text-align: center;
	margin-bottom: 20px;
	color: #333;
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

.liststyle {
	display: flex;
	flex-direction: row;
}

textarea {
	resize: none;
}

.datalabel{

width:250px;
display:flex;

background-color: white;
input[type="text"]{

background-color: white;
	color: black;
	width:140pxpx;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-top: 5px;
	margin-bottom: 5px;

}

}
.back-btn {
	padding: 10px;
	background-color: #dda853;
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
	if (request.getAttribute("errorMessage") != null) {
	%>
	<script type="text/javascript">
        alert("<%=request.getAttribute("errorMessage")%>
		");
	</script>
	<%
	}
	%>


	<div class="container">
		<h2>Update Contacts</h2>
		<form action="/contact" method="post">
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
			System.out.println("hey" + uc.getFirstName() + uc.getID());
			%>
			<label for="name">FirstName</label> <input type="text" name="f_name"
				value="<%=uc.getFirstName()%>" required /> <label for="name">MiddleName</label>
			<input type="text" value="<%=uc.getMiddleName()%>" name="m_name" />
			<label for="name">LastName</label> <input type="text"
				value="<%=uc.getLastName()%>" name="l_name" /> <label for="phoneno">Phone
				No</label>


			<div id="Phonelist">


				<%
				if (uc.getAllContactphone() != null && uc.getAllContactphone().size() > 0) {
					for (ContactPhone phone : uc.getAllContactphone()) {
				%>
				<div class="input-container">
					<input type="text" name="phones"
						value="<%=phone.getContactPhone()%>" required />
				
				<div class="datalabel">
					<%
					
				if(phone.getLabelName()==null){
					
				%>
					<input type="text" name="phonelabels" placeholder="label" class="datalabel" />
					
				
				<%
				} else{
				%>
				
					<input type="text" name="phonelabels" value="<%=phone.getLabelName()%>" class="datalabel" />
					
				
				<%
				
				}
				%>
			
				
				
					</div>
				
					<button type="button" class="remove-btn"
						onclick="removeField(this)">X</button>
					<input type="hidden" name="phoneID" value="<%=phone.getID()%>"
						required />
				</div>


				<%
				}
				}
				%>




			</div>

			<button type="button" class="glowyellowbutton" id="addphone">Add
				Phone No</button>
			<label for="phoneno">Email</label>


			<div id="emaillist">



				<%
				if (uc.getAllContactMail() != null && uc.getAllContactMail().size() > 0) {
					for (ContactMail mail : uc.getAllContactMail()) {
				%>


				<div class="input-container">

					<input type="text" name="emails"
						value="<%=mail.getContactMailID()%>" required />
				
					<div  class="datalabel">
					
					
					
						<%
					
				if(mail.getLabelName()==null){
					
				%>
					<input type="text" name="emaillabels" placeholder="label" class="datalabel"/>
				
				<%
				} else{
				%>
				
					<input type="text" name="emaillabels" value="<%=mail.getLabelName()%>" class="datalabel"/>
				
				<%
				
				}
				%>
					
				
					
					</div>
					<button type="button" class="remove-btn"
						onclick="removeField(this)">X</button>


					<input type="hidden" name="emailID" value="<%=mail.getID()%>"
						required />
				</div>
				<%
				}
				}
				%>

			</div>


			<button type="button" class="glowyellowbutton" id="addEmail">Add
				Email</button>






			<div
				style="display: flex; flex-direction: row; gap: 10px; align-items: center; background-color: white;">
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
				required /> <input type="submit" class="glowyellowbutton"
				value="Update" /> <input type="hidden" value="updateContact"
				name="action" />


		</form>
		<form action="/home.jsp" method="get">
			<input type="submit" value="Back" class="back-btn" />
		</form>
	</div>

	<script type="text/javascript" src="js/formfunctional.js">

</script>




</body>
</html>