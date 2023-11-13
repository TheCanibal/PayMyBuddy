package com.PayMyBuddy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
@EnableWebMvc
public class WebMvcConfig {

    @Bean
    public SpringTemplateEngine templateEngine() {
	SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	templateEngine.addDialect(new SpringSecurityDialect());
	return templateEngine;
    }
}
