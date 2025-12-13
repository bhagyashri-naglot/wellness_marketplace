package com.infy.wellness.practitioner.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.wellness.practitioner.PractitionerProfile;
import com.infy.wellness.practitioner.Specialization;
import com.infy.wellness.practitioner.VerificationRequest;
import com.infy.wellness.practitioner.repo.PractitionerProfileRepo;
import com.infy.wellness.practitioner.repo.SpecializationRepo;
import com.infy.wellness.practitioner.repo.VerificationRequestRepo;
import com.infy.wellness.user.User;
import com.infy.wellness.user.repo.UserRepo;

import jakarta.transaction.Transactional;

@Service
public class PractitionerService {
	@Autowired
	private PractitionerProfileRepo profileRepo;
	@Autowired
	private VerificationRequestRepo verificationRepo;
	@Autowired
	private SpecializationRepo specializationRepo;
	@Autowired
	private UserRepo userRepo;

	public PractitionerProfile ensureProfileForUser(Long userId) {
		return profileRepo.findByUser_Id(userId).orElseGet(() -> {
			User user = userRepo.findById(userId).orElseThrow();
			PractitionerProfile p = new PractitionerProfile();
			p.setUser(user);
			return profileRepo.save(p);
		});
	}

	public VerificationRequest submitVerification(Long userId, Set<String> docUrls, String note) {
		PractitionerProfile profile = ensureProfileForUser(userId);
		VerificationRequest vr = new VerificationRequest();
		vr.setPractitioner(profile);
		vr.setDocUrls(docUrls);
		vr.setNote(note);
		vr.setRequestedAt(LocalDateTime.now());
		profile.setVerificationStatus("PENDING");
		profile.getVerificationDocs().addAll(docUrls);
		profileRepo.save(profile);
		return verificationRepo.save(vr);
	}

	@Transactional
	public void reviewVerification(Long requestId, Long adminId, boolean approve, String comment) {
		VerificationRequest vr = verificationRepo.findById(requestId).orElseThrow();
		PractitionerProfile p = vr.getPractitioner();
		vr.setReviewedAt(LocalDateTime.now());
		vr.setReviewedByAdminId(adminId);
		vr.setStatus(approve ? "APPROVED" : "REJECTED");
		verificationRepo.save(vr);

		p.setVerificationStatus(vr.getStatus());
		p.setVerified(approve);
		profileRepo.save(p);

		// TODO: Email Notification
	}

	public PractitionerProfile addSpecializations(Long practitionerId, List<String> specializationNames) {
		PractitionerProfile p = profileRepo.findById(practitionerId).orElseThrow();
		for (String name : specializationNames) {
			Specialization s = specializationRepo.findByName(name)
					.orElseGet(() -> specializationRepo.save(new Specialization(name)));
			p.getSpecializations().add(s);
		}
		return profileRepo.save(p);
	}
}
