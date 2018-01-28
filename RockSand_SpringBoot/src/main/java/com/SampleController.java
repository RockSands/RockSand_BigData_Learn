package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.author.Author;

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
	return "Hello " + author.getName() + "!";
    }
}
