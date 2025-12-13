package com.infy.wellness.practitioner.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.wellness.practitioner.Specialization;

public interface SpecializationRepo extends JpaRepository<Specialization, Long> {

	Optional<Specialization> findByName(String name);

}
