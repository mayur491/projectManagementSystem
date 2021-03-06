package com.codemayur.pms.service;

import java.util.List;
import java.util.Set;

import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.entity.Task;
import com.codemayur.pms.entity.UserLogin;
import com.codemayur.pms.exception.PmsException;

public interface EmployeeService {

	String validateLogin(
			int userId,
			String password) throws com.codemayur.pms.exception.PmsException;

	Employee getEmployeeDetails(
			int userId) throws PmsException;

	Set<Project> getProjects(
			int userId) throws PmsException;

	String changeTaskStatus(
			int taskId,
			String status) throws PmsException;

	List<Task> getEmployeeTasks(
			int userId,
			int projectId) throws PmsException;

	int setUserDetails(
			UserLogin userLogin,
			Employee employee) throws PmsException;

	List<Employee> getTeamOfEmployees(
			int managerId,
			Integer projectId) throws PmsException;

	boolean changeProjectStatus(
			int projectId,
			String status) throws PmsException;

	int setTask(
			Task task) throws PmsException;

	Project getProjectDetails(
			int projectId) throws PmsException;

	List<Employee> getAllEmployees() throws PmsException;

	List<Employee> getAllManagers() throws PmsException;

	void createProject(
			Project project,
			int manager,
			String[] employees) throws PmsException;

	int getWorkload(
			int userId) throws PmsException;

	boolean checkDuplicate(
			String email) throws PmsException;

	boolean checkDuplicateProject(
			String projectName) throws PmsException;

}
