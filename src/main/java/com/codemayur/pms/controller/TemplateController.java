package com.codemayur.pms.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codemayur.pms.service.EmployeeService;
import com.codemayur.pms.constant.PmsConstants;
import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.entity.Task;
import com.codemayur.pms.entity.UserLogin;
import com.codemayur.pms.exception.PmsException;

/**
 * The Class TemplateController. This class handles the flow of the entire project.
 * Based on different URL's, different methods are called.
 */
@Controller
public class TemplateController {

	/** The service. */
	@Autowired
	EmployeeService service;

	/** The logger. */
	Logger logger;

	/**
	 * Instantiates a new front controller. Also gets the Logger for the class.
	 */
	public TemplateController() {
		super();
		logger = LoggerFactory.getLogger(
				TemplateController.class);
	}

	/**
	 * Gets the home page. This is done at the start of the project.
	 * 
	 * @return the home page
	 */
	@RequestMapping("/getHomePage")
	public String getHomePage() {
		logger.info(
				"Requesting Homepage");
		return "HomePage";
	}

	/**
	 * Gets the registration page.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the registration page
	 */
	@RequestMapping("/getRegistrationPage")
	public String getRegistrationPage(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting Registration");
		HttpSession session;
		try {
			session = request.getSession();

			// To check when user is logged in but is not ADMIN and tries to
			// access unauthorized page; send him to Login page.
			if (!(session.getAttribute(
					"role")).equals(
							PmsConstants.ADMIN)) {
				logger.error(
						"User is not Admin but is trying to access Registration Page.");
				request.setAttribute(
						"message",
						"Not Authorised To Access Registration Page.");
				return "Login";
			}
		}

		// To check when user has NOT logged in and tries to access unauthorized
		// page; send him to Login page.
		catch (NullPointerException | NumberFormatException e) {
			logger.error(
					"User not Logged in. ",
					e);
			request.setAttribute(
					"message",
					"Please Login first.");
			return "Login";
		}
		return "Registration";
	}

