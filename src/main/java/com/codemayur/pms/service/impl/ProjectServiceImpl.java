package com.codemayur.pms.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codemayur.pms.constant.ProjectConstants;
import com.codemayur.pms.dao.impl.ProjectDaoImpl;
import com.codemayur.pms.dto.ProjectDto;
import com.codemayur.pms.entity.Project;
import com.codemayur.pms.exception.PmsException;

@Service
public class ProjectServiceImpl {

	ProjectDaoImpl projectDao;

	@Autowired
	public ProjectServiceImpl(
			ProjectDaoImpl projectDao) {
		this.projectDao = projectDao;
	}

	public List<Project> getAllProjects() throws PmsException {
		List<Project> projects = null;
		try {

			projects = projectDao.getAllProjects();

		} catch (Exception e) {
			throw new PmsException("EC:CCPSIPSIgetAllProjects01",
					e);
		}
		return projects;
	}

	public Project getProjectById(
			int projectId) throws PmsException {
		Project projects = null;
		try {

			projects = projectDao.getProjectByProjectId(projectId);

		} catch (Exception e) {
			throw new PmsException("EC:CCPSIPSIgetProjectById01",
					e);
		}
		return projects;
	}

	public void postProject(
			ProjectDto projectDto) throws PmsException {

		try {

			validateProjectDto(projectDto);

			Project project = new Project(projectDto);
			Integer projectId = projectDao.getMaxProjectId();
			project.setId(projectId + 1);
			project.setStatus(ProjectConstants.ACTIVE);

			projectDao.postProject(project);

		} catch (Exception e) {
			throw new PmsException(String.format("projectDto: %s",
					projectDto),
					e);
		}
	}

	private void validateProjectDto(
			ProjectDto projectDto) throws PmsException {

		if (Objects.isNull(projectDto.getName())) {
			throw new PmsException("Project Name cannot be null.");
		}

		if (projectDao.isDuplicateProjectNamePresent(projectDto.getName())) {
			throw new PmsException(String.format("Project Name: %s already exists.",
					projectDto.getName()));
		}

	}

}
