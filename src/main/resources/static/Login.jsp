<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:include page="Template.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<div align="center">
		<h2>LOGIN</h2>
		<form action="getMainPage.do" method="post">
			<table>
				<tr>
					<th>USER ID :</th>
					<td><input required type="text" name="userId"
						placeholder="User Id" pattern = "[1-9]{1}[0-9]{3}" title = "4 digit number. Not starting with Zero ( 0 )."></td>
				</tr>
				<tr>
					<th>PASSWORD :</th>
					<td><input required type="password" name="password"
						placeholder="Password" pattern = "[a-zA-Z0-9]{8,}" title = "Min. 8 charecters"></td>
				</tr>
				<tr>
					<th colspan="2"><input class="css" type=submit value="Login"></th>
				</tr>
			</table>
			<br> <font color="red"><i><b>${message}</b></i></font>
		</form>
	</div>
</body>
</html>