package com.infy.wellness.practitioner;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "verification_requests")
public class VerificationRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private PractitionerProfile practitioner;

	@ElementCollection
	private Set<String> docUrls;

	private String note; // practitioner note

	private String status = "PENDING"; // PENDING, APPROVED, REJECTED
	private LocalDateTime requestedAt;
	private LocalDateTime reviewedAt;
	private Long reviewedByAdminId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PractitionerProfile getPractitioner() {
		return practitioner;
	}

	public void setPractitioner(PractitionerProfile practitioner) {
		this.practitioner = practitioner;
	}

	public Set<String> getDocUrls() {
		return docUrls;
	}

	public void setDocUrls(Set<String> docUrls) {
		this.docUrls = docUrls;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(LocalDateTime requestedAt) {
		this.requestedAt = requestedAt;
	}

	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

	public Long getReviewedByAdminId() {
		return reviewedByAdminId;
	}

	public void setReviewedByAdminId(Long reviewedByAdminId) {
		this.reviewedByAdminId = reviewedByAdminId;
	}

	public VerificationRequest() {
		super();
	}

	public VerificationRequest(Long id, PractitionerProfile practitioner, Set<String> docUrls, String note,
			String status, LocalDateTime requestedAt, LocalDateTime reviewedAt, Long reviewedByAdminId) {
		super();
		this.id = id;
		this.practitioner = practitioner;
		this.docUrls = docUrls;
		this.note = note;
		this.status = status;
		this.requestedAt = requestedAt;
		this.reviewedAt = reviewedAt;
		this.reviewedByAdminId = reviewedByAdminId;
	}

}
