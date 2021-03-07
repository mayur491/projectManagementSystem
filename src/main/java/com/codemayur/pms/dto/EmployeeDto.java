package com.codemayur.pms.dto;

import java.util.Set;

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
public class EmployeeDto {

	private Integer employeeId;

	private String firstName;

	private String lastName;

	private String email;

	private Integer bandwidth;

	private UserLoginDto userLogin;

	private Set<ProjectDto> projects;

	private Set<TaskDto> tasks;

}
