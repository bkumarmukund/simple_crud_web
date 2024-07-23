<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <title>Error Page</title>
</head>
<body>
    <h4>Some <%= request.getAttribute("error") %> error occurred! Now</h4><br>
    
</body>
</html>
