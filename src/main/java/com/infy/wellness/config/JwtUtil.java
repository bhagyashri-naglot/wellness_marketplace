package com.infy.wellness.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.secret:a9Fh27QpL3xT8wZc0mBv4Nj5RsYk1HdG}")
	private String secret;

	@Value("${jwt.expiration-ms:3600000}") // default 1 hour
	private long expirationMs;

	private Key key;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// -------------------------------------
	// 🔹 Generate Access Token
	// -------------------------------------
	public String generateToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// -------------------------------------
	// 🔹 Generate Refresh Token (24x expiry)
	// -------------------------------------
	public String generateRefreshToken(String subject) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationMs * 24))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// -------------------------------------
	// 🔹 Extract username (email) from JWT
	// -------------------------------------
	public String extractUsername(String token) {
		return getClaims(token).getSubject();
	}

	// -------------------------------------
	// 🔹 Validate Token → Signature + Expiry + Subject
	// -------------------------------------
	public boolean validateToken(String token, String username) {
		try {
			final String extractedUsername = extractUsername(token);
			return extractedUsername.equals(username) && !isTokenExpired(token);
		} catch (ExpiredJwtException e) {
			System.out.println("JWT Token expired: " + e.getMessage());
		} catch (JwtException e) {
			System.out.println("JWT Validation error: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("JWT Unknown error: " + e.getMessage());
		}
		return false;
	}

	// -------------------------------------
	// 🔹 Check if token expired
	// -------------------------------------
	private boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	// -------------------------------------
	// 🔹 Get Claims from token
	// -------------------------------------
	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
