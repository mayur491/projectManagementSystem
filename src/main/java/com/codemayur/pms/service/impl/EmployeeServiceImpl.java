package com.codemayur.pms.service.impl;

import static com.codemayur.pms.constant.PmsConstants.ACTIVE;
import static com.codemayur.pms.constant.PmsConstants.COMPLETED;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codemayur.pms.dao.EmployeeDao;
import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.entity.Task;
import com.codemayur.pms.entity.UserLogin;
import com.codemayur.pms.exception.PmsException;
import com.codemayur.pms.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeDao dao;

	/*
	 * Get all the Employees present in the System from the database.
	 */
	@Override
	public List<Employee> getAllEmployees() throws PmsException {
		return dao.getAllEmployees();
	}

	/*
	 * Get all the Managers present in the System from the database.
	 */
	@Override
	public List<Employee> getAllManagers() throws PmsException {
		return dao.getAllManagers();
	}

	/*
	 * Validate userId and Password of the user. 1. If valid, return 'role' of the
	 * user. 2. If NOT valid, return 'NULL'.
	 */
	@Override
	public String validateLogin(
			int userId,
			String password) throws PmsException {
		return dao.getRole(
				userId,
				password);
	}

	/*
	 * Get a specific Employee's Details.
	 */
	@Override
	public Employee getEmployeeDetails(
			int userId) throws PmsException {
		return dao.getEmployeeDetails(
				userId);
	}

	/*
	 * Get a specific Project's details.
	 */
	@Override
	public Project getProjectDetails(
			int projectId) throws PmsException {
		return dao.getProjectDetails(
				projectId);
	}

	/*
	 * Get all the Projects that are related to a user (Employee / Manager)
	 */
	@Override
	public Set<Project> getProjects(
			int userId) throws PmsException {
		
		Employee emp = dao.getEmployeeDetails(
				userId);
		return emp.getProjects();
	}

	/*
	 * An Employee can change the status of his task from ACTIVE to COMPLETED. 1.
	 * Check if Status is ACTIVE, then change it to COMPLETED. 2. If Status is
	 * already COMPLETED, return respective message. 3. If its anything else, return
	 * Invalid Status message.
	 */
	@Override
	public String changeTaskStatus(
			int taskId,
			String status) throws PmsException {

		if (status.equalsIgnoreCase(
				ACTIVE)) {
			status = COMPLETED;
			dao.changeTaskStatus(
					taskId,
					status);
			return "Changed Status";
		}
		// TESTING
		// else if (status.equalsIgnoreCase(COMPLETED)) {
		// status = ACTIVE;
		// }

		else if (status.equalsIgnoreCase(
				COMPLETED)) {
			return "Already Completed";
		}

		else {
			return "Invalid Status";
		}
	}

	// get Employee Specific Tasks
	@Override
	public List<Task> getEmployeeTasks(
			int userId,
			int projectId) throws PmsException {
		return dao.getEmployeeTasks(
				userId,
				projectId);
	}

	/*
	 * Register a User in the System Database. 1. Generate a new UserId. 2. Pass all
	 * the user details to the DAO Layer, and register the User.
	 */
	@Override
	public int setUserDetails(
			UserLogin userLogin,
			Employee employee) throws PmsException {

		// Get the maximum UserId and increment it by 1, to get a new UserId.
		int userId = dao.getMaxUserId();
		userId = userId + 1;

		userLogin.setUserId(
				userId);
		employee.setEmployeeId(
				userId);
		dao.setEmployee(
				userLogin,
				employee);

		return userId;
	}

	/*
	 * For a Manager there can be many Employees under him. This method gets all of
	 * the Manager specific Employees.
	 */
	@Override
	public List<Employee> getTeamOfEmployees(
			int managerId,
			Integer projectId) throws PmsException {
		return dao.getTeamOfEmployees(
				managerId,
				projectId);
	}

	/*
	 * A Project's status can be changed from ACTIVE to COMPLETED by the manager
	 * only; and that too only when all the tasks under the project are completed.
	 * This method verifies the project specific task's status and changes the
	 * Project's status if the necessary conditions are met.
	 */
	@Override
	public boolean changeProjectStatus(
			int projectId,
			String status) throws PmsException {

		List<Task> tasks = dao.getProjectTasks(
				projectId);

		@SuppressWarnings("rawtypes")
		Iterator iterator = tasks.iterator();

		while (iterator.hasNext()) {
			Object[] row = (Object[]) iterator.next();
			if (!((String) row[1]).equalsIgnoreCase(
					COMPLETED)) {
				return false;
			}
		}

		if (status.equalsIgnoreCase(
				ACTIVE)) {
			status = "COMPLETED";
			dao.changeProjectStatus(
					projectId,
					status);
		}
		// Testing
		// else if (status.equalsIgnoreCase(COMPLETED)) {
		// status = ACTIVE;
		// }

		return true;
	}

	/*
	 * The manager can allot new tasks to the Employees under him. This method
	 * allots tasks to the Employees by a manager based on the Workload of the
	 * Employee. The Employee can be assigned a new Task only if his workload is not
	 * exceeded by the new task; above the Employee's Bandwidth. 1. Get Employee's
	 * Id and Bandwidth. 2. Calculate Employee's Workload. 3. Check if Estimated
	 * Hours of the Task exceeds the Employee's Bandwidth or not. 4. If it does not
	 * exceed the Employees bandwidth, assign him the task. (and return '99' to let
	 * the controller know that the task was assigned) 5. If it does exceed the
	 * Employee's bandwidth, inform the Manager that he cannot allot a new task to
	 * that Employee. (and return the actual workload).
	 */
	@Override
	public int setTask(
			Task task) throws PmsException {

		int employeeId = task.getEmployee().getEmployeeId();
		int bandwidth = task.getEmployee().getBandwidth();
		@SuppressWarnings("rawtypes")
		List workLoads = dao.getWorkLoad(
				employeeId);
		int workload = 0;

		@SuppressWarnings("rawtypes")
		Iterator iterator = workLoads.iterator();

		while (iterator.hasNext()) {
			Object row = (Object) iterator.next();
			workload += (int) row;
		}

		if (task.getEstimatedHours() <= (bandwidth - workload)) {
			task.setStatus(
					ACTIVE);
			int taskId = dao.getMaxTaskId() + 1;
			task.setId(
					taskId);
			dao.setTask(
					task);
			return 99;
		}
		return workload;
	}

	/*
	 * The ADMIN can create a new Project. 1. Generate the Project Id. 2. Set the
	 * status as ACTIVE. 3. Send the Project details: Assigned Manager and the
	 * Assigned Employees under the project; to the DAO layer.
	 */
	@Override
	public void createProject(
			Project project,
			int manager,
			String[] employees) throws PmsException {
		int projectId = dao.getMaxProjectId() + 1;
		project.setId(
				projectId);
		project.setStatus(
				ACTIVE);
		dao.createProject(
				project,
				manager,
				employees);
	}

	/*
	 * Every Employee has a Workload in Hours. Get that Workload from the database.
	 * It is the addition of all the Estimated Hours of all the ACTIVE tasks under
	 * the user.
	 */
	@Override
	public int getWorkload(
			int userId) throws PmsException {

		@SuppressWarnings("rawtypes")
		List workLoads = dao.getWorkLoad(
				userId);
		int workload = 0;

		@SuppressWarnings("rawtypes")
		Iterator iterator = workLoads.iterator();

		while (iterator.hasNext()) {
			Object row = iterator.next();
			workload += (int) row;
		}

		return workload;
	}

	/*
	 * Checking whether a user's details are already present in the Database or not,
	 * before Inserting the details in the Database.
	 */
	@Override
	public boolean checkDuplicate(
			String email) throws PmsException {

		// if duplicate not found return false
		if (dao.checkDuplicate(
				email) == null) {
			return false;
		}
		// if duplicate found return true
		return true;
	}

	/*
	 * Checking whether a Project already exists with the same name in Database,
	 * before creating the Project.
	 */
	@Override
	public boolean checkDuplicateProject(
			String projectName) throws PmsException {
		// if duplicate not found return false
		if (dao.checkDuplicateProject(
				projectName) == null) {
			return false;
		}
		// if duplicate found return true
		return true;
	}

}
