<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="Template.jsp" />
<%@ page isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
</head>
<body>
	<a class="css" href="getHomePage.do">HomePage</a>
	<div align="center">
		<h2>Error Occurred!</h2>
		<br> <font color="red"><b><i>${message}</i></b></font>
	</div>
</body>
</html>