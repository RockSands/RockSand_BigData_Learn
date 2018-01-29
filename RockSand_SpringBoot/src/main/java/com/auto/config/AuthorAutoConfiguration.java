package com.auto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.author.Author;
import com.author.service.SayHi;

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
