package com.infy.wellness.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.wellness.user.User;

public interface UserRepo extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
