package com.infy.wellness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.infy.wellness.common.enums.Role;
import com.infy.wellness.user.User;
import com.infy.wellness.user.repo.UserRepo;

@Configuration
public class AdminUserInitializer {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner initAdminUser() {
		return args -> {
			String adminUsername = "admin@gmail.com";
			String adminPassword = "ADMIN";

			userRepo.findByEmail(adminUsername).ifPresentOrElse(u -> {
				System.out.print("Admin user already exists");
			}, () -> {
				User admin = new User();
				admin.setName("System Admin");
				admin.setEmail("admin@gmail.com");
				admin.setPassword(passwordEncoder.encode(adminPassword));
				admin.setRole(Role.ADMIN);

				userRepo.save(admin);
			});
		};
	}
}
