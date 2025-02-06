<%@page import="dbpojo.Oauth"%>
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
</head>
<body>




	<%
	CacheModel cachemodel = SessionOperation.checkSessionAlive(SessionOperation.getCustomSessionId(request.getCookies()));

	if (cachemodel == null) {
		System.out.println("hello hi");
		response.sendRedirect("Login.jsp");
		return;

	}

	Userdata ud = cachemodel.getUserData();

	ArrayList<ContactDetails> userContacts = UserContactOperation.viewAllUserContacts(ud.getID());
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
				<li><a href="/login"> <i
						class="fa-solid fa-arrow-right-from-bracket"></i>
				</a></li>

			</ul>

		</div>



	</section>


	<section id="main">



		<section id="tableContainer">
			<section id="header">

				<%
				if (ud.getallOauth().size() > 0) {

					Oauth oauth = ud.getallOauth().getFirst();

					if (oauth.getID() == -1) {
						System.out.println("here the Oauth data IDis" + oauth.getID());
						System.out.println("here the Oauth data is" + oauth.getEmail());
				%>
				<form action="/oauthdirector" method="post">
					<input type="submit" value="Sync with Google"
						class="glowgreenbutton" />
				</form>


				<%
				} else {

				if (oauth.getSyncState()) {
				%>



				<form action="/syncstate" method="post">
					<input type="hidden" name="operation" value="syncoff"
						class="glowbutton" /> <input type="hidden" name="ID"
						value="<%=oauth.getID()%>" class="glowbutton" /> <input
						type="submit" value="Sync off" class="glowyellowbutton" />
				</form>

				<%
				} else {
				%>
				<form action="/syncstate" method="post">


					<input type="hidden" name="operation" value="syncon" /> <input
						type="hidden" name="ID" value="<%=oauth.getID()%>" /> <input
						type="submit" value="Sync On" class="glowgreenbutton" />
				</form>

				<%
				}
				%>

				<form action="/syncstate" method="post">

					<input type="hidden" name="operation" value="deletesync" /> <input
						type="hidden" name="ID" value="<%=oauth.getID()%>" /> <input
						type="submit" value="Delete Sync" class="glowredbutton" />
				</form>
				<%
				}
				}
				%>



				<section id="tableHeader">
					<h1>Contact</h1>



				</section>

				<section id="addbutton">

					<form action="Add_contacts.jsp">


						<input type="submit" class="glowgreenbutton" value="Addcontact" />
					</form>
				</section>

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
							<th>Update</th>
							<th>Delete</th>


						</tr>

					</thead>
					<tbody>

						<%
						if (userContacts != null) {

							for (ContactDetails uc : userContacts) {
						%>




						<tr>


							<td><%=uc.getFirstName()%></td>
							<td><%=uc.getMiddleName()%></td>
							<td><%=uc.getLastName()%></td>
							<td><%=uc.getGender()%></td>
							<td><%=uc.getContactMail().getContactMailID()%></td>
							<td><%=uc.getContactphone().getContactPhone()%></td>


							<td>


								<form action="/GetAndUpdatecontact" method="post">
									<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
									<input type="submit" class="glowyellowbutton" value="Update" />
								</form>
							</td>
							<td>



								<form action="/deletecontact" method="post">
									<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
									<input type="submit" class="glowredbutton" value="Delete" />
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