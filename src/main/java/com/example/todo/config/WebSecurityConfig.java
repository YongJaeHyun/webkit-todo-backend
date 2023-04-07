package com.example.todo.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.todo.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {

	private final ObjectMapper objectMapper;

	public WebSecurityConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	protected SecurityFilterChain SecurityConfiguration(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().httpBasic().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/", "/auth/**", "/h2-console/**").permitAll().anyRequest().authenticated()
				.and()
				.headers().frameOptions().disable();

		http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("status", HttpServletResponse.SC_FORBIDDEN);
			data.put("message", e.getMessage());

			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);

			objectMapper.writeValue(response.getOutputStream(), data);

		});

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}