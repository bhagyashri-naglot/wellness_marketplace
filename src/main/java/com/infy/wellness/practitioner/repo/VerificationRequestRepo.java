package com.infy.wellness.practitioner.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.wellness.practitioner.VerificationRequest;

public interface VerificationRequestRepo extends JpaRepository<VerificationRequest, Long> {

	Page<VerificationRequest> findAllByStatus(String status, Pageable pageable);

}
