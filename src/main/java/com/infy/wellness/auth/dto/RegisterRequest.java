package com.infy.wellness.auth.dto;

import java.util.List;

import com.infy.wellness.common.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

	@NotBlank
	private String name;

	@Email
	private String email;

	@NotBlank
	private String password;

	private Role role;
	private String bio;

	private List<String> specializations;

	public RegisterRequest() {
	}

	public RegisterRequest(String name, String email, String password, Role role, String bio,
			List<String> specializations) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.bio = bio;
		this.specializations = specializations;
	}

	// Getters & Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<String> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(List<String> specializations) {
		this.specializations = specializations;
	}
}
