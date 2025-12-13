package com.infy.wellness.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infy.wellness.auth.dto.UserDTO;
import com.infy.wellness.practitioner.VerificationRequest;
import com.infy.wellness.practitioner.repo.VerificationRequestRepo;
import com.infy.wellness.practitioner.service.PractitionerService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

	@Autowired
	private PractitionerService practitionerService;
	@Autowired

	private VerificationRequestRepo verificationRepo;

	@GetMapping("/verification-requests")
	public Page<VerificationRequest> listRequests(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "PENDING") String status) {
		return verificationRepo.findAllByStatus(status,
				PageRequest.of(page, size, Sort.by("requestedAt").descending()));
	}

	@PutMapping("/verification-requests/{id}")
	public ResponseEntity<?> review(@PathVariable Long id, @RequestParam boolean approve,
			@RequestParam(required = false) String comment, @RequestAttribute("AuthorizedUser") UserDTO admin) {
		practitionerService.reviewVerification(id, admin.getUserId(), approve, comment);
		return ResponseEntity.ok(Map.of("status", approve ? "APPROVED" : "REJECTED"));
	}
}
