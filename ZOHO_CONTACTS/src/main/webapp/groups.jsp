<%@page import="com.zohocontacts.sessionstorage.ThreadLocalStorage"%>
<%@page import="java.util.List"%>
<%@page import="com.zohocontacts.dboperation.UserGroupOperation"%>
<%@page import="com.zohocontacts.dbpojo.Category"%>
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


	<%
	CacheModel cacheModel = ThreadLocalStorage.getCurrentUserCache();
	if (cacheModel == null || cacheModel.getUserData() == null) {

		System.out.println("hello hi");
		response.sendRedirect("Login.jsp");
		return;

	}

	UserData ud = cacheModel.getUserData();
	List<ContactDetails> userContacts = null;
	List<Category> usergroup = null;

	userContacts = UserContactOperation.viewAllUserContacts(ud.getID());

	usergroup = UserGroupOperation.viewAllGroup(ud.getID());

	List<ContactDetails> groupsInContact = new ArrayList<>();
	List<ContactDetails> groupsNotInContact = new ArrayList<>();
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







			<button id="closecreatemodel">X</button>













			<div class="modelcreategroup">
				<input type="text" id="groupNamecreate"
					placeholder="Enter group name" required /> <input type="hidden"
					id="groupidcreate" required /> <input type="hidden" value="create"
					id="methodcreate" />
				<button id="submitGroup" class="glowgreenbutton">Create</button>
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
			<li><a href="emails.jsp"> emails</a></li>
				<li><a href="changePassword.jsp"> change Password</a></li>
				<li><a href="/logout"> <i
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

					<button id="groupbutton" class="glowgreenbutton">Create</button>
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
								<form action="/group" method="post">
									<input type="hidden" value="<%=ug.getID()%>" name="groupid"
										id="categoryID" /> <input type="hidden" value="view"
										name="method" /> <input type="hidden" value="groupview"
										name="action" />
									<button type="submit" class="logobutton">
										<i class="fa-regular fa-eye"></i>
									</button>
								</form>

							</td>
							<td>
								<form action="/group" method="post">
									<input type="hidden" value="add" name="method" /> <input
										type="hidden" value="<%=ug.getID()%>" name="groupid" /> <input
										type="hidden" value="groupview" name="action" />

									<button type="submit" class="updatelogobutton">
										<i class="fa-solid fa-pen-to-square"></i>
									</button>

								</form>
							</td>


							<td>
								<form action="/group" method="post">
									<input type="hidden" value="deletegroup" name="action" /> <input
										type="hidden" value="<%=ug.getID()%>" name="groupid" />


									<button type="submit" class="deletelogobutton">
										<i class="fa-regular fa-trash-can"></i>
									</button>

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












	<script type="text/javascript" src="js/script.js"></script>





</body>
</html>