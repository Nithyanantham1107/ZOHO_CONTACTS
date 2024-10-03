<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   
    
 
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
        input[type="email"],input[type="password"],
        textarea {
            width: calc(100% - 20px);
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-top: 5px;
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
            color:black;
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
 <div class="container">
        <h2>Signup</h2>
        <form action="/signup" method="post">
            <label for="name">Name</label>
            <input type="text" name="Name"  autocomplete="off" required />

            <label for="phoneno">Phone No</label>
            <input type="text" name="phone"  autocomplete="off" required />

            <label for="email">Email</label>
            <input type="email" name="email"   autocomplete="off" required />

            <label for="address">Address</label>
            <textarea rows="3" cols="20" name="Address"   autocomplete="off" required></textarea>

            <label for="username">Username</label>
            <input type="text" name="username"  autocomplete="off" required />

            <label for="password">Password</label>
            <input type="password" id="pass" name="password"  autocomplete="off" required />
              <span id="prompt"> Password must be 8 - 72 characters</span>

            <input type="submit" value="Signup" />
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
    
    </script>
</body>
</html>