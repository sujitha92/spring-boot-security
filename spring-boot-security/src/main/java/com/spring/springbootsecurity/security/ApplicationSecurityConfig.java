package com.spring.springbootsecurity.security;

import static com.spring.springbootsecurity.security.ApplicationUserRole.ADMIN;
import static com.spring.springbootsecurity.security.ApplicationUserRole.ADMINTRAINEE;
import static com.spring.springbootsecurity.security.ApplicationUserRole.STUDENT;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
//to use annotations over ant matchers for permissions
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
	
	/*
	 * BASIC AUTH
	 * 
	 * Enable CSRF - Only when the client is browser
	 * 
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
		//.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		//.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","/index").permitAll()
		.antMatchers("/student/**").hasRole(STUDENT.name())
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()//form based authentication instead of httpbasic();
			.loginPage("/login")
			.permitAll()//to have our own custom login page
			.defaultSuccessUrl("/courses",true)
			.passwordParameter("password")//can be changed according to the form name given in html
            .usernameParameter("username")//as of now set to default.
		.and()
		.rememberMe() //default 2 weeks
			.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
			.key("something_very_secured")
			.rememberMeParameter("remember-me")
		.and()
		.logout()
			.logoutUrl("/logout")
			.clearAuthentication(true)
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID","remember-me")
			.logoutSuccessUrl("/login");
	
		
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
