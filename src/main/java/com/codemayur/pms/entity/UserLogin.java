package com.codemayur.pms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "UserLogin")
@Table(name = "USER_LOGIN")
public class UserLogin {

	@Id
	@Column(name = "USER_ID")
	private int userId;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ROLE")
	private String role;

	/*
	 * OneToOne UserLogin to Employee
	 */
	@OneToOne(mappedBy = "userLogin")
	private Employee employee;

	public UserLogin(
			int userId,
			String password,
			String role) {
		super();
		this.userId = userId;
		this.password = password;
		this.role = role;
	}

	public UserLogin(
			String password,
			String role) {
		super();
		this.password = password;
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserLogin [userId=" + userId + ", password=" + password + ", role=" + role + "]";
	}
}
