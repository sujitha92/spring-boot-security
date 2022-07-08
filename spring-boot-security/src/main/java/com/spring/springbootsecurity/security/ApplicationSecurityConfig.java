package com.spring.springbootsecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfig {
	
	/*
	 * BASIC AUTH
	 * 
	 * ant matchers - to open index page without any authentication.
	 * 
	 * Allow only user with Role Student to view student student api 
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/","/index").permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
		
		return http.build();
		
	}
	

}
