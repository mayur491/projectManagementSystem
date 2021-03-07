package com.codemayur.pms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codemayur.pms.dto.ProjectDto;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.exception.PmsException;
import com.codemayur.pms.service.impl.ProjectServiceImpl;

@RestController
@RequestMapping("/project")
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	ProjectServiceImpl projectService;

	@Autowired
	public ProjectController(
			ProjectServiceImpl projectService) {
		this.projectService = projectService;
	}

	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> projects;
		try {

			projects = projectService.getAllProjects();

		} catch (PmsException e) {
			logger.error("EC:CCPCPCgetAllProjects01",
					e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(projects,
				HttpStatus.OK);
	}

	@GetMapping
	@RequestMapping("/{projectId}")
	public ResponseEntity<Project> getProjectById(
			@PathVariable int projectId) {
		Project project;
		try {

			project = projectService.getProjectById(projectId);

		} catch (PmsException e) {
			logger.error("EC:CCPCPCgetProjectById01 projectId: {}",
					projectId,
					e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(project,
				HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<HttpStatus> postProject(
			@RequestParam("name") String projectName) {
		try {

			ProjectDto projectDto = new ProjectDto(projectName);
			projectService.postProject(projectDto);

		} catch (PmsException e) {
			logger.error("EC:CCPCPCpostProject01 projectName: {}",
					projectName,
					e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
