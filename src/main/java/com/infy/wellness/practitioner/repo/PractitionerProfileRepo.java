package com.infy.wellness.practitioner.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.wellness.practitioner.PractitionerProfile;

public interface PractitionerProfileRepo extends JpaRepository<PractitionerProfile, Long> {

	Optional<PractitionerProfile> findByUser_Id(Long userId);

}
