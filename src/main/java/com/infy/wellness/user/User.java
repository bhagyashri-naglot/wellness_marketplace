package com.infy.wellness.user;

import com.infy.wellness.common.BaseEntity;
import com.infy.wellness.common.enums.Role;
import com.infy.wellness.practitioner.PractitionerProfile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(length = 1000)
	private String bio;

	// One-to-one link to PractitionerProfile (optional)
	// mappedBy = "user" because PractitionerProfile owns the FK (user_id)
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private PractitionerProfile practitionerProfile;

	public User(String name, String email, String password, Role role, String bio) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.bio = bio;
	}

	public User() {
		super();
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

	public PractitionerProfile getPractitionerProfile() {
		return practitionerProfile;
	}

	public void setPractitionerProfile(PractitionerProfile practitionerProfile) {
		if (practitionerProfile == null) {
			if (this.practitionerProfile != null) {
				this.practitionerProfile.setUser(null);
			}
			this.practitionerProfile = null;
		} else {
			practitionerProfile.setUser(this);
			this.practitionerProfile = practitionerProfile;
		}
	}
}
