package com.codemayur.pms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskDto {

	private int id;

	private String name;

	private String instructions;

	private int estimatedHours;

	private String status;

	private EmployeeDto employee;

	private ProjectDto project;

}
