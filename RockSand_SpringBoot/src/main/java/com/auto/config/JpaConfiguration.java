package com.auto.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * JPA的配置文件
 * 
 * @EnableJpaRepositories 读取value中的类,作为repository(即持久化层:Dao)
 * @author Administrator
 *
 */
@Configuration
@EnableJpaRepositories("com.jpa.repository")
public class JpaConfiguration {
}
