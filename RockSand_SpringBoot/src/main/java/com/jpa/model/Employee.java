package com.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA的实体类
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "employees")
public class Employee {
	/**
	 * 员工编号
	 */
	@Id
	@Column(name = "emp_no", unique = true, nullable = false, length = 11)
	private Long empNo;

	/**
	 * 出生日期
	 */
	@Column(name = "birth_date")
	private Date birthDate;

	/**
	 * 姓
	 */
	@Column(name = "first_name")
	private String firstName;

	/**
	 * 名
	 */
	@Column(name = "last_name")
	private String lastName;

	/**
	 * 雇佣日期
	 */
	@Column(name = "hire_date")
	private Date hireDate;

	/**
	 * 岗位
	 */
	@Column(name = "gender")
	private String gender;

	public Long getEmpNo() {
		return empNo;
	}

	public void setEmpNo(Long empNo) {
		this.empNo = empNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
}
