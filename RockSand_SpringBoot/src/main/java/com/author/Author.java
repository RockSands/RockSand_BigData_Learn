package com.author;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 通过读取properties,自动生成Bean
 * 路径:config/author.properties
 * 
 * 使用@Component ,则是正常使用资源文件
 * 注释掉@Component ,则使用AuthorAutoConfiguration自动加载资源文件
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix="author")
@PropertySource("classpath:/config/author.properties")
public class Author {
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * qq
     */
    private String qq;
    
    /**
     * 电话
     */
    private String phone;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getQq() {
	return qq;
    }

    public void setQq(String qq) {
	this.qq = qq;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }
}
