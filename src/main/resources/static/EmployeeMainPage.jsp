<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="Template.jsp" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee</title>
<script src="../M_d_ProjectManagementSystem/jsLib/jquery-2.1.1.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function view_projects(projectId, userId) {
		$.getJSON(
				'http://localhost:8080/M_d_ProjectManagementSystem/getEmployeeTasks.do?userId='
						+ userId + '&projectId=' + projectId, function(data) {
					$('#spaceDetails').empty();
					var space = '';
					space += '<fieldset>'
					space += '<legend style="font-size: x-large;">'
					space += '<b><i>TASKS</i></b>'
					space += '</legend><br>'
					space += '<table>'
					space += '<tr>'
					space += '<th>Sr.</th>'
					space += '<th>Task Names</th>'
					space += '<th>Instructions</th>'
					space += '<th>Estimated Hours</th>'
					space += '<th>Status</th>'
					space += '</tr>'
					for (var i = 0; i < data.length; i++) {
						space += '<tr>'
						space += '<td>' + (i + 1) + '</td>'
						space += '<td>' + data[i][0] + '</td>'
						space += '<td>' + data[i][1] + '</td>'
						space += '<td>' + data[i][2] + '</td>'
						space += '<td>'
						space += '<a class = "css" href="setTaskStatus.do?id='
								+ data[i][4] + '&status=' + data[i][3] + '">'
								+ data[i][3] + '</a>'
						space += '</td>'
						space += '</tr>'
					}
					space += '</table>'
					space += '<br></fieldset>'
					$('#spaceDetails').append(space);
				});
		return false;
	}
</script>
</head>
<body>
	<%
	response.setHeader(
			"Cache-Control",
			"no-cache, no-store, must-revalidate");
	// HTTP 1.1 
	response.setHeader(
			"Pragma",
			"no-cache");
	// HTTP 1.0 
	response.setHeader(
			"Expires",
			"0");
	// Proxies
	if (!session.getAttribute(
			"role")
			.equals(
			"employee")) {
		response.sendRedirect(
		"getLoginPage.do");
	}
	%>
	<div class="align-right">
		<a class="css" href="logout.do">Log Out</a>
	</div>
	<h3>Workload : ${requestScope.workload} hours</h3>
	<br>
	<div align=center>
		<fieldset>
			<legend style="font-size: x-large;">
				<strong><i>PROJECTS</i></strong>
			</legend>
			<br>
			<table>
				<tr>
					<th>Sr.</th>
					<th>Project Names</th>
					<th>Status</th>
				</tr>
				<%!int i = 1;%>
				<!-- For Loop -->
				<c:forEach items="${requestScope.projects}" var="projects">
					<tr>
						<td><%=i%> <% i++; %></td>
						<td>${projects.name}</td>
						<td><button id="${projects.id}" class="css"
								onclick='view_projects(this.id, "${userId}")'>${projects.status}</button></td>
					</tr>
				</c:forEach>
				<% i = 1; %>
			</table>
			<br>
		</fieldset>
		<br>
		<div id="spaceDetails"></div>
	</div>
	<br>
	<br>
	<br>
	<br>
</body>
</html>