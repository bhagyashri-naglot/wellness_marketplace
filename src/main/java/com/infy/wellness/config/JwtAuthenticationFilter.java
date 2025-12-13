package com.infy.wellness.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.infy.wellness.auth.dto.UserDTO;
import com.infy.wellness.common.InfyLogger;
import com.infy.wellness.user.User;
import com.infy.wellness.user.repo.UserRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT filter that also attaches a UserDTO to the request after successful auth.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String USER_DTO_ATTR = "AuthorizedUser";

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final UserRepo userRepo;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserRepo userRepo) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		this.userRepo = userRepo;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		try {
			String header = request.getHeader("Authorization");
			String token = null;

			if (!StringUtils.hasText(header)) {
				InfyLogger.debug(this, "No Authorization header in request: {}", requestURI);
			}

			if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
				token = header.substring(7);
				InfyLogger.debug(this, "JWT Token extracted for request {}", requestURI);
			}

			if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				String username = null;
				try {
					username = jwtUtil.extractUsername(token);
					InfyLogger.debug(this, "Extracted username '{}' from token", username);
				} catch (Exception e) {
					InfyLogger.warn(this, "Failed to extract username from token: {}", e.getMessage());
				}

				if (username != null) {

					if (jwtUtil.validateToken(token, username)) {
						InfyLogger.info(this, "Token validated for user '{}'", username);

						UserDetails userDetails = userDetailsService.loadUserByUsername(username);
						InfyLogger.debug(this, "Loaded UserDetails for '{}'", username);

						UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());

						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);

						InfyLogger.info(this, "SecurityContext authentication set for '{}'", username);

						// --- NEW: load User entity to build UserDTO and attach to request ---
						try {
							User user = userRepo.findByEmail(username).orElse(null);
							if (user != null) {
								UserDTO userDto = new UserDTO(user.getId(), user.getEmail());
								// attach to request so controllers can read it
								request.setAttribute(USER_DTO_ATTR, userDto);
								InfyLogger.debug(this, "Attached UserDTO to request for user '{}'", username);
							} else {
								InfyLogger.warn(this, "User record not found for username '{}'", username);
							}
						} catch (Exception ex) {
							InfyLogger.warn(this, "Error loading user for request attribute: {}", ex.getMessage());
						}

					} else {
						InfyLogger.warn(this, "Invalid or expired token for user '{}'", username);
					}
				}
			} else if (token == null) {
				InfyLogger.debug(this, "No token found in Authorization header for {}", requestURI);
			}

		} catch (Exception ex) {
			InfyLogger.error(this, "JWT processing error for request {}: {}", ex, requestURI, ex.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}
