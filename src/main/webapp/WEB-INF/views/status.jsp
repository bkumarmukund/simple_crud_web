<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <title>Status Page</title>
</head>
<body>
    <p>status:<%= request.getAttribute("message") %></p><br>
</body>
</html>