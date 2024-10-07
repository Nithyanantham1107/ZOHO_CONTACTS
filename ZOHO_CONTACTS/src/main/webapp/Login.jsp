<%@page import="dboperation.SessionOperation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>



<%

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 


%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
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
            width: 300px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
        }

        label {
            margin-top: 10px;
            font-weight: bold;
            display: block;
        }

        input[type="text"],
        input[type="password"] {
            width: calc(100% - 20px);
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-top: 5px;
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
            color: black;
        }

        .back-btn {
            background-color: #ccc;
            margin-top: 10px;
        }

        .back-btn:hover {
            background-color: #bbb;
        }

        #prompt,#prompt1 {
          
            display: none;
            color: #888;
            font-size: 12px;
            margin-top: 5px;
            margin-top: 10px;
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
  SessionOperation so=new SessionOperation();
  String sessionid=so.getCustomSessionId(request.getCookies());
  if(sessionid !=null ){
	  
	  response.sendRedirect("Dashboard.jsp");
  }
  
  
  
  %>
    <div class="container">
        <h2>Login </h2>
        <form action="/login" method="post">
            <label for="username">Username</label>
            <input type="text" id="user" name="username"  autocomplete="off"   required />
      <span id="prompt1">  do not use special character for username</span>
            <label for="password">Password</label>
            <input type="password" id="pass" name="password" autocomplete="off" required />
            <span id="prompt"> Password must be 8 - 72 characters </span>
          <div  class="login_button"> <input type="submit" value="Login" />
          
          </div>
        </form>
        <form action="/index.jsp" method="get">
            <input type="submit" value="Back" class="back-btn" />
        </form>
    </div>
    
    <script>
       
    
    
       const password = document.getElementById('pass');
        const prompt = document.getElementById('prompt');

        password.addEventListener('focus', function() {
            prompt.style.display = 'block'; 
        });

        password.addEventListener('blur', function() {
            prompt.style.display = 'none'; 
        });
        
        
        const user= document.getElementById('user');
        const prompt1 = document.getElementById('prompt1');

        user.addEventListener('focus', function() {
            prompt1.style.display = 'block'; 
        });

        user.addEventListener('blur', function() {
            prompt1.style.display = 'none'; 
        });
    </script>
</body>
</html>
