package com.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.model.Employee;

/**
 * 只需要接口声明,Dao会自动实现JpaRepository内部提供的功能. 开发者只需要在接口里面定义自己的方法.
 * JpaRepository：提供基础的增删改查、翻页以及默认的Jpql语法方法-->适合简单逻辑
 * JpaSpecificationExecutor：提供Specification接口的支持-->适合复杂逻辑
 * 
 * @author Administrator
 *
 */
public interface EmployeeDao extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
	/*
	 * 使用JPA语言 的方法 --> 不推荐,简单可以使用 等同于select * from table where name = ?
	 */
	public List<Employee> findByFirstName(String name);

	/*
	 * 使用JPA语言 的方法 等同于select * from table where name like %?%
	 */
	public List<Employee> findByFirstNameLike(String name);

	/*
	 * 自定义 ?1 位传参第一个值, ?2 为第二个值 nativeQuery = true value内为sql语法, 默认为jpql语法
	 */
	@Query(value = "SELECT * FROM EMPLOYEES WHERE first_name = ?1", nativeQuery = true)
	public List<Employee> queryByName(String name);

	/*
	 * 自定义
	 * 
	 */
	@Query(value = "SELECT * FROM EMPLOYEES WHERE first_name LIKE %:name%", nativeQuery = true)
	public List<Employee> queryByNameLike(@Param("name") String name);

	/**
	 * 更新
	 * 
	 * @Modifying 标记为更新操作
	 * @Transactional 表明事物
	 * @Query 书写SQL
	 * @return
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE EMPLOYEES SET GENDER = :gender WHERE EMP_NO = :emp_no", nativeQuery = true)
	public int updateEmployeeGender(@Param("gender") String gender, @Param("emp_no") String emp_no);
}
