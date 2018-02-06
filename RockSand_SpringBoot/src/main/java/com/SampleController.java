package com;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.author.Author;
import com.author.service.SayHi;
import com.jpa.model.Employee;
import com.jpa.repository.EmployeeDao;

/**
 * @RestController Rest接口的Controller
 * @SpringBootApplication SpringBoot的入口
 * @author Administrator
 *
 */
@RestController
@SpringBootApplication
public class SampleController {

	/**
	 * 自动绑定
	 */
	@Autowired
	private Author author;

	@Autowired
	private SayHi sayHi;

	@Autowired
	private EmployeeDao employeeDao;

	public static void main(String[] args) {
		/**
		 * SpringBoot加载Main的方法一,最简操作
		 */
		// SpringApplication.run(SampleController.class, args);

		/**
		 * SpringBoot加载Main的方法二 SpringBoot加载Main的方法三 使用SpringApplicationBuilder,这里不演示
		 */
		SpringApplication application = new SpringApplication(SampleController.class);
		// 关闭Banner的输出
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);

	}

	@RequestMapping("/")
	public String hello() {
		List<Employee> employees = employeeDao.queryByName("Kyoichi");
		for(Employee employee : employees) {
			System.out.println("===>" + employee.getFirstName() + "." + employee.getLastName());
		}
		return "Hello" + author.getName() + "!\n" + sayHi.sayHi();
	}
}
