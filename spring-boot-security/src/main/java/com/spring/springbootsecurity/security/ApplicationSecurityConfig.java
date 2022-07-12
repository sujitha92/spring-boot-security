package com.spring.springbootsecurity.security;

import static com.spring.springbootsecurity.security.ApplicationUserRole.ADMIN;
import static com.spring.springbootsecurity.security.ApplicationUserRole.ADMINTRAINEE;
import static com.spring.springbootsecurity.security.ApplicationUserRole.STUDENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.spring.springbootsecurity.jwt.JwtAuthenticationFilter;
import com.spring.springbootsecurity.jwt.JwtTokenVerifier;

@Configuration
//to use annotations over ant matchers for permissions
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter  {
	
	
	/*
	 * BASIC AUTH
	 * 
	 * Enable CSRF - Only when the client is browser
	 * 
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {
		
		//authenticationManager = http.getSharedObject(AuthenticationManager.class);
		
		http
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))
		.addFilterAfter(new JwtTokenVerifier(), JwtAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers("/","/index").permitAll()
		.antMatchers("/student/**").hasRole(STUDENT.name())
		.anyRequest()
		.authenticated();
	
		
		//return http.build();
		
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
