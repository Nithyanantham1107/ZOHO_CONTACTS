<%@page import="com.zohocontacts.dbpojo.EmailUser"%>
<%@page import="com.zohocontacts.dbpojo.Category"%>
<%@page import="com.zohocontacts.dbpojo.ContactDetails"%>
<%@page import="com.zohocontacts.dbpojo.UserData"%>
<%@page import="com.zohocontacts.sessionstorage.CacheData"%>
<%@page import="com.zohocontacts.sessionstorage.CacheModel"%>

<%@page import="dboperation.UserOperation"%>

<%@page import="java.util.UUID"%>
<%@page import="dboperation.UserContactOperation"%>

<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ZOHO CONTACTS</title>
<style>
body {
	font-family: Arial, sans-serif;
}

input[type="submit"] {
	padding: 10px;
	background-color: black;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin: 10px;
	font-size: 16px;
}

input[type="submit"]:hover {
	background-color: white;
	color: black;
}

.main {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.content {
	display: flex;
	padding: 20px;
	align-items: center;
	justify-content: center;
}

.table_header {
	display: flex;
	justify-content: center;
	align-items: center;
}

.modal {
	display: none; /* Hidden by default */
	position: fixed; /* Stay in place */
	z-index: 1; /* Sit on top */
	left: 0;
	top: 0;
	width: 100%; /* Full width */
	height: 100%; /* Full height */
	overflow: auto; /* Enable scroll if needed */
	background-color: rgb(0, 0, 0); /* Fallback color */
	background-color: rgba(0, 0, 0, 0.7); /* Black w/ opacity */
}

.modal-content {
	background-color: #fefefe;
	margin: 15% auto; /* 15% from the top and centered */
	padding: 20px;
	border: 1px solid #888;
	width: 40%; /* Could be more or less, depending on screen size */
}

.close-button {
	color: #aaa;
	float: right;
	font-size: 28px;
	font-weight: bold;
}

.close-button:hover, .close-button:focus {
	color: black;
	text-decoration: none;
	cursor: pointer;
}

#profileButton, .profileButton, #groupbutton, #submitGroup, #updateGroup
	{
	padding: 10px;
	background-color: black;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	margin: 10px;
	font-size: 16px;
}

#profileButton:hover, .prifileButton:hover, #groupbutton:hover,
	#submitGroup:hover, #updateGroup:hover {
	background-color: white;
	color: black;
}

.container {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.user_account {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
}

.user_account>input:not(input[type='submit']), #groupName {
	border-color: black;
	height: 15px;
	background-color: white;
	border-radius: 5px;
}

.field {
	display: flex;
	flex-direction: row;
	width: 100%;
	margin: 8px;
	justify-content: space-between;
}

