<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Voter Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container auth-container">
    <h1>Voter Registration</h1>
    <p class="subtitle">Create your voter account to cast a ballot.</p>

    <%
        String error = request.getParameter("error");
        if ("missing".equals(error)) {
    %>
        <p class="alert">Please fill in all fields.</p>
    <%
        } else if ("mismatch".equals(error)) {
    %>
        <p class="alert">Passwords do not match. Please try again.</p>
    <%
        } else if ("taken".equals(error)) {
    %>
        <p class="alert">That username is already registered. Please choose another or log in.</p>
    <%
        }
    %>

    <form action="register" method="post" class="auth-form">
        <div class="form-group">
            <label for="fullName">Full Name</label>
            <input type="text" id="fullName" name="fullName" placeholder="Jane Doe" required autofocus>
        </div>
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" placeholder="janedoe" required>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" placeholder="At least 6 characters" minlength="6" required>
        </div>
        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Re-enter password" minlength="6" required>
        </div>
        <button type="submit" class="btn-vote">Register</button>
    </form>

    <p class="results-link">Already registered? <a href="login.jsp">Log in</a></p>
</div>
</body>
</html>
