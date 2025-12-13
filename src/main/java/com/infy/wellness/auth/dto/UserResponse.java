package com.infy.wellness.auth.dto;

import com.infy.wellness.common.enums.Role;

public class UserResponse {

	private Long id;
	private String name;
	private String email;
	private Role role;
	private String bio;
	private boolean practitioner;
	private boolean practitionerVerified;

	// ⭐ NEW FIELD
	private Long profileId;

	public UserResponse() {
	}

	public UserResponse(Long id, String name, String email, Role role, String bio, boolean practitioner,
			boolean practitionerVerified, Long profileId) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
		this.bio = bio;
		this.practitioner = practitioner;
		this.practitionerVerified = practitionerVerified;
		this.profileId = profileId;
	}

	// ---------------------------
	// Getters & Setters
	// ---------------------------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public boolean isPractitioner() {
		return practitioner;
	}

	public void setPractitioner(boolean practitioner) {
		this.practitioner = practitioner;
	}

	public boolean isPractitionerVerified() {
		return practitionerVerified;
	}

	public void setPractitionerVerified(boolean practitionerVerified) {
		this.practitionerVerified = practitionerVerified;
	}

	public Long getProfileId() {
		return profileId;
	}

	public void setProfileId(Long profileId) {
		this.profileId = profileId;
	}
}
