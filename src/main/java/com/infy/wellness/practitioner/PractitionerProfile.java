package com.infy.wellness.practitioner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.infy.wellness.common.BaseEntity;
import com.infy.wellness.user.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "practitioner_profiles")
public class PractitionerProfile extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Column(name = "verified", nullable = false)
	private boolean verified = false;

	@Column(name = "verification_status", length = 32)
	private String verificationStatus = "PENDING"; // PENDING, APPROVED, REJECTED

	@ElementCollection
	@CollectionTable(name = "practitioner_docs", joinColumns = @JoinColumn(name = "practitioner_id"))
	@Column(name = "doc_url", length = 1000)
	private Set<String> verificationDocs = new HashSet<>();

	// Example: many-to-many with Specialization
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "practitioner_specializations", joinColumns = @JoinColumn(name = "practitioner_id"), inverseJoinColumns = @JoinColumn(name = "specialization_id"))
	private Set<Specialization> specializations = new HashSet<>();

	@Column(name = "rating")
	private Double rating = 0.0;

	@Column(name = "last_verified_at")
	private LocalDateTime lastVerifiedAt;

	public PractitionerProfile() {
	}

	// getters & setters

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
		if (verified) {
			this.verificationStatus = "APPROVED";
			this.lastVerifiedAt = LocalDateTime.now();
		}
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Set<String> getVerificationDocs() {
		return verificationDocs;
	}

	public void setVerificationDocs(Set<String> verificationDocs) {
		this.verificationDocs = verificationDocs;
	}

	public Set<Specialization> getSpecializations() {
		return specializations;
	}

	public void setSpecializations(Set<Specialization> specializations) {
		this.specializations = specializations;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public LocalDateTime getLastVerifiedAt() {
		return lastVerifiedAt;
	}

	public void setLastVerifiedAt(LocalDateTime lastVerifiedAt) {
		this.lastVerifiedAt = lastVerifiedAt;
	}
}
