package com.codemayur.pms.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.codemayur.pms.dto.ProjectDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "project")
@Table(name = "PROJECT")
public class Project {

	@Id
	@Column(name = "PROJECT_ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "STATUS")
	private String status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "EMPLOYEE_PROJECT_RELATION", joinColumns = {
			@JoinColumn(name = "PROJECT_ID")
	}, inverseJoinColumns = {
			@JoinColumn(name = "EMPLOYEE_ID")
	})
	private Set<Employee> employees;

	@OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
	private Set<Task> tasks;

	public Project(
			ProjectDto projectDto) {
		super();
		this.name = projectDto.getName();
	}
	
	public Project(
			int id,
			String name,
			String status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public Project(
			String name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", status=" + status + "]";
	}

}
