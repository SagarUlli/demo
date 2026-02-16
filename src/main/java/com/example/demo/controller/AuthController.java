package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	// Register user
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {

		// Check if email already exists
		if (userService.findByEmail(user.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("Email already in use");
		}

		User savedUser = userService.save(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
	}

	// Login user (basic example - no JWT yet)
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest) {

		Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());

		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}

		User user = userOptional.get();

		// In real app: compare hashed password here
		if (!user.getPassword().equals(loginRequest.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}

		return ResponseEntity.ok("Login successful");
	}
}