.profile_view {
	width: 100%;
	border: none;
	border-collapse: collapse;
	margin: 20px 0;
	font-size: 1em;
	background-color: white;
	overflow: hidden; input [type="text"], input[type="email"], textarea {
	width : calc( 100% - 20px);
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	margin-top: 5px;
}

thead tr {
	border: none;
	background-color: #de5935;
	color: white;
}

th, td {
	border: none;
	padding: 12px 15px;
	text-align: left;
}

tbody tr {
	border: none;
	background-color: white;
}

tbody tr:hover {
	background-color: white;
}

}
table:not(.profile_view) {
	width: 100%;
	border-collapse: collapse;
	margin: 20px 0;
	font-size: 1em;
	background-color: white;
	border: none;
	border-radius: 16px;
	overflow: hidden; thead tr { background-color : #de5935;
	color: white;
	border-radius: 16px;
}

th, td {
	border: none;
	padding: 12px 15px;
	text-align: left;
}

tbody tr {
	border: none;
	background-color: #f4db7d;
	transition: transform 0.2s ease, box-shadow 0.2s ease;
}

tbody tr:hover {
	background-color: white;
	transform: translateY(-5px); /* Move the row up */
	box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

}
textarea {
	resize: none;
}

.emails {
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

.emailstyle {
	display: flex;
	flex-direction: row;
}

.header {
	display: flex;
	flex-direction: row;
}

.tableview {
	margin: 5px;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	display: flex;
}
</style>
<script>
        <%if (request.getAttribute("errorMessage") != null) {%>
            alert("<%=request.getAttribute("errorMessage")%>");
        <%}%>
    </script>

</head>
<body>


	<div>
		<%
		String primary =null;
				
				
				

				// upto this session check is implemented

				   CacheModel cachemodel = SessionOperation.checkSessionAlive(SessionOperation.getCustomSessionId(request.getCookies()));

			if (cachemodel == null) {
				            System.out.println("hello hi");
			response.sendRedirect("Login.jsp");
				            return;
			

				}

			
			
				
				 // String sessionid=(String) request.getAttribute("sessionid");
				         // CacheModel cachemodel=CacheData.getCache(sessionid);
				          
				          
				          UserData ud = cachemodel.getUserData();
				
				Category ugu = (Category) request.getAttribute("usergroupupdate");
				
				for (EmailUser email : ud.getallemail()) {
			if (email != null &&  email.getIsPrimary()) {
			
				
				
				primary=email.getEmail();
				
			}}
				
				
				
				ArrayList<ContactDetails> user_contacts = UserContactOperation.viewAllUserContacts(ud.getID());
				ArrayList<Category> usergroup = UserGroupOperation.viewAllGroup(ud.getID());
		%>

		<div id="profileModal" class="modal">
			<div class="modal-content">
				<span class="close-button">&times;</span>
				<div class="container">
					<h2>Profile</h2>
					<form class="user_account" id="profile_form"
						action="/updateprofile" method="post">


						<table id="profile" class="profile_view">
							<tr>
								<td><label for="name">Name</label></td>
								<td><input type="text" name="Name"
									value="<%=ud.getName()%>" required /></td>
							</tr>
							<tr>
								<td><label for="phoneno">Phone No</label></td>
								<td><input type="text" name="phone"
									value="<%=ud.getPhoneno()%>" required /></td>
							</tr>

							<tr>
								<td><label for="Timezone">Time zone</label></td>
								<td><select name="timezone" id="timezone" required>
										<option value="<%= ud.getTimezone() %>" selected><%= ud.getTimezone() %></option>
										<option value="Asia/Kolkata">Asia/Kolkata (Indian
											Standard Time)</option>
										<option value="America/New_York">America/New_York
											(Eastern Time)</option>
										<option value="America/Chicago">America/Chicago
											(Central Time)</option>
										<option value="America/Denver">America/Denver
											(Mountain Time)</option>
										<option value="America/Los_Angeles">America/Los_Angeles
											(Pacific Time)</option>
										<option value="Europe/London">Europe/London (GMT/BST)</option>
										<option value="Europe/Paris">Europe/Paris (CET/CEST)</option>
										<option value="Asia/Tokyo">Asia/Tokyo (Japan Standard
											Time)</option>
										<option value="Asia/Kolkata">Asia/Kolkata (India
											Standard Time)</option>
										<option value="Australia/Sydney">Australia/Sydney
											(Australian Eastern Daylight Time)</option>
										<option value="America/Sao_Paulo">America/Sao_Paulo
											(Brasília Time)</option>
										<option value="Africa/Johannesburg">Africa/Johannesburg
											(South Africa Standard Time)</option>
										<option value="Asia/Shanghai">Asia/Shanghai (China
											Standard Time)</option>
										<option value="UTC">UTC (Coordinated Universal Time)</option>
								</select></td>
							</tr>



							<%
							
									
							%>




							<tr>
								<td><label for="email">Email</label></td>
								<td>
									<div class="emailstyle">
										<input type="email" name="email" value="<%=primary%>"
											onchange="addEmailToDropdown()" required />
										<button type="button" onclick="Addemail()">Add</button>


										<input type="hidden" name="method" value="update" />
									</div>
								</td>
							</tr>

							<%
							
							
							for (EmailUser email : ud.getallemail()) {
								if (email != null &&  !email.getIsPrimary()) {
									
							
							
							%>

							<tr>
								<td></td>
								<td>
									<div class="emailstyle">
										<input type="email" name="email" value="<%=email.getEmail()%>"
											onchange="addEmailToDropdown()" required />
										<button type="button" onclick="removeEmail(this)">Remove</button>
									</div>
								</td>
							</tr>


							<%
							}
							}
							%>

							<tr>
								<td><label for="emailDropdown"> primary email:</label></td>
								<td><select id="emailDropdown" name="primaryemail" required>


								</select></td>
							</tr>
							<tr>
								<td><label for="address">Address</label></td>
								<td><textarea rows="3" cols="20" name="Address" required><%=ud.getAddress()%></textarea></td>
							</tr>
							<tr>
								<td><label for="username">Username</label></td>
								<td><input type="text" name="username"
									value="<%=ud.getLoginCredentials().getUserName()%>" required /></td>
							</tr>
							<tr>
								<td><label for="password">Enter password</label></td>
								<td><input type="text" name="password" /></td>
							</tr>
							<tr>
								<td><label for="password">Enter New password</label></td>
								<td><input type="text" name="Newpassword" /></td>
							</tr>
							<tr>
								<td colspan="2"><input type="submit" value="Update Profile" /></td>

							</tr>
						</table>
					</form>

					<form action="/updateprofile" method="post">
						<input type="hidden" name="method" value="delete" /> <input
							type="submit" value="Delete" />

					</form>




				</div>
			</div>
		</div>
		<div class="content">

			<h1>
				Welcome to the Dashboard
				<%=ud.getName()%></h1>


		</div>
		<div class="main">
			<div class="table_header">

				<form action="Add_contacts.jsp">


					<input class="profileButton" type="submit" value="Addcontact" />
				</form>

				<a href="/login">Log out</a>
			</div>



			<div class="header">
				<button id="profileButton">Open Profile</button>



				<div id="groupContaineru"
					style="display: none; flex-direction: row;">
					<input type="text" id="groupNameu" value="" required /> <input
						type="hidden" id="groupidu" value="" required /> <input
						type="hidden" id="methodu" value="update" />
					<button id="updateGroup">Update Group</button>


				</div>



				<button id="groupbutton" style="display: block;">Create
					group</button>


				<div id="groupContainer" style="display: none; flex-direction: row;">
					<input type="text" id="groupNamecreate"
						placeholder="Enter group name" required /> <input type="hidden"
						id="groupidcreate" required /> <input type="hidden"
						value="create" id="methodcreate" />
					<button id="submitGroup">Submit Group</button>

				</div>


				<%
				if (ugu != null) {
				%>
				<script>
				
				   const groupContaineru= document.getElementById('groupContaineru');
				   const groupbutton = document.getElementById('groupbutton');
				   
				   groupContaineru.style.display = groupContaineru.style.display === 'none' ? 'block' : 'none';
		   	    	 groupbutton.style.display = groupbutton.style.display =='none';
				   
				   
				   document.getElementById("groupNameu").value = "<%=ugu.getCategoryName() %>";
		            
		        
		            document.getElementById("groupidu").value = "<%=ugu.getID()%>";
				
				</script>
				<%
				}
				%>
			</div>




			<div
				style="width: 100%; display: flex; flex-direction: row; justify-content: space-between;">

				<div class="tableview">
					<%
					if (user_contacts != null) {
					%>
					<h3>contact Details</h3>
					<table border="1" style="border-collapse: collapse;">
						<thead>
							<tr>
								<th>Select</th>
								<!-- New column for checkboxes -->
								<th>First Name</th>
								<th>Middle Name</th>
								<th>Last Name</th>
								<th>Email</th>
								<th>Phone</th>
								<th>Gender</th>
								<th>Update</th>
								<th>Delete</th>
							</tr>
						</thead>
						<tbody>
							<%
							for (ContactDetails uc : user_contacts) {
								
								System.out.println("here name of contact is"+uc.getFirstName());
							%>
							<tr>
								<td>
									<%
									if (ugu != null && ugu.getCategoryRelation() != null) {
										if (ugu.isContactExist( uc.getID())) {
									%> <input type="checkbox" name="contact_ids"
									value="<%=uc.getID()%>" style="display: block;"
									class="contact-checkbox" checked="checked" /> <%
 } else {
 %> <input type="checkbox" name="contact_ids" value="<%=uc.getID()%>"
									style="display: block;" class="contact-checkbox" /> <%
 }
 %> <%
 } else {
 %> <input type="checkbox" name="contact_ids" value="<%=uc.getID()%>"
									style="display: none;" class="contact-checkbox" /> <%
 }
 %>




								</td>
								<!-- Hidden checkbox -->
								<td><%=uc.getFirstName()%></td>
								<td><%=uc.getMiddleName()%></td>
								<td><%=uc.getLastName()%></td>
								<td><%=uc.getContactMail().getContactMailID()%></td>
								<td><%=uc.getContactphone().getContactPhone()%></td>
								<td><%=uc.getGender()%></td>
								<td>
									<form action="/GetAndUpdatecontact" method="post">
										<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
										<input type="submit" value="Update" />
									</form>
								</td>
								<td>
									<form action="/deletecontact" method="post">
										<input type="hidden" value="<%=uc.getID()%>" name="contact_id" />
										<input type="submit" value="Delete" />
									</form>
								</td>
							</tr>
							<%
							}
							%>
						</tbody>


					</table>
					<%
					}
					%>
				</div>
				<div class="tableview">
					<%
					if (usergroup != null) {
					%>
					<h3>Group Details</h3>
					<table border="1" style="border-collapse: collapse;">
						<thead>
							<tr>

								<th>Group Name</th>

								<th>Update</th>
								<th>Delete</th>
							</tr>
						</thead>
						<tbody>
							<%
							for (Category ug : usergroup) {
							%>
							<tr>

								<td><%=ug.getCategoryName()%></td>


								<td>
									<form action="/updategroup" method="post">
										<input type="hidden" value="<%=ug.getID()%>" name="groupid" />
										<input type="hidden" value="<%=ug.getCategoryName()%>"
											name="groupName" /> <input type="submit"
											id="updategrouptable" value="Update" />
									</form>

								</td>
								<td>
									<form action="/deletegroup" method="post">
										<input type="hidden" value="<%=ug.getID()%>" name="groupid" />
										<input type="submit" value="Delete" />
									</form>
								</td>
							</tr>
							<%
							}
							%>
						</tbody>


					</table>
					<%
					}
					%>

				</div>
			</div>

		</div>
	</div>
	<script>

	   
	    
		const modal = document.getElementById("profileModal");

		const profileButton = document.getElementById("profileButton");

		const closeButton = document.querySelector(".close-button");

		profileButton.onclick = function() {
			modal.style.display = "block";
			var xhr = new XMLHttpRequest();
            xhr.open('GET', '/addcontact', true); 
            xhr.send(); 
            
           
			addEmailToDropdown();
			
		}

		closeButton.onclick = function() {
			modal.style.display = "none";
		}

		window.onclick = function(event) {
			if (event.target === modal) {
				modal.style.display = "none";
			}
		}

		function Addemail() {
			const emailTable = document.querySelector('.profile_view');
			const lastRow = emailTable.rows[emailTable.rows.length - 1];

			const newRow = emailTable.insertRow(lastRow.rowIndex - 5);

			const cell1 = newRow.insertCell(0);
			const cell2 = newRow.insertCell(1);

			cell1.innerHTML = `<span></span>`;
			cell2.innerHTML = `<div class="emailstyle"><input type="email" name="email"  onchange="addEmailToDropdown()" required /> <button type="button" onclick="removeEmail(this)">Remove</button>
			
		</div>`;
		}

		function removeEmail(button) {
			const emailRow = button.closest('tr');
			emailRow.remove();
			addEmailToDropdown();
		}
		
		
        function addEmailToDropdown() {
            const inputs = document.querySelectorAll('#profile_form input[type="email"]');
            emailList = []; 

            const dropdown = document.getElementById('emailDropdown');
            dropdown.innerHTML = '';
            dropdown.innerHTML += '<option value="<%=primary %>"><%=primary%></option>';

            inputs.forEach(input => {
                const email = input.value.trim();
                
                if (email && validateEmail(email) && !emailList.includes(email) && !("<%=primary%>" === email) ) {
                    emailList.push(email);
                    const option = document.createElement('option');
                    option.value = email;
                    option.textContent = email;
                    dropdown.appendChild(option);
                }
            });

           
           
        }

        function validateEmail(email) {
            const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 
            return regex.test(email);
        }
        
        
        
        
        
        const groupContainer = document.getElementById('groupContainer');
   	    const createGroupButton = document.getElementById("groupbutton");
   	    const checkboxes = document.querySelectorAll('.contact-checkbox');


   	    createGroupButton.onclick = function() {
   	    	console.log("hello");
   	    
             var xhr = new XMLHttpRequest();
             xhr.open('GET', '/addcontact', true); 
             xhr.send(); 


            
                	    	
                	    	 groupContainer.style.display = groupContainer.style.display === 'none' ? 'block' : 'none';
                	        checkboxes.forEach(checkbox => {
                	            checkbox.style.display = checkbox.style.display === 'none' ? 'inline' : 'none';
                	        });
                	    }
                	    
                	    
                	 document.getElementById('submitGroup').onclick = function() {
             	        const groupName = document.getElementById('groupNamecreate').value.trim();
             	        const method = document.getElementById('methodcreate').value.trim();
             	        const groupid = document.getElementById('groupidcreate').value.trim();
             	        
             	        if (!groupName) {
             	            alert("Please enter a group name.");
             	            return;
             	        }

             	        const selectedIds = Array.from(checkboxes)
             	            .filter(checkbox => checkbox.checked)
             	            .map(checkbox => checkbox.value);

             	        if (selectedIds.length === 0) {
             	            alert("Please select at least one contact.");
             	            return;
             	        }

             	      
             	        const form = document.createElement('form');
             	        form.method = 'post';
             	        form.action = '/creategroup';

             	        
             	        const groupNameInput = document.createElement('input');
             	        groupNameInput.type = 'hidden';
             	        groupNameInput.name = 'groupName';
             	        groupNameInput.value = groupName;
             	        form.appendChild(groupNameInput);
             	        
             	        
             	        const methoddata = document.createElement('input');
             	        methoddata.type = 'hidden';
             	        methoddata.name = 'methodtype';
             	        methoddata.value = method;
             	        form.appendChild(methoddata);
             	        
             	        
             	        const group = document.createElement('input');
             	        group.type = 'hidden';
             	        group.name = 'groupdata';
             	        group.value = groupid;
             	        form.appendChild(group);
             	        
             	      
             	        
             	        

             	        
             	        selectedIds.forEach(id => {
             	            const idInput = document.createElement('input');
             	            idInput.type = 'hidden';
             	            idInput.name = 'contact_ids'; 
             	            idInput.value = id;
             	            form.appendChild(idInput);
             	        });

             	        document.body.appendChild(form);
             	        form.submit();
             	    }
                	 
                	 
                	 
                	 
                	 
                	document.getElementById('updateGroup').onclick = function() {
               		 const groupName = document.getElementById('groupNameu').value.trim();
                        const method = document.getElementById('methodu').value.trim();
                        const groupid = document.getElementById('groupidu').value.trim();
             	        
             	        if (!groupName) {
             	            alert("Please enter a group name.");
             	            return;
             	        }

             	        const selectedIds = Array.from(checkboxes)
             	            .filter(checkbox => checkbox.checked)
             	            .map(checkbox => checkbox.value);

             	        if (selectedIds.length === 0) {
             	            alert("Please select at least one contact.");
             	            return;
             	        }

             	      
             	        const form = document.createElement('form');
             	        form.method = 'post';
             	        form.action = '/creategroup';

             	        
             	        const groupNameInput = document.createElement('input');
             	        groupNameInput.type = 'hidden';
             	        groupNameInput.name = 'groupName';
             	        groupNameInput.value = groupName;
             	        form.appendChild(groupNameInput);
             	        
             	        
             	        const methoddata = document.createElement('input');
             	        methoddata.type = 'hidden';
             	        methoddata.name = 'methodtype';
             	        methoddata.value = method;
             	        form.appendChild(methoddata);
             	        
             	        
             	        const group = document.createElement('input');
             	        group.type = 'hidden';
             	        group.name = 'groupdata';
             	        group.value = groupid;
             	        form.appendChild(group);
             	        
             	      
             	        
             	        

             	        
             	        selectedIds.forEach(id => {
             	            const idInput = document.createElement('input');
             	            idInput.type = 'hidden';
             	            idInput.name = 'contact_ids'; 
             	            idInput.value = id;
             	            form.appendChild(idInput);
             	        });

             	        document.body.appendChild(form);
             	        form.submit();
             	    }
               	           	
             	</script>

</body>
</html>




