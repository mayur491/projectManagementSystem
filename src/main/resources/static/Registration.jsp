<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="Template.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration</title>
<script>
function myFunction() {
    var x = document.getElementById("email");
    x.value = x.value.toLowerCase();
}
</script>
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
		<h2>REGISTRATION</h2>
		<br>
		<font size=5>${message}</font>
			<br><br>
		<form method="POST" action="setEmployeeDetails.do">
			<table>
				<tr>
					<th>First Name:</th>
					<td><input required type="text" name="firstName"
						placeholder="First Name" pattern="[a-zA-Z]{3,}"
						title="Atleast 3 charecters. All Alphabets."></td>
				</tr>
				<tr>
					<th>Last Name:</th>
					<td><input required type="text" name="lastName"
						placeholder="Last Name" pattern="[a-zA-Z]{3,}"
						title="Atleast 3 charecters. All Alphabets."></td>
				</tr>
				<tr>
					<th>Email:</th>
					<td><input required type="text" id = "email" name="email"
						placeholder="Email" onkeyup="myFunction()" 
						pattern="[A-Za-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"
						title="ex: Abc123@abc.abc"></td>
				</tr>
				<tr>
					<th>Password:</th>
					<td><input required type="password" name="password"
						placeholder="password" pattern="[a-zA-Z0-9]{8,}"
						title="Minimum 8 charecters."></td>
				</tr>
				<tr>
					<th>Bandwidth:</th>
					<td><input required type="number" name="bandwidth"
						placeholder="bandwidth" pattern="[0-9]{1,2}" min="1" max="50"
						title="Min: 1. Max = 50"></td>
				</tr>
				<tr>
					<th>Role:</th>
					<td><select class="css" name=role required>
							<option disabled selected value>-- select an option --</option>
							<option value="employee">Employee</option>
							<option value="manager">Manager</option>
							<option value="admin">Admin</option>
					</select></td>
				</tr>
				<tr>
					<th colspan="2"><input class="css" type=submit
						value="Register"></th>
				</tr>
			</table>
		</form>
		<br> <br> <br> <br> <br>
	</div>
</body>
</html>