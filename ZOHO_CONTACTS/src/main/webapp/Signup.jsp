<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@page import="dboperation.SessionOperation"%> 
   
    
 
  <%
  
	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
  
  %>
    
   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up</title>
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
    input[type="password"],
    textarea,
    select {
        width: calc(100% - 20px);
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        margin-top: 5px;
        box-sizing: border-box;
    }

    textarea {
        resize: none;
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

    #prompt {
        display: none;
        color: #888;
        font-size: 12px;
        margin-top: 5px;
    }

    /* Styling the timezone dropdown */
    select {
        appearance: none;
        -webkit-appearance: none;
        -moz-appearance: none;
        background-color: #fff;
        background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><polygon points="0,0 20,0 10,10"/></svg>');
        background-repeat: no-repeat;
        background-position: right 10px center;
        background-size: 12px;
        padding-right: 30px;
        font-size: 16px;
        color: #333;
    }

    /* Ensures proper rendering on mobile and non-webkit browsers */
    select:focus {
        outline: none;
        border-color: #333;
    }

    select:hover {
        border-color: #888;
    }

    /* Additional styling for dropdown */
    .container select {
        margin-top: 10px;
        border: 1px solid #ccc;
    }
</style>

    
    
    
      <script>
        <% if (request.getAttribute("errorMessage") != null) { %>
            alert("<%= request.getAttribute("errorMessage") %>");
        <% } %>
    </script>

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
 
 
 
        <h2>Signup</h2>
        <form action="/signup" method="post">
            <label for="name">Name</label>
            <input type="text" name="Name"  autocomplete="off" required />

            <label for="phoneno">Phone No</label>
            <input type="text" name="phone"  autocomplete="off" required />

            <label for="email">Email</label>
            <input type="email" name="email"   autocomplete="off" required />
          <label for="email">Timezone</label>
        <select name="timezone" id="timezone" required>
          <option value="Asia/Kolkata">Asia/Kolkata (Indian Standard Time)</option> 
            <option value="America/New_York">America/New_York (Eastern Time)</option>
            <option value="America/Chicago">America/Chicago (Central Time)</option>
            <option value="America/Denver">America/Denver (Mountain Time)</option>
            <option value="America/Los_Angeles">America/Los_Angeles (Pacific Time)</option>
            <option value="Europe/London">Europe/London (GMT/BST)</option>
            <option value="Europe/Paris">Europe/Paris (CET/CEST)</option>
            <option value="Asia/Tokyo">Asia/Tokyo (Japan Standard Time)</option>
            <option value="Asia/Kolkata">Asia/Kolkata (India Standard Time)</option>
            <option value="Australia/Sydney">Australia/Sydney (Australian Eastern Daylight Time)</option>
            <option value="America/Sao_Paulo">America/Sao_Paulo (Brasília Time)</option>
            <option value="Africa/Johannesburg">Africa/Johannesburg (South Africa Standard Time)</option>
            <option value="Asia/Shanghai">Asia/Shanghai (China Standard Time)</option>
            <option value="UTC">UTC (Coordinated Universal Time)</option>
        </select>


            <label for="address">Address</label>
            <textarea rows="3" cols="20" name="Address"   autocomplete="off" required></textarea>



   <input type="hidden" id="localDateTime" name="localDateTime">
        <input type="hidden" id="timeZoneId" name="timeZoneId">
        <input type="hidden" id="utcDateTime" name="utcDateTime">
   
   
            <label for="username">Username</label>
            <input type="text" name="username"  autocomplete="off" required />

            <label for="password">Password</label>
            <input type="password" id="pass" name="password"  autocomplete="off" required />
              <span id="prompt"> Password must be 8 - 72 characters</span>

            <input type="submit" onclick="prepareFormData()" value="Signup" />
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
    
    
    
    
    function prepareFormData() {
        
        let localDate = new Date();  


        let timeZoneId = Intl.DateTimeFormat().resolvedOptions().timeZone;

       
        let utcDate = localDate.toISOString();  

       
        document.getElementById('localDateTime').value = localDate.toISOString(); 
        document.getElementById('timeZoneId').value = timeZoneId;   
         
        document.getElementById('utcDateTime').value = utcDate;                     
    }

    
    </script>
</body>
</html>