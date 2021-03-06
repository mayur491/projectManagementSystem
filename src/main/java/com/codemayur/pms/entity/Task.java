package com.codemayur.pms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Task")
@Table(name = "TASK")
public class Task {

	@Id
	@Column(name = "TASK_ID")
	private int id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "INSTRUCTIONS")
	private String instructions;

	@Column(name = "ESTIMATED_HOURS")
	private int estimatedHours;

	@Column(name = "STATUS")
	private String status;

	/*
	 * ManyToOne Task to Employee
	 */
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;

	/*
	 * ManyToOne Task to Project
	 */
	@ManyToOne
	@JoinColumn(name = "PROJECT_ID")
	private Project project;

	public Task(
			String name,
			String instructions,
			int estimatedHours,
			Employee employee,
			Project project) {
		super();
		this.name = name;
		this.instructions = instructions;
		this.estimatedHours = estimatedHours;
		this.employee = employee;
		this.project = project;
	}

	public Task(
			int id,
			String name,
			String instructions,
			int estimatedHours,
			String status) {
		super();
		this.id = id;
		this.name = name;
		this.instructions = instructions;
		this.estimatedHours = estimatedHours;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", instructions=" + instructions + ", estimatedHours="
				+ estimatedHours + ", status=" + status + "]";
	}
}
