package com.favorapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import com.favorapp.api.config.JwtFilter;


@EnableAutoConfiguration
@EnableSpringConfigured
@ComponentScan
@SpringBootApplication
public class FavorappApiApplication {
	
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/user/secure/*");
		registrationBean.addUrlPatterns("/event/secure/*");
		//registrationBean.addUrlPatterns("/demo/secure/*");

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(FavorappApiApplication.class, args);
	}
}
