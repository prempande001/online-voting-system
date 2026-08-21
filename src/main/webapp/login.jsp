<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voter Login</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container auth-container">
    <h1>Voter Login</h1>
    <p class="subtitle">Log in to cast your vote.</p>

    <%
        if ("true".equals(request.getParameter("registered"))) {
    %>
        <p class="success">Registration successful! Please log in to vote.</p>
    <%
        }
        if ("invalid".equals(request.getParameter("error"))) {
    %>
        <p class="alert">Invalid username or password. Please try again.</p>
    <%
        }
    %>

    <form action="login" method="post" class="auth-form">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" placeholder="janedoe" required autofocus>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="Your password" required>
        </div>
        <button type="submit" class="btn-vote">Log In</button>
    </form>

    <p class="results-link">New voter? <a href="register.jsp">Register here</a></p>
</div>
</body>
</html>
