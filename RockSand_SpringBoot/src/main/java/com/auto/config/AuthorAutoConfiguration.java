package com.auto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.author.Author;
import com.author.service.SayHi;

/**
 * @EnableConfigurationProperties 读取资源文件生产类,Author内定义了资源文件
 * @ConditionalOnClass 表名当该类存在的情况下
 * @ConditionalOnMissingBean 表名如果SpringBean容器没有value的类时,调用
 * @author Administrator
 *
 */
@Configuration
@EnableConfigurationProperties(Author.class)
@ConditionalOnClass(Author.class)
public class AuthorAutoConfiguration {

	@Autowired
	private Author author;

	@Bean
	@ConditionalOnMissingBean(value = SayHi.class)
	public SayHi initAuthor() {
		SayHi hi = new SayHi();
		hi.setMsg(author.getName());
		return hi;
	}
}
