package com.infy.wellness.auth.dto;

public class UserDTO {

	private Long userId;
	private String email;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserDTO(Long userId, String email) {
		super();
		this.userId = userId;
		this.email = email;
	}

	public UserDTO() {
		super();
	}

}
