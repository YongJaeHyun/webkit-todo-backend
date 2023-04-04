package com.example.todo.config;

import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.example.todo.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final ObjectMapper objectMapper;

	public WebSecurityConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().httpBasic().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/", "/auth/**", "/h2-console/**").permitAll().anyRequest().authenticated();

		http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("status", HttpServletResponse.SC_FORBIDDEN);
			data.put("message", e.getMessage());

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			objectMapper.writeValue(response.getOutputStream(), data);

		});

		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
	}
}