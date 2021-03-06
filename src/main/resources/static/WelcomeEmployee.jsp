<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="Template.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee</title>
</head>
<body>
	<%
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		// HTTP 1.1 
		response.setHeader("Pragma","no-cache"); 
		// HTTP 1.0 
		response.setHeader("Expires", "0"); 
		// Proxies
		if (!session.getAttribute("role").equals("employee")) {
			response.sendRedirect("getLoginPage.do");
		}
	%>
	<div class="align-right">
		<a class="css" href="logout.do">Log Out</a>
	</div>
	<div class="centered">
		<h3>
			Welcome Employee : <i>${userId}</i>
		</h3>
		<br> <b><i>${message}</i></b> <br> <br> <a class="css"
			href="getEmployeeMainPage.do">View Projects And Tasks</a>
	</div>
</body>
</html>