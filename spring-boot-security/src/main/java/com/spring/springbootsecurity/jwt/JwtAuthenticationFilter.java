package com.spring.springbootsecurity.jwt;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//To validate credentials sent by user
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	

	private final AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager=authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			JwtAuthenticationRequest authenticationRequest = new ObjectMapper().
					readValue(request.getInputStream(),JwtAuthenticationRequest.class);
			
			Authentication authentication=new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()
					);
			
			Authentication authenticate = authenticationManager.authenticate(authentication);
			return authenticate;
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String key="some_strong key_12312425rwfdasdvasgergwetgwegvefvaf";
		 String token = Jwts.builder()
	                .setSubject(authResult.getName())
	                .claim("authorities", authResult.getAuthorities())
	                .setIssuedAt(new java.util.Date())
	                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
	                .signWith(Keys.hmacShaKeyFor(key.getBytes()))
	                .compact();

	        response.addHeader("Authorization", "Bearer "+ token);
	}

}
