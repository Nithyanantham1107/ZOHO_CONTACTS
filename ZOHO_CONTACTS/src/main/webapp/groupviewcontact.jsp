<%@page import="com.zohocontacts.dbpojo.Category"%>
<%@page import="com.zohocontacts.dboperation.UserGroupOperation"%>
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
</head>
<body>




	<%
	CacheModel cachemodel = SessionOperation.checkSessionAlive(SessionOperation.getCustomSessionId(request.getCookies()));
		
		if (cachemodel == null) {
			System.out.println("hello hi");
			response.sendRedirect("Login.jsp");
			return;

		}
		UserData ud = cachemodel.getUserData();
		
		Category group = (Category) request.getAttribute("group");
		

		ArrayList<ContactDetails> contactsInGroup = new ArrayList<>();
		if(group!=null){
		
		
		contactsInGroup = UserGroupOperation.getGroupContactList(group.getID(), ud.getID(), "view");
		
		
		
			
		}
	%>

	<section id="header">



		<a href="#"> <img src="asset/contactIcon.png" class="logo" alt="">
		</a>


		<div>


			<ul id="navbar">
				<li><a href="home.jsp">Contacts</a></li>
				<li><a href="groups.jsp">Groups</a></li>
				<li><a href="profile.jsp">Profile</a></li>
				<li><a href="changePassword.jsp"> More</a></li>
				<li><a href="/logout"> <i
						class="fa-solid fa-arrow-right-from-bracket"></i>
				</a></li>

			</ul>

		</div>



	</section>

































	<section id="main">



		<section id="tableContainer">
			<section id="tableHeader">
			
			<%
			
			if(group!=null){
				
			
			
			%>

					<h1><%= group.getCategoryName() %></h1>



				
				
				
				<%}else{ %>
				
					<h1> Group Details </h1>



				
				<%
				
				}
				%>

			

			</section>

			<section id="table">



				<table>

					<thead>


						<tr>
							<th>FirstName</th>
							<th>MiddleName</th>
							<th>LastName</th>
							<th>Gender</th>
							<th>Email</th>
							<th>Phone</th>
						
							<th>Remove</th>


						</tr>

					</thead>
					<tbody>

						<%
						if (contactsInGroup != null) {

							for (ContactDetails uc : contactsInGroup) {
						%>




						<tr>


							<td><%=uc.getFirstName()%></td>
							<td><%=uc.getMiddleName()%></td>
							<td><%=uc.getLastName()%></td>
							<td><%=uc.getGender()%></td>
						
						
						<%
							if (uc.getAllContactMail() != null && uc.getAllContactMail().size() > 0) {
							%>
							<td><%=uc.getAllContactMail().getFirst().getContactMailID()%></td>


							<%
							} else {
							%>
							<td>Mail</td>

							<%
							}
							%>
							
						<%
							if (uc.getAllContactphone() != null && uc.getAllContactphone().size() > 0) {
							%>
							<td><%=uc.getAllContactphone().getFirst().getContactPhone()%></td>


							<%
							} else {
							%>
							<td>Phone</td>

							<%
							}
							%>
						

	
							<td>



								<form action="/groupcontactremoval" method="post">
									<input type="hidden" value="<%=uc.getID()%>" name="contactID" />
									
									<input type="hidden" value="<%=group.getID()%>" name="groupID" />
									<input type="submit" class="glowredbutton" value="remove" />
								</form>
							</td>
						</tr>

						<%
						}
						}
						%>



					</tbody>

				</table>


			</section>


		</section>

	</section>


















</body>
</html>