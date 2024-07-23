<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Registration Form</title>
</head>
<body>
    <center>
        <h2><%= request.getAttribute("message") %>!</h2>
        <form id="newEntryForm" hx-post="db" 
        hx-trigger="submit">
            <label for="username">Username:</label><br>
            <input type="text" id="username" name="username" required><br><br>

            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required><br><br>

            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" required><br><br>

            <label for="firstName">First Name:</label><br>
            <input type="text" id="firstName" name="firstName" required><br><br>

            <label for="lastName">Last Name:</label><br>
            <input type="text" id="lastName" name="lastName" required><br><br>

            <label for="dateOfBirth">Date of Birth:</label><br>
            <input type="date" id="dateOfBirth" name="dateOfBirth"><br><br>

            <input type="submit" value="Submit">
        </form>
    </center>
</body>
</html>
