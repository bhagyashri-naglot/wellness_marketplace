package com.infy.wellness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.infy.wellness.user.repo.UserRepo;

@Configuration
public class SecurityConfig {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Bean
	UserDetailsService userDetailsService() {
		return username -> userRepo.findByEmail(username)
				.map(u -> org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
						.password(u.getPassword()).authorities("ROLE_" + u.getRole().name()).build())
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	@Bean
	JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtUtil, userDetailsService(), userRepo);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				// we don't use sessions — JWT stateless
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// swagger / docs
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs.yaml",
								"/swagger-resources/**", "/webjars/**")
						.permitAll()

						// auth endpoints are public
						.requestMatchers("/api/auth/**").permitAll()

						// public GETs (products)
						.requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

						// everything else requires authentication
						.anyRequest().authenticated());

		// add JWT filter before UsernamePasswordAuthenticationFilter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
