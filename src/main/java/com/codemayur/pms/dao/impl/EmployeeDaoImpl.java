package com.codemayur.pms.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.codemayur.pms.dao.EmployeeDao;
import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.entity.Task;
import com.codemayur.pms.entity.UserLogin;
import com.codemayur.pms.exception.PmsException;
import com.codemayur.pms.constant.PmsConstants;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	SessionFactory sFactory;

	/*
	 * This method returns the UserLogin Objects in a List of all the Users whose
	 * role is set to 'employee'
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllEmployees() throws PmsException {
		Session session = null;
		List<Employee> employeeList;
		try {
			session = sFactory.openSession();
			Query qryForAll = session.createQuery(
					"from UserLogin WHERE ROLE = :employee");
			qryForAll.setParameter(
					"employee",
					PmsConstants.EMPLOYEE);
			employeeList = qryForAll.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Procuring list of Employee encountered a problem.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return employeeList;
	}

	/*
	 * This method returns the UserLogin Objects in a List of all the Users whose
	 * role is set to 'manager'
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllManagers() throws PmsException {
		Session session = null;
		List<Employee> managerList;
		try {
			session = sFactory.openSession();
			Query qryForAll = session.createQuery(
					"from UserLogin WHERE ROLE = :manager");
			qryForAll.setParameter(
					"manager",
					PmsConstants.MANAGER);
			managerList = qryForAll.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Manager present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Procuring list of Managers encountered a problem.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return managerList;
	}

	/*
	 * Every User has a Role as an Employee/ Manager/ ADMIN. Get that Role for a
	 * specific user in this method.
	 */
	@Override
	public String getRole(
			int userId,
			String password) throws PmsException {
		Session session = null;
		UserLogin user;
		try {
			session = sFactory.openSession();
			user = session.get(
					UserLogin.class,
					userId);
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		if (user != null && user.getPassword()
				.equals(
						password)) {
			return user.getRole();
		}
		return null;
	}

	/*
	 * For a specific user get the required details of the user from the database.
	 */
	@Override
	public Employee getEmployeeDetails(
			int employeeId) throws PmsException {
		Session session = null;
		Employee employee = null;
		try {
			session = sFactory.openSession();
			employee = session.get(
					Employee.class,
					employeeId);
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return employee;
	}

	/*
	 * Every Employee can change the status of the task he/ she is alloted. This
	 * message changes the task status of a specific task of the Employee.
	 */
	@Override
	public void changeTaskStatus(
			int taskId,
			String status) throws PmsException {
		Session session = null;
		Transaction trans = null;
		try {
			session = sFactory.openSession();
			trans = session.beginTransaction();
			Task task = (Task) session.get(
					Task.class,
					taskId);
			task.setStatus(
					status);
			trans.commit();
		} catch (HibernateException e) {
			if (trans != null) {
				trans.rollback();
			}
			throw new PmsException(
					"Encountered problem while updating Task Status.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * Every Employee has a set of tasks alloted to him / her only. This method gets
	 * those tasks of the specific Employee.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getEmployeeTasks(
			int employeeId,
			int projectId) throws PmsException {
		Session session = null;
		Query<Task> query;
		List<Task> tasks;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT NAME, INSTRUCTIONS, ESTIMATED_HOURS, STATUS, TASK_ID FROM M_D_TASK WHERE EMPLOYEE_ID = ? AND PROJECT_ID = ?;");
			query.setParameter(
					0,
					employeeId);
			query.setParameter(
					1,
					projectId);
			tasks = query.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return tasks;
	}

	/*
	 * Get the Maximum UserId present in the Database. If no userId is present,
	 * return '2999'(To generate UserIds from 3000)
	 */
	@Override
	public int getMaxUserId() throws PmsException {
		Session session = null;
		Query query;
		int userId = 0;
		try {
			session = sFactory.openSession();
			query = session.createQuery(
					"SELECT MAX(employeeId) FROM Employee");
			@SuppressWarnings("unchecked")
			List<Integer> list = query.list();
			userId = list.get(
					0);
		} catch (NullPointerException e) {
			// When the Database has no userId's, return default value
			return 2999;
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return userId;
	}

	/*
	 * When User Registration is to be done, the details of the user needs to be
	 * saved in all the necessary tables in the Database. This method saves the user
	 * details in all the required tables, and roll-backs if any single one of them
	 * fails.
	 */
	@Override
	public void setEmployee(
			UserLogin userLogin,
			Employee employee) throws PmsException {
		Session session = null;
		Transaction trans = null;
		try {
			session = sFactory.openSession();
			trans = session.beginTransaction();
			session.save(
					userLogin);
			session.save(
					employee);
			trans.commit();
		} catch (HibernateException e) {
			trans.rollback();
			throw new PmsException(
					"Problem in inserting a new record.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * Every manager has a set of Employees under him. This method returns all the
	 * Employees under a specific Manager in a List.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getTeamOfEmployees(
			int managerId,
			int projectId) throws PmsException {
		Session session = null;
		Query query;
		List<Employee> employees;
		try {
			session = sFactory.openSession();

			String sql = "SELECT M_D_EMPLOYEE.EMPLOYEE_ID, FIRSTNAME, BANDWIDTH ";
			sql += "FROM M_D_EMPLOYEE, M_D_EMPLOYEE_PROJECT_RELATION, M_D_MANAGER_EMPLOYEE_RELATION ";
			sql += "WHERE M_D_EMPLOYEE.EMPLOYEE_ID = M_D_EMPLOYEE_PROJECT_RELATION.EMPLOYEE_ID ";
			sql += "AND M_D_EMPLOYEE.EMPLOYEE_ID = M_D_MANAGER_EMPLOYEE_RELATION.EMPLOYEE_ID ";
			sql += "AND M_D_EMPLOYEE_PROJECT_RELATION.PROJECT_ID = ? ";
			sql += "AND M_D_MANAGER_EMPLOYEE_RELATION.MANAGER_ID = ? ; ";

			query = session.createSQLQuery(
					sql);
			query.setInteger(
					0,
					projectId);
			query.setInteger(
					1,
					managerId);
			employees = query.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee found.",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return employees;
	}

	/*
	 * A manager can change the status of the Project when all the tasks under that
	 * project has been set to COMPLETE. This method changes the status of the
	 * Project.
	 */
	@Override
	public void changeProjectStatus(
			int projectId,
			String status) throws PmsException {
		Session session = null;
		Transaction trans = null;
		try {
			session = sFactory.openSession();
			trans = session.beginTransaction();
			Project project = (Project) session.get(
					Project.class,
					projectId);
			project.setStatus(
					status);
			trans.commit();
		} catch (HibernateException e) {
			trans.rollback();
			throw new PmsException(
					"Encountered problem while updating Project Status.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * Every Project has a set of Tasks under it. This method returns the List of
	 * Project specific Tasks in a List.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getProjectTasks(
			int projectId) throws PmsException {
		Session session = null;
		Query query;
		List<Task> tasks;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT TASK_ID, STATUS FROM M_D_TASK WHERE PROJECT_ID = ?;");
			query.setInteger(
					0,
					projectId);
			tasks = query.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return tasks;
	}

	/*
	 * This method returns the details of a specific Project.
	 */
	@Override
	public Project getProjectDetails(
			int projectId) throws PmsException {
		Session session = null;
		Project project = null;
		try {
			session = sFactory.openSession();
			project = (Project) session.get(
					Project.class,
					projectId);
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return project;
	}

	/*
	 * Every Employee has a workload that can be calculated by adding all the
	 * Employee's task's estimated hours where the status of the task is set to
	 * ACTIVE. This method returns the List of task's estimated hours.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List getWorkLoad(
			int employeeId) throws PmsException {
		Session session = null;
		Query query;
		List workload;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT ESTIMATED_HOURS FROM M_D_TASK WHERE EMPLOYEE_ID = ? AND STATUS = 'ACTIVE'");
			query.setInteger(
					0,
					employeeId);
			workload = query.list();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Employee with this Id present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return workload;
	}

	/*
	 * A Manager can assign a task to any Employee. This method saves the task
	 * details in respective tables in the databases.
	 */
	@Override
	public void setTask(
			Task task) throws PmsException {
		Session session = null;
		Transaction trans = null;
		try {
			session = sFactory.openSession();
			trans = session.beginTransaction();
			session.save(
					task);
			trans.commit();
		} catch (HibernateException e) {
			trans.rollback();
			throw new PmsException(
					"Problem in inserting a new record.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * This method returns the Max TaskId from the Database.
	 */
	@Override
	public int getMaxTaskId() throws PmsException {
		Session session = null;
		Query query;
		int taskId;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT MAX(TASK_ID) FROM M_D_TASK;");
			taskId = (int) query.uniqueResult();
		} catch (NullPointerException e) {
			// When there is not task present in the Database, return the
			// default value for the taskId.
			return 0;
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return taskId;
	}

	/*
	 * This method returns the Max ProjectId from the Database.
	 */
	@Override
	public int getMaxProjectId() throws PmsException {
		Session session = null;
		Query query;
		int taskId;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT MAX(PROJECT_ID) FROM M_D_PROJECT;");
			taskId = (int) query.uniqueResult();
		} catch (NullPointerException e) {
			// When there is not Project present in the Database, return the
			// default value for the projectId.
			return 0;
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return taskId;
	}

	/*
	 * This method creates a Project by setting the details of the project in
	 * respective tables in the database.
	 */
	@Override
	public void createProject(
			Project project,
			int manager,
			String[] employees) throws PmsException {
		Session session = null;
		Transaction trans = null;
		Employee employee;
		Query query;
		int projectId = project.getId();
		try {
			session = sFactory.openSession();
			trans = session.beginTransaction();
			session.save(
					project);

			// assign project to manager
			employee = (Employee) session.get(
					Employee.class,
					manager);
			Set<Project> projects = employee.getProjects();
			projects.add(
					project);
			session.save(
					employee);

			for (int i = 0; i < employees.length; i++) {

				query = session.createSQLQuery(
						"INSERT IGNORE INTO M_D_MANAGER_EMPLOYEE_RELATION(MANAGER_ID, EMPLOYEE_ID) VALUES(?, ?);");
				query.setInteger(
						0,
						manager);
				query.setInteger(
						1,
						Integer.parseInt(
								employees[i]));
				query.executeUpdate();

			}
			trans.commit();
			// check if there is any other way not to commit so that everything
			// happens in one transaction.
			trans = session.beginTransaction();

			for (int i = 0; i < employees.length; i++) {

				query = session.createSQLQuery(
						"INSERT IGNORE INTO M_D_EMPLOYEE_PROJECT_RELATION(PROJECT_ID, EMPLOYEE_ID) VALUES(?, ?);");
				query.setInteger(
						0,
						projectId);
				query.setInteger(
						1,
						Integer.parseInt(
								employees[i]));
				query.executeUpdate();

			}

			trans.commit();
		} catch (HibernateException e) {
			trans.rollback();
			throw new PmsException(
					"Problem in inserting a new record.",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/*
	 * Check for duplicate entry for a User.
	 */
	@Override
	public String checkDuplicate(
			String email) throws PmsException {
		Session session = null;
		Query query;
		String check;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT EMAIL FROM M_D_EMPLOYEE WHERE EMAIL = ?;");
			query.setString(
					0,
					email);
			check = (String) query.uniqueResult();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Task present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Employee Details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return check;
	}

	/*
	 * Check for Duplicate Entry for a Project.
	 */
	@Override
	public Object checkDuplicateProject(
			String projectName) throws PmsException {
		Session session = null;
		Query query;
		String check;
		try {
			session = sFactory.openSession();
			query = session.createSQLQuery(
					"SELECT NAME FROM M_D_PROJECT WHERE NAME = ?;");
			query.setString(
					0,
					projectName);
			check = (String) query.uniqueResult();
		} catch (NullPointerException e) {
			throw new PmsException(
					"No Project present",
					e);
		} catch (HibernateException e) {
			throw new PmsException(
					"Could not get Project details",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return check;
	}
}