	/**
	 * Sets the employee details.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * If ADMIN wants to add a new Employee, the control goes through here and if
	 * duplicate entry is NOT found the Employee is entered.
	 */
	@RequestMapping("/setEmployeeDetails")
	public String setEmployeeDetails(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting to set Employee Details");
		HttpSession session;
		try {
			session = request.getSession();

			// To check when user is logged in but is not ADMIN and tries to
			// access unauthorized page; send him to Login page.
			if (!(session.getAttribute(
					"role")).equals(
							PmsConstants.ADMIN)) {
				logger.error(
						"User is not Admin but is trying to add new Employees.");
				request.setAttribute(
						"message",
						"Not Authorised To add Employees.");
				return "Login";
			}
		}

		// To check when user has NOT logged in and tries to access unauthorized
		// page; send him to Login page.
		catch (NullPointerException | NumberFormatException e) {
			logger.error(
					"User not Logged In. ",
					e);
			request.setAttribute(
					"message",
					"Please Login first.");
			return "Login";
		}

		// Control reaches here when user is a valid user.
		int userId, bandwidth;
		String password, role, firstName, lastName, email;
		UserLogin userLogin;
		Employee employee;

		try {

			// Get the parameters entered by the user on the Registration Form.
			password = request.getParameter(
					"password");
			role = request.getParameter(
					"role");
			firstName = request.getParameter(
					"firstName");
			lastName = request.getParameter(
					"lastName");
			email = request.getParameter(
					"email");
			bandwidth = Integer.parseInt(
					request.getParameter(
							"bandwidth"));

			// if duplicate is found, it returns true.
			// It also solves the issue of re-insertion into the DB, via a
			// refresh.
			if (service.checkDuplicate(
					email)) {
				logger.error(
						"Duplicate entry for email.");
				request.setAttribute(
						"message",
						"Duplicate entry found. Could not register.");
				return "Registration";
			}

			// Entity Objects
			userLogin = new UserLogin(
					password,
					role);
			employee = new Employee(
					firstName,
					lastName,
					email,
					bandwidth);

			// It sends user details to be set in the DB and also get the
			// Auto-Generated UserId.
			userId = service.setUserDetails(
					userLogin,
					employee);

			// Display Successful Insertion and the Auto-Generated UserId to the
			// ADMIN.
			request.setAttribute(
					"message",
					"Insertion Successful! UserId : " + userId);
		}

		// If insertion was not successful, inform the ADMIN.
		catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Insertion Failed!");
		}
		return "Registration";
	}

	/**
	 * Gets the login page.
	 *
	 * @return the login page
	 */
	/*
	 * When user requests for the Login Page show the Login Page.
	 */
	@RequestMapping("/getLoginPage")
	public String getLoginPage() {
		logger.info(
				"Requesting Login page");
		return "Login";
	}

	/**
	 * Gets the main page.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the main page
	 */
	/*
	 * Deciding which page to be shown to the user who logged in, based on his / her
	 * role as an Employee / Manager / ADMIN.
	 */
	@RequestMapping("/getMainPage")
	public String getMainPage(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Deciding Main Page");
		HttpSession session;
		int userId;
		String password, role = null;

		// check if user is Logging in for first time or has already logged in.
		try {
			session = request.getSession();

			// If user is logging in for first time he would have entered the
			// userId and password.
			if (session.getAttribute(
					"userId") == null) {
				userId = Integer.parseInt(
						request.getParameter(
								"userId"));
				password = request.getParameter(
						"password");

				// validate userId and password and get the role of the user
				// (Employee / Manager / ADMIN).
				role = service.validateLogin(
						userId,
						password);
			}

			// if user has already logged in the userId and password would be
			// already present on the session.
			else {
				userId = (int) session.getAttribute(
						"userId");
				role = (String) session.getAttribute(
						"role");
			}

		}

		// If user was NOT Logged in; send him to the Login Page.
		catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"You are not Logged In.");
			return "Login";
		}

		// No user found or userId and/ or password is invalid; send him to
		// Login page.
		if (role == null) {
			logger.error(
					"User not found");
			request.setAttribute(
					"message",
					"User not found.");
			return "Login";
		}

		// For user who is an Employee, send him to the Employee HomePage.
		else if (role.equalsIgnoreCase(
				PmsConstants.EMPLOYEE)) {
			logger.info(
					"User is Employee");
			session.setAttribute(
					"userId",
					userId);
			session.setAttribute(
					"role",
					role);
			return "WelcomeEmployee";
		}

		// For user who is an Manager, send him to the Manager HomePage.
		else if (role.equalsIgnoreCase(
				PmsConstants.MANAGER)) {
			logger.info(
					"User is Manager");
			session.setAttribute(
					"userId",
					userId);
			session.setAttribute(
					"role",
					role);
			return "WelcomeManager";
		}

		// For user who is an ADMIN, send him to the ADMIN HomePage.
		else if (role.equalsIgnoreCase(
				PmsConstants.ADMIN)) {
			logger.info(
					"User is ADMIN");
			session.setAttribute(
					"userId",
					userId);
			session.setAttribute(
					"role",
					role);
			return "WelcomeAdmin";
		}

		// unreachable code
		logger.error(
				"Something went wrong in deciding role");
		request.setAttribute(
				"message",
				"Something went wrong!");
		return "HomePage";
	}

	/**
	 * Gets the employee main page.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the employee main page
	 */
	/*
	 * Generating the Employee Main Page. 1. Check user role == Employee. (if false,
	 * send to Login) 2. Get the Projects specific to the Employee. 3. Get the
	 * current Workload of the Employee. 4. Send the data to the Employee Main Page.
	 */
	@RequestMapping("/getEmployeeMainPage")
	public String getEmployeeMainPage(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting Employee Main Page");

		HttpSession session;
		int userId, workload;
		Set<Project> projects = null;

		try {
			session = request.getSession();
			// If user role is NOT Employee
			if (!(session.getAttribute(
					"role")).equals(
							PmsConstants.EMPLOYEE)) {
				logger.error(
						"Unauthorized user wants to access Employee Page.");
				request.setAttribute(
						"message",
						"Not Authorised To access Employee Page.");
				return "Login";
			}

			// If user role is Employee, get Projects and Workload for Employee
			// Main page
			userId = (int) session.getAttribute(
					"userId");

			// Get Employee Specific projects
			projects = service.getProjects(
					userId);

			// Get the Employee's current workload.
			workload = service.getWorkload(
					userId);

			// Send data to the JSP page.
			request.setAttribute(
					"projects",
					projects);
			request.setAttribute(
					"workload",
					workload);

		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Not Authorised To access Employee Page.");
			return "Login";
		}
		return "EmployeeMainPage";
	}

	/**
	 * Sets the task status.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * When the Employee has finished a Task he can set the task's status from
	 * ACTIVE to COMPLETED. 1. Check is user is an Employee.
	 */
	@RequestMapping("/setTaskStatus")
	public String setTaskStatus(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting to change Employee Task Status");
		HttpSession session;
		int taskId;
		String status;

		try {
			session = request.getSession();
			// if user role is NOT Employee, return Login Page.
			if (!(session.getAttribute(
					"role")).equals(
							PmsConstants.EMPLOYEE)) {
				logger.error(
						"Unauthorized user wants to access Employee Page.");
				request.setAttribute(
						"message",
						"Not Authorised to set task.");
				return "Login";
			}

			// When user click the Button, TaskId and Status is sent as
			// parameters, so get them as they are required to change the
			// respective task's status.
			taskId = Integer.parseInt(
					request.getParameter(
							"id"));
			status = request.getParameter(
					"status");

			String message = service.changeTaskStatus(
					taskId,
					status);
			request.setAttribute(
					"message",
					message);
		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Not Authorised to set task.");
			return "Login";
		}

		return "WelcomeEmployee";
	}

	/**
	 * Gets the manager main page.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the manager main page
	 */
	/*
	 * Generating Manager Main Page. 1. Check if User is a Manager; if not return
	 * Login Page. 2. Get the Projects under the Manager and pass it to the Manager
	 * Main Page.
	 */
	@RequestMapping("/getManagerMainPage")
	public String getManagerMainPage(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting Manager Main Page");

		HttpSession session;
		int userId;
		Set<Project> projects = null;

		try {
			session = request.getSession();
			// if user role is NOT manager
			if (!session.getAttribute(
					"role")
					.equals(
							PmsConstants.MANAGER)) {
				logger.error(
						"Unauthorised user wants to access Manager Page.");
				request.setAttribute(
						"message",
						"Not Authorised To access Manager Page.");
				return "Login";
			}
			userId = (int) session.getAttribute(
					"userId");
			projects = service.getProjects(
					userId);
			request.setAttribute(
					"projects",
					projects);
		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Not Authorised To access Manager Page.");
			return "Login";
		}

		return "ManagerMainPage";
	}

	/**
	 * Sets the project status.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * The Manager can Change the Status of the Project from ACTIVE to COMPLETED.
	 * This can be done only when all the tasks under the Project are set as
	 * COMPLETED. The manager is not able to change the status of the Project if
	 * even a single task of the Project is set as ACTIVE. 1. Check if user role ==
	 * Manager; if NOT return Login Page. 2. Get the Project Id and status of the
	 * Project and send it to the Service Layer.
	 */
	@RequestMapping("/setProjectStatus")
	public String setProjectStatus(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting to change Manager Project Status");
		HttpSession session;
		int projectId;
		String status;

		try {
			session = request.getSession();
			// if role is not manager
			if (!session.getAttribute(
					"role")
					.equals(
							PmsConstants.MANAGER)) {
				logger.error(
						"Unauthorised User wants to change Project Status.");
				request.setAttribute(
						"message",
						"Not Authorised to change project status.");
				return "Login";
			}

			projectId = Integer.parseInt(
					request.getParameter(
							"id"));
			status = request.getParameter(
					"status");

			// 'true' is returned when the status is successfully changed to
			// COMPLETED.
			if (service.changeProjectStatus(
					projectId,
					status)) {
				request.setAttribute(
						"message",
						"Project Completed");
			}

			// 'false' is returned when the status could not be change or it was
			// already set to COMPLETE.
			else {
				logger.error(
						"Could not change status as some tasks are not complete or the project is already complete.");
				request.setAttribute(
						"message",
						"Could not change Status as some tasks or not complete or the project is already complete.");
			}
		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Could not Update Project Status");
			return "ErrorPage";
		}

		return "WelcomeManager";
	}

	/**
	 * Assign tasks to employees.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * A Manager can assign tasks to its Employees. This method is called when the
	 * Manager assigns a task to the Employee. 1. Check if User role == Manager, if
	 * NOT return Login Page. 2. Assign Task to Employee by sending the required
	 * data to the Service Layer. 3. Get the Updated Workload of the Employee Now.
	 * 4. If the workload == 99, task was assigned to the user. 5. If workload is
	 * anything but 99, the task was not assigned and that means the conditions of
	 * bandwidth and workload were not satisfied. 6. Display appropriate messages to
	 * the User.
	 */
	@RequestMapping("/assignTasksToEmployees")
	public String assignTasksToEmployees(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting page to assign Task to Employees");

		HttpSession session;
		int projectId, employeeId, estimatedHours, workload;
		String name, instructions;
		Employee employee;
		Project project;
		Task task;

		try {
			session = request.getSession();
			// user role is not manager
			if (!session.getAttribute(
					"role")
					.equals(
							PmsConstants.MANAGER)) {
				request.setAttribute(
						"message",
						"Not Authorised To Access Registration Page.");
				return "Login";
			}

			projectId = Integer.parseInt(
					request.getParameter(
							"projectId"));
			employeeId = Integer.parseInt(
					request.getParameter(
							"employeeId"));
			name = request.getParameter(
					"taskName");
			instructions = request.getParameter(
					"instruction");
			estimatedHours = Integer.parseInt(
					request.getParameter(
							"estimatedHours"));

			// Creating Objects
			employee = service.getEmployeeDetails(
					employeeId);
			project = service.getProjectDetails(
					projectId);
			task = new Task(
					name,
					instructions,
					estimatedHours,
					employee,
					project);

			// get workload
			workload = service.setTask(
					task);

			// if workload == 99, task is assigned else it's not assigned
			if (workload != 99) {
				logger.error(
						"Could not assign task, as workload is exceeding bandwidth.");
				request.setAttribute(
						"message",
						"Could not assign Task as Workload " + workload + " hours is exceeding bandwidth.");
			} else {
				logger.error(
						"Task assigned.");
				request.setAttribute(
						"message",
						"Task Assigned.");
			}

		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			e.printStackTrace();
			request.setAttribute(
					"message",
					"Not Authorised to assign tasks to Employees");
			return "Login";
		}

		return "WelcomeManager";
	}

	/**
	 * Gets the new project.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the new project
	 */
	/*
	 * The ADMIN can create a new Project. This method returns the Form for the
	 * ADMIN to choose from the existing Managers and Employees. 1. Check if User is
	 * ADMIN; if NOT return Login Page. 2. Get all the Managers in the System. 3.
	 * Get all the Employees in the System. 3. Send this data to the New Project
	 * Page.
	 */
	@RequestMapping("/getNewProject")
	public String getNewProject(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting getNewProject");

		HttpSession session;

		try {
			session = request.getSession();
			// if user is not ADMIN
			if (!session.getAttribute(
					"role")
					.equals(
							PmsConstants.ADMIN)) {
				request.setAttribute(
						"message",
						"Not Authorised To Add New Project.");
				return "Login";
			}

			List<Employee> managers = service.getAllManagers();
			List<Employee> employees = service.getAllEmployees();

			request.setAttribute(
					"managers",
					managers);
			request.setAttribute(
					"employees",
					employees);
		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Not Authorised To Add New Project.");
			return "Login";
		}

		return "NewProject";
	}

	/**
	 * Sets the new project.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * The ADMIN can create a new Project. This method gets the data entered by the
	 * ADMIN and
	 */
	@RequestMapping("/setNewProject")
	public String setNewProject(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting getNewProject");

		HttpSession session;

		try {
			session = request.getSession();
			if (!session.getAttribute(
					"role")
					.equals(
							PmsConstants.ADMIN)) {
				request.setAttribute(
						"message",
						"Not Authorised To add Employees.");
				return "Login";
			}
		} catch (NullPointerException | NumberFormatException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Not Authorised To add Employees.");
			return "Login";
		}

		String projectName;
		int manager;
		String[] employees;

		try {
			projectName = request.getParameter(
					"projectName");
			manager = Integer.parseInt(
					request.getParameter(
							"manager"));
			employees = request.getParameterValues(
					"employees");

			// check if a project with same name exists or not. It returns true
			// if a Project with same name exists, else it returns false.
			if (service.checkDuplicateProject(
					projectName)) {
				request.setAttribute(
						"message",
						"Duplicate Entry for Project!");
			} else {
				Project project = new Project(
						projectName);
				service.createProject(
						project,
						manager,
						employees);
				// send request messages to JSP
				request.setAttribute(
						"message",
						"Insertion Successful!");
			}
		} catch (NullPointerException | NumberFormatException | PmsException e) {
			logger.error(
					e.getMessage(),
					e);
			request.setAttribute(
					"message",
					"Insertion Failed!");
		}
		return "Registration";
	}

	/**
	 * Logout.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the string
	 */
	/*
	 * Every Page once Logged In has a Logout Button. This Method thus Invalidates
	 * the session and hence the data stored on the session is cleared.
	 */
	@RequestMapping("/logout.do")
	public String logout(
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info(
				"Requesting to Log Out");

		HttpSession session = request.getSession();
		session.invalidate();

		return "HomePage";
	}
}
