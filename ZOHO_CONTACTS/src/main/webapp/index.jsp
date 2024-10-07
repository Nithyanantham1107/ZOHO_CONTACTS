<%@page import="dboperation.SessionOperation"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%
  
  response.addHeader("Cache-Control", " no-cache");
 
  System.out.println("hello from index.jsp page");
  
  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ZOHO CONTACTS</title>


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
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 300px;
        }

        h2 {
            margin-bottom: 20px;
            color: #333;
        }

        input[type="submit"] {
            padding: 10px;
            background-color: black;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
            font-size: 16px;
            margin: 10px 0;
        }

        input[type="submit"]:hover {
            background-color: white;
            color:black
        }

        .footer {
            margin-top: 15px;
            font-size: 14px;
        
        }

     
    </style>
</head>
<body>
  <div class="container">
  
  <%
  SessionOperation so=new SessionOperation();
  String sessionid=so.getCustomSessionId(request.getCookies());
  if(sessionid !=null ){
	  
	  response.sendRedirect("Dashboard.jsp");
  }
  
  
  
  %>
  
  
        <h2>ZOHO CONTACTS</h2>
        <form action="Login.jsp">
            <input type="submit" value="Login"/>
        </form>
        <form action="Signup.jsp">
            <input type="submit" value="Sign Up"/>
        </form>
        
    </div>


</body>
</html>