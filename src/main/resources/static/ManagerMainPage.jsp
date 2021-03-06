<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="Template.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Manager</title>
<script src="../M_d_ProjectManagementSystem/jsLib/jquery-2.1.1.js"
	type="text/javascript"></script>
	<script type="text/javascript">
	
	/* To view Employees under a manager for a project */
	function view_employees(projectId, managerId)
	{
		$.getJSON('http://localhost:8080/M_d_ProjectManagementSystem/getTeamOfEmployees.do?managerId=' + managerId + '&projectId=' + projectId, 
				function(data){
			$('#taskSpace').empty();
			$('#employeeSpace').empty();
            var space = '';
            space += '<fieldset>'
            space += '<legend style="font-size: x-large;">'
            space += '<b><i>EMPLOYEES</i></b>'
            space += '</legend><br>'
            space += '<table>'
            space += '<tr>'
            space += '<th>Sr.</th>'
            space += '<th>Employee IDs</th>'
            space += '<th>Names</th>'
            space += '<th>Bandwidth</th>'
			space += '<th>View Tasks</th>'
			space += '<th>Assign Tasks</th>'
			space += '</tr>'
            for (var i = 0; i < data.length; i++){
            	space += '<tr>'
            	space += '<td>' + (i + 1) + '</td>'
            	space += '<td>' + data[i][0] + '</td>'
            	space += '<td>' + data[i][1] + '</td>'
            	space += '<td>' + data[i][2] + '</td>'
            	space += '<td>'
            	space += '<button id = ' + projectId + ' class="css" onclick="view_tasks(this.id, ' + data[i][0] + ')">View Tasks</button>'
            	space += '</td>'
            	space += '<td>'
            	space += '<button id = ' + projectId + ' class="css" onclick="assign_tasks(this.id, ' + data[i][0] + ')">Assign Tasks</button>'
            	space += '</td>'
            	space += '</tr>'
            }
            space += '</table>'
            space += '<br></fieldset>'
            $('#employeeSpace').append(space);
		});
		return false;
	}
	
	/* To view Tasks for a specific user */
	function view_tasks(projectId, userId)
	{
		$.getJSON('http://localhost:8080/M_d_ProjectManagementSystem/getEmployeeTasks.do?userId=' + userId + '&projectId=' + projectId, 
				function(data){
			$('#taskSpace').empty();
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
            for (var i = 0; i < data.length; i++){
            	space += '<tr>'
            	space += '<td>' + (i + 1) + '</td>'
            	space += '<td>' + data[i][0] + '</td>'
            	space += '<td>' + data[i][1] + '</td>'
            	space += '<td>' + data[i][2] + '</td>'
            	space += '<td><label class="css">' + data[i][3] + '</label></td>'
            	space += '</tr>'
            }
            space += '</table>'
            space += '<br></fieldset>'
            $('#taskSpace').append(space);
		});
		return false;
	}
	
	/* To view form of assigning tasks */
	function assign_tasks(projectId, employeeId)
	{
		
		document.getElementById('projectId').value = projectId;
		document.getElementById('employeeId').value = employeeId;
		
		var x = document.getElementById("assignTaskForm");
		if (x.style.display === "none") {
			x.style.display = "block";
		} else {
			x.style.display = "none";
		}
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
		if (!session.getAttribute("role").equals("manager")) {
			response.sendRedirect("getLoginPage.do");
		}
	%>
	<span class="align-right"> <a class="css" href="logout.do">Log
			Out</a></span>
			<br><br><br><br>
	<div align=center>
	
		<!-- PROJECTS -->
		<fieldset>
			<legend style="font-size: x-large;">
				<b><i>PROJECTS</i></b>
			</legend>
			<br>
			<table>
				<tr>
					<th>Sr.</th>
					<th>Project Names</th>
					<th>View Employees</th>
					<th>Status</th>
				</tr>
				<%! int i = 1; %>
				<!-- For Loop -->
				<c:forEach items="${requestScope.projects}" var="projects">

					<tr>
						<td><%= i %> <% i++; %></td>
						<td>${projects.name}</td>
						<td><button id ="${projects.id}" class="css" onclick='view_employees(this.id, "${userId}")'>View Employees</button></td>
						<td><a class = "css" href="setProjectStatus.do?id=${projects.id}&status=${projects.status}">${projects.status}</a></td>
					</tr>

				</c:forEach>
				<% i = 1; %>
			</table>
			<br>
		</fieldset>
		<br>
		<div id= "employeeSpace"></div>
		<br>
		<div id= "taskSpace"></div>
		<br>
		<div id="assignTaskForm" style="display: none;">
		
		<!-- ASSIGN TASKS -->
		<fieldset>
			<legend style="font-size: x-large;">
				<b><i>ASSIGN TASK</i></b>
			</legend>
			<br>
		
			<form action = "assignTasksToEmployees.do" method = "post">
				<input type="hidden" name="projectId" id="projectId" />
				<input type="hidden" name="employeeId" id="employeeId" />
				<table>
					<tr>
						<th>Task Name: </th><td><input required placeholder = "Task Name" type="text" 
						name="taskName" id="taskName" pattern = "[a-zA-Z0-9 ]{3,}" title = "Minimum 3 charecters" /></td>
					</tr>
					<tr>
						<th>Instructions: </th><td><input required placeholder = "Instruction" type="text" 
						name="instruction" id="instruction" pattern = "[a-zA-Z0-9 ]{3,}" title = "Minimum 3 charecters" /></td>
					</tr>
					<tr>
						<th>Estimated Hours: </th><td><input required placeholder = "Estimated Hours" 
						type="number" min = "1" max = "50" name="estimatedHours" id="estimatedHours" /></td>
					</tr>
					<tr>
						<td colspan = "2">
							<input class = "css" type = "submit">
							<input class = "css" type="reset">
						</td>
					</tr>
				</table>
			</form>
			
			<br>
		</fieldset>
		<br>
		</div>
	</div>
	<br>
	<br>
	<br>
	<br>
</body>
</html>