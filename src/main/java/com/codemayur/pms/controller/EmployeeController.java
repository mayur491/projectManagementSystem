package com.codemayur.pms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Task;
import com.codemayur.pms.exception.PmsException;
import com.codemayur.pms.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@GetMapping("/getEmployeeTasks")
	public ResponseEntity<List<Task>> getEmployeeTasks(
			@RequestParam(value = "userId") int userId,
			@RequestParam(value = "projectId") Integer projectId) throws PmsException {

		List<Task> tasks = service.getEmployeeTasks(
				userId,
				projectId);
		return new ResponseEntity<>(
				tasks,
				HttpStatus.OK);
	}

	@GetMapping("/getTeamOfEmployees")
	public ResponseEntity<List<Employee>> getTeamOfEmployees(
			@RequestParam(value = "managerId") int managerId,
			@RequestParam(value = "projectId") Integer projectId) throws PmsException {

		List<Employee> employees = service.getTeamOfEmployees(
				managerId,
				projectId);
		return new ResponseEntity<>(
				employees,
				HttpStatus.OK);
	}

}
