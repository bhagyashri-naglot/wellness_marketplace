package com.infy.wellness.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.wellness.auth.AuthService;
import com.infy.wellness.auth.dto.AuthResponse;
import com.infy.wellness.auth.dto.LoginRequest;
import com.infy.wellness.auth.dto.RegisterRequest;
import com.infy.wellness.auth.dto.UserResponse;
import com.infy.wellness.common.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
		AuthResponse response = authService.register(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Registered", response));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Logged in", response));
	}

	@GetMapping("/loggedInUserDetails")
	public ResponseEntity<ApiResponse<UserResponse>> getLoggedInUserDetails(
			@RequestHeader("Authorization") String bearerToken) {

		UserResponse user = authService.getLoggedInUserDetails(bearerToken);

		return ResponseEntity.ok(new ApiResponse<>(true, "Logged-in user details fetched", user));
	}

}
