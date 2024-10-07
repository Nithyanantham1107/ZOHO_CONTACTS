<%@page import="dbmodel.UserGroup"%>
<%@page import="dbmodel.UserContacts"%>
<%@page import="java.util.ArrayList"%>
<%@page import="dboperation.UserGroupOperation"%>
<%@page import="dboperation.UserContactOperation"%>
<%@page import="dboperation.UserOperation"%>
<%@page import="dboperation.SessionOperation"%>
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
<title>Adding Contact</title>



<style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
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
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        label {
            margin-top: 10px;
            font-weight: bold;
            display: block;
        }

        input[type="text"],
        input[type="email"],
        textarea {
            width: calc(100% - 20px);
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-top: 5px;
        }

        textarea {
            resize: none; /* Prevent resizing */
        }

        input[type="submit"] {
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

        input[type="submit"]:hover {
            background-color: white;
            color:black;
        }

        .back-btn {
            background-color: #ccc;
            margin-top: 10px;
        }

        .back-btn:hover {
            background-color: #bbb;
        }
    </style>
    
    
    <script>
        <% if (request.getAttribute("errorMessage") != null) { %>
            alert("<%= request.getAttribute("errorMessage") %>");
        <% } %>
    </script>
    
</head>
<body>
<%
 session = request.getSession(false);
SessionOperation so=new SessionOperation();
UserData ud = (UserData) session.getAttribute("user");
String sessionid = so.getCustomSessionId(request.getCookies());
int userid = so.checkSessionAlive(sessionid);
if (userid != 0) {
	if (ud == null) {

		session = request.getSession();
		UserOperation user_op = new UserOperation();
		UserContactOperation uco = new UserContactOperation();
		UserGroupOperation ugo = new UserGroupOperation();
		
		ud = user_op.getUserData(userid);
		

		ArrayList<UserContacts> uc = uco.viewAllUserContacts(userid);
		ArrayList<UserGroup> ug = ugo.viewAllGroup(userid);

		session.setAttribute("user", ud);
		session.setAttribute("usercontact", uc);
		session.setAttribute("usergroup", ug);
	}

} else {

	so.DeleteSessionData(sessionid);
	if (session != null) {

		session.invalidate();
	}

	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	response.sendRedirect("index.jsp");
	return;

}

// upto this session check is implemented


if(ud==null){
	response.sendRedirect("index.jsp");
	return;
}


%>


<div class="container">
        <h2>Add Contacts</h2>
        <form action="/addcontact" method="post">
            <label for="name">FirstName</label>
            <input type="text" name="f_name" required />
             
             
             <label for="name">MiddleName</label>
            <input type="text" name="m_name"  />
            
            <label for="name">LastName</label>
            <input type="text" name="l_name"  />
            
            
            <label for="phoneno">Phone No</label>
            <input type="text" name="phone" required />

            <label for="email">Email</label>
            <input type="email" name="email" required />

            
     <div style=" display: flex;
            flex-direction: row;
            gap: 10px; 
            align-items: center">
           <label for="Gender">Gender</label>
            <input type="radio"  name="gender" value="male">
           <label for="male">Male</label><br>
           <input type="radio"  name="gender" value="female">
            <label for="female">female</label><br>
     
  
</div>
           <label for="address">Address</label>
            <textarea rows="3" cols="20" name="Address" required></textarea>
         
        
            <input type="submit" value="Add" />
        </form>
        <form action="/Dashboard.jsp" method="get">
            <input type="submit" value="Back" class="back-btn" />
        </form>
    </div>


</body>
</html>