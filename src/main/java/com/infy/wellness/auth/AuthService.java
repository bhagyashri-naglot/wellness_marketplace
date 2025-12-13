package com.infy.wellness.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infy.wellness.auth.dto.AuthResponse;
import com.infy.wellness.auth.dto.LoginRequest;
import com.infy.wellness.auth.dto.RegisterRequest;
import com.infy.wellness.auth.dto.UserResponse;
import com.infy.wellness.common.enums.Role;
import com.infy.wellness.config.JwtUtil;
import com.infy.wellness.practitioner.PractitionerProfile;
import com.infy.wellness.practitioner.Specialization;
import com.infy.wellness.practitioner.repo.SpecializationRepo;
import com.infy.wellness.user.User;
import com.infy.wellness.user.repo.UserRepo;

@Service
public class AuthService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private SpecializationRepo specializationRepo;

	public AuthResponse register(RegisterRequest request) {
		if (userRepo.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already registered");
		}

		Role role = request.getRole() != null ? request.getRole() : Role.PATIENT;

		// Create User
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(role);
		user.setBio(request.getBio());

		if (role == Role.PRACTITIONER) {
			PractitionerProfile profile = new PractitionerProfile();
			profile.setUser(user);

			// Add specializations if provided
			if (request.getSpecializations() != null) {
				Set<Specialization> specs = new HashSet<>();
				for (String s : request.getSpecializations()) {
					Specialization spec = specializationRepo.findByName(s)
							.orElseGet(() -> specializationRepo.save(new Specialization(s)));
					specs.add(spec);
				}
				profile.setSpecializations(specs);
			}

			user.setPractitionerProfile(profile);
		}

		userRepo.save(user);

		// Generate JWT tokens
		String access = jwtUtil.generateToken(user.getEmail());
		String refresh = jwtUtil.generateRefreshToken(user.getEmail());

		return new AuthResponse(access, refresh);
	}

	public AuthResponse login(LoginRequest request) {
		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		String access = jwtUtil.generateToken(user.getEmail());
		String refresh = jwtUtil.generateRefreshToken(user.getEmail());

		return new AuthResponse(access, refresh);
	}

	public UserResponse getLoggedInUserDetails(String bearerToken) {

		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new RuntimeException("Invalid Authorization header");
		}

		String token = bearerToken.substring(7); // remove "Bearer "

		// Extract email (username) from JWT
		String email = jwtUtil.extractUsername(token);

		if (email == null) {
			throw new RuntimeException("Invalid token");
		}

		// Load user details
		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		boolean isPractitioner = user.getPractitionerProfile() != null;
		boolean isVerified = isPractitioner && user.getPractitionerProfile().isVerified();

		PractitionerProfile pr = user.getPractitionerProfile();

		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getBio(),
				isPractitioner, isVerified, pr.getId());
	}

}
