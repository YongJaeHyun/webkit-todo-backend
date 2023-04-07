package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.dto.UserDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private TokenProvider tokenProvider;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			UserEntity user = UserEntity.builder().email(userDTO.getEmail()).username(userDTO.getUsername())
					.password(passwordEncoder.encode(userDTO.getPassword())).build();

			UserEntity registeredUser = userService.create(user);
			UserDTO responseUserDTO = UserDTO.builder().email(registeredUser.getEmail()).id(registeredUser.getId())
					.username(registeredUser.getUsername()).build();
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error(error).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

		if (user != null) {
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder().email(user.getEmail()).id(user.getId()).token(token)
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error("Login failed").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@GetMapping("/user")
	public ResponseEntity<?> getUserInfo(@RequestParam String id) {
		UserEntity user = userService.getById(id);
		log.info("user: " + user);
		if (user != null) {
			final UserDTO responseUserDTO = UserDTO.builder().email(user.getEmail()).username(user.getUsername())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error("user not found").build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

	@PutMapping("/user")
	public ResponseEntity<?> updateUserInfo(@RequestBody UserDTO userDTO) {
		try {
			String username = userDTO.getUsername();
			String password = passwordEncoder.encode(userDTO.getPassword());
			String email = userDTO.getEmail();
			
			userService.update(username, password, email);
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<?> responseDTO = ResponseDTO.builder().error(error).build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
