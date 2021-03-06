<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="Template.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
</head>
<body>
	<%
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		// HTTP 1.1 
		response.setHeader("Pragma","no-cache"); 
		// HTTP 1.0 
		response.setHeader("Expires", "0"); 
		// Proxies
		if (!session.getAttribute("role").equals("admin")) {
			response.sendRedirect("getLoginPage.do");
		}
	%>
	<div align="center">
		<h3>
			Welcome Admin: <i>${userId}</i>
		</h3>
		<br> <b><i>${message}</i></b>
	</div>
	<div class="centered">
		<fieldset>
			<legend>
				<b> ADD NEW </b>
			</legend>
			<a class="css" href="getRegistrationPage.do">Employee</a> <a
				class="css" href="getNewProject.do">Project</a>
		</fieldset>
		<br> <br>
		<!-- <fieldset>
			<legend>
				<b> ASSIGN </b>
			</legend>
			<a class="css" href="assignManagerToProject.do">Manager To Project</a> <a
				class="css" href="assignEmployeeToProject.do">Employees To Project</a> <a
				class="css" href="assignEmployeeToManager.do">Employees To Manager</a>
		</fieldset> -->
	</div>
</body>
</html>