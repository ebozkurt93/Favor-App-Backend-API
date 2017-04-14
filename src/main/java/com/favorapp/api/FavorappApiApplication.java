package com.favorapp.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.favorapp.api.config.JwtFilter;

@SpringBootApplication
public class FavorappApiApplication {
	
	@Bean
	public FilterRegistrationBean jwtFilter() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/user/secure/*");

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(FavorappApiApplication.class, args);
	}
}
