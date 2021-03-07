package com.codemayur.pms.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.codemayur.pms.constant.PmsConstants;
import com.codemayur.pms.entity.Employee;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.entity.Task;

public class TaskController {

	Set<Task> tasks;

	@Autowired
	public TaskController(
			Set<Task> tasks) {

		Task task = new Task();
		task.setId(1);
		task.setName("task1");
		task.setEstimatedHours(12);
		task.setInstructions("Instruction 1");
		task.setStatus(PmsConstants.ACTIVE);
		task.setEmployee(new Employee());
		task.setProject(new Project());

		this.tasks = new HashSet<>();
		this.tasks.add(task);

	}

	@GetMapping
	public ResponseEntity<Set<Task>> getAllTasks() {
		return new ResponseEntity<>(tasks,
				HttpStatus.OK);
	}

}
