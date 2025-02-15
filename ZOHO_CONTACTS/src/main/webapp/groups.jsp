<%@page import="dboperation.UserGroupOperation"%>
<%@page import="dbpojo.Category"%>
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
	ArrayList<ContactDetails> userContacts=null;
	ArrayList<Category> usergroup=null;

	 userContacts = UserContactOperation.viewAllUserContacts(ud.getID());

 usergroup = UserGroupOperation.viewAllGroup(ud.getID());

	
	

	
	ArrayList<ContactDetails> groupsInContact = new ArrayList<>();
	ArrayList<ContactDetails> groupsNotInContact = new ArrayList<>();
	Category group = null;
	System.out
			.println("here the type" + (request.getAttribute("method") != null && request.getAttribute("groupID") != null));

	if (request.getAttribute("method") != null && request.getAttribute("groupID") != null) {

		String method = (String) request.getAttribute("method");
		int groupID = (int) request.getAttribute("groupID");
		group = UserGroupOperation.getSpecificGroup(groupID, ud.getID());

		if (method.equals("view")) {

			groupsInContact = UserGroupOperation.getGroupContactList(groupID, ud.getID(), method);

		} else {

			groupsNotInContact = UserGroupOperation.getGroupContactList(groupID, ud.getID(), method);

		}

	}
	%>



	<nav id="sidebar">
		
<section id="creategroup">
			
				<h1>Create Group</h1>


		


		

				<button id="closecreatemodel">
 X

				</button>


		








		

          <div>
			<input type="text" id="groupNamecreate"
				placeholder="Enter group name" required /> <input type="hidden"
				id="groupidcreate" required /> <input type="hidden" value="create"
				id="methodcreate" />
			<button id="submitGroup" class="glowgreenbutton">Create Group</button>
</div>






<div id="createtablecontainer">
		



			<table>

				<thead>


					<tr>

						<th>select</th>
						<th>FirstName</th>



						<th>Email</th>
						<th>Phone</th>



					</tr>

				</thead>
				<tbody>

					<%
					if (userContacts != null) {

						for (ContactDetails uc : userContacts) {
					%>
					<tr>

						<td><input type="checkbox" name="contact_ids"
							value="<%=uc.getID()%>" style="display: block;"
							class="contact-checkbox" /></td>
						<td><%=uc.getFirstName()%></td>

						<td><%=uc.getContactMail().getContactMailID()%></td>
						<td><%=uc.getContactphone().getContactPhone()%></td>



					</tr>

					<%
					}
					}
					%>



				</tbody>

			</table>

</div>
		</section>





	</nav>



























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
			<section id="tableHeader">

				<section id="tableHeader">
					<h1>Groups</h1>



				</section>

				<section id="addbutton">

					<button id="groupbutton" class="glowgreenbutton">Create group</button>
				</section>

			</section>

			<section id="table">



				<table>

					<thead>


						<tr>
							<th>Group Name</th>
							<th>View</th>
							<th>add</th>
							<th>Delete</th>


						</tr>

					</thead>
					<tbody>

						<%
						if (usergroup != null && usergroup.size() > 0) {

							for (Category ug : usergroup) {
						%>




						<tr>


							<td><%=ug.getCategoryName()%></td>


							<td>
								<form action="/grouplist" method="post">
									<input type="hidden" value="<%=ug.getID()%>" name="groupid"
										id="categoryID" /> <input type="hidden" value="view"
										name="method" /> <input type="submit"
										class="glowyellowbutton" id="viewgroup" value="view Group" />
								</form>

							</td>
							<td>
								<form action="/grouplist" method="post">
								 <input type="hidden" value="add"
										name="method" /> 
									<input type="hidden" value="<%=ug.getID()%>" name="groupid" />
									<input type="submit" class="glowgreenbutton" value="add" />
								</form>
							</td>
							
							
							<td>
								<form action="/deletegroup" method="post">
								 
									<input type="hidden" value="<%=ug.getID()%>" name="groupid" />
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














	<script type="text/javascript" src="js/script.js">
        <%if (request.getAttribute("errorMessage") != null) {%>
            alert("<%=request.getAttribute("errorMessage")%>
		");
	<%}%>
		
	
		
	</script>



</body>
</html>