<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<span class="align-right"> <a class="css" href="logout.do">Log
			Out</a></span>
	<br>
	<br>
	<br>
	<div align="center">
	<br>
		<h2>NEW PROJECT</h2>
		<br>
		<font size=5>${message}  ${newUserId}</font>
		<br><br>
		<form method="GET" action="setNewProject.do">
			<table>
				<tr>
					<th>Project Name:</th>
					<td><input required type="text" name="projectName"
						placeholder="Project Name" pattern="[a-zA-Z ]{3,}"
						title="Atleast 3 charecters. All Alphabets."></td>
				</tr>
				<tr>
					<th>Assign Manager:</th>
					<td>
						<select class = "css" required name="manager">
							
							<c:forEach items="${requestScope.managers}" var="manager">
								<option value = '${manager.employee.employeeId}'>
									${manager.employee.firstName} ${manager.employee.lastName}
								</option>
							</c:forEach>
							
						</select>
					</td>
				</tr>
				<tr>
					<th>Assign Employees:</th>
					<td>
						<select class = "css" required name="employees" size = "10" multiple>
							
							<c:forEach items="${requestScope.employees}" var="employee">
								<option value = "${employee.employee.employeeId}">
									${employee.employee.firstName} ${employee.employee.lastName}
								</option>
							</c:forEach>
							
						</select>
					</td>
				</tr>
				<tr>
					<th colspan="2"><input class="css" type=submit
						value="Create"></th>
				</tr>
			</table>
		</form>
		<br> <br> <br> <br> <br>
	</div>
</body>
</html>