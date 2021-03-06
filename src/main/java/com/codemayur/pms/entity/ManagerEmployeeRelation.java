package com.codemayur.pms.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity(name = "ManagerEmployeeRelation")
@Table(name = "MANAGER_EMPLOYEE_RELATION")
public class ManagerEmployeeRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MANAGER_ID")
	private int managerId;

	@Id
	@Column(name = "EMPLOYEE_ID")
	private int employeeId;

}
