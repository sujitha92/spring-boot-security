package com.spring.springbootsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/","/index").permitAll()
		.antMatchers("/student/**").hasRole(ApplicationUserRole.STUDENT.name())
		.anyRequest()
		.authenticated()
		.and()
		.httpBasic();
		
		return http.build();
		
	}
	/*
	 * In Memory User Detail Manager
	 * 
	 * Password Encoding with Bcrypt
	 * 
	 */
	
	@Bean
	protected UserDetailsService userDetailsService() {
		
		//Admin User
		UserDetails sujithaUser = User.builder()
				.username("sujitha")
				.password(passwordEncoder.encode("sujitha"))
				.roles(ApplicationUserRole.ADMIN.name()).build();
		
		//Student User
		UserDetails pradeepUser = User.builder()
				.username("pradeep")
				.password(passwordEncoder.encode("pradeep"))
				.roles(ApplicationUserRole.STUDENT.name()).build();
		
		return new InMemoryUserDetailsManager(sujithaUser,pradeepUser);
	}
	

}
