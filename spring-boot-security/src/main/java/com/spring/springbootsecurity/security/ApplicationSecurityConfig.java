package com.spring.springbootsecurity.security;

import static com.spring.springbootsecurity.security.ApplicationUserPermission.*;
import static com.spring.springbootsecurity.security.ApplicationUserRole.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
//to use annotations over ant matchers for permissions
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
	
	/*
	 * BASIC AUTH
	 * 
	 * ant matchers - to open index page without any authentication.
	 * 
	 * Only Student to view student api --> hasRole()
	 * 
	 * Only Admin and AdminTrainee can view management api -->hasAnyRole()
	 * 
	 * Admin Trainee has permission to only read (i.e get) 
	 * Only Admin has permission to write and read--> hasAuthority ()
	 * 
	 * antMatchers order matters.
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","/index").permitAll()
		.antMatchers("/student/**").hasRole(STUDENT.name())
		.antMatchers(HttpMethod.DELETE,"/management/student/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.PUT,"/management/student/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers(HttpMethod.POST,"/management/student/**").hasAuthority(COURSE_WRITE.getPermission())
		.antMatchers("/management/student/**").hasAnyRole(ADMIN.name(),ADMINTRAINEE.name())
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
	 * When we use authorities() we have to build Roles ourselves. Check ApplicationUserRole.java.
	 * 
	 */
	
	@Bean
	protected UserDetailsService userDetailsService() {
		
		//Admin User
		UserDetails sujithaUser = User.builder()
				.username("sujitha")
				.password(passwordEncoder.encode("password"))
				.authorities(ADMIN.getGrantedAuthorities()).build();
				//.roles(ADMIN.name()).build();
		
		//Student User
		UserDetails pradeepUser = User.builder()
				.username("pradeep")
				.password(passwordEncoder.encode("password"))
				.authorities(STUDENT.getGrantedAuthorities()).build();
				//.roles(STUDENT.name()).build();
		
		//AdminTrainee User
		UserDetails tomUser = User.builder()
				.username("tom")
				.password(passwordEncoder.encode("password"))
				.authorities(ADMINTRAINEE.getGrantedAuthorities()).build();
				//.roles(ADMINTRAINEE.name()).build();
				
		
		return new InMemoryUserDetailsManager(sujithaUser,pradeepUser,tomUser);
	}
	

}
