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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Employee")
@Table(name = "EMPLOYEE")
public class Employee {

	@Id
	@Column(name = "EMPLOYEE_ID")
	private Integer employeeId;

	@Column(name = "FIRSTNAME")
	private String firstName;

	@Column(name = "LASTNAME")
	private String lastName;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "BANDWIDTH")
	private Integer bandwidth;

	/*
	 * OneToOne Employee to UserLogin
	 */
	@OneToOne
	@JoinColumn(name = "EMPLOYEE_ID", referencedColumnName = "USER_ID")
	private UserLogin userLogin;

	/*
	 * ManyToMany Employee to Project
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "M_D_EMPLOYEE_PROJECT_RELATION", joinColumns = {
			@JoinColumn(name = "EMPLOYEE_ID")
	}, inverseJoinColumns = {
			@JoinColumn(name = "PROJECT_ID")
	})
	private Set<Project> projects;

	/*
	 * OneToMany Employee to Tasks
	 */
	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
	private Set<Task> tasks;

	public Employee(
			Integer employeeId,
			String firstName,
			String lastName,
			String email,
			Integer bandwidth) {
		super();
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.bandwidth = bandwidth;
	}

	public Employee(
			String firstName,
			String lastName,
			String email,
			Integer bandwidth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.bandwidth = bandwidth;
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
				+ email + ", bandwidth=" + bandwidth + ", userLogin=" + userLogin + ", projects=" + projects
				+ ", tasks=" + tasks + "]";
	}

}
