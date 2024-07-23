<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Update Form</title>
</head>
<body>
    <center>
        <h3>__update__</h3>
        <h5><%= request.getAttribute("userEmail")%></h5>
        <form id="updateEntryForm" hx-put="db" 
        hx-trigger="submit">
        <input type="hidden" name="userEmail" id="userEmail" value=<%= request.getAttribute("userEmail") %> >
            <label for="username">Username:</label><br>
            <input type="text" id="username" name="username" ><br><br>

            <label for="firstName">First Name:</label><br>
            <input type="text" id="firstName" name="firstName" ><br><br>

            <label for="lastName">Last Name:</label><br>
            <input type="text" id="lastName" name="lastName" ><br><br>

            <label for="dateOfBirth">Date of Birth:</label><br>
            <input type="date" id="dateOfBirth" name="dateOfBirth"><br><br>

            <input type="submit" value="Update">
            <input type="reset" value="clear">
        </form>
    </center>
</body>
</html>
