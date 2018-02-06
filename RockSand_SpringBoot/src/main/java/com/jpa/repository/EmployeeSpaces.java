package com.jpa.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.jpa.model.Employee;

/**
 * Specification: SQL逻辑构建器,它与JSqlParser类似.就是SQL的构建工具.
 * 
 * Employee工具类,适合构建复杂逻辑.
 * 通过Specification构建Sql复杂逻辑并由EmployeeDao的JpaSpecificationExecutor接口方法进行执行
 * 
 * @author Administrator
 *
 */
public class EmployeeSpaces {
	
	public static Specification<Employee> queryManager() {
		return new Specification<Employee>() {
			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// root对象相当于form后面的表对象
				// query对象构建where条件语句
				// CriteriaBuilder构建工具
				// 一下逻辑是查询Employee表,且Where条件为gender = 'M'
				return cb.equal(root.get("gender"), "M");
			}
		};
	}
}
