package com.infy.wellness.practitioner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.infy.wellness.auth.dto.UserDTO;
import com.infy.wellness.common.ApiResponse;
import com.infy.wellness.config.JwtAuthenticationFilter;
import com.infy.wellness.practitioner.service.PractitionerService;
import com.infy.wellness.utils.files.FileStorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/practitioners")
public class PractitionerController {

	@Autowired
	private PractitionerService practitionerService;

	@Autowired
	private FileStorageService fileStorageService;

	/**
	 * Submit verification (practitioner uploads documents). Expects Authorization:
	 * Bearer <token> header — JWT filter attaches UserDTO as request attribute.
	 */
	@PostMapping(value = "/verify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('PRACTITIONER')")
	public ResponseEntity<ApiResponse<Map<String, Object>>> submitVerification(HttpServletRequest request,
			@RequestParam("files") List<MultipartFile> files,
			@RequestParam(value = "note", required = false) String note,
			@RequestHeader("Authorization") String breareToken) {
		try {

			// read UserDTO set by JwtAuthenticationFilter
			Object attr = request.getAttribute(JwtAuthenticationFilter.USER_DTO_ATTR);
			if (!(attr instanceof UserDTO)) {
				return ResponseEntity.status(401).body(new ApiResponse<>(false, "Unauthorized", null));
			}
			UserDTO user = (UserDTO) attr;

			// upload files -> get URLs
			Set<String> urls = files.stream().map(fileStorageService::upload).collect(Collectors.toSet());

			VerificationRequest vr = practitionerService.submitVerification(user.getUserId(), urls, note);

			Map<String, Object> data = Map.of("requestId", vr.getId(), "status", vr.getStatus());
			return ResponseEntity.ok(new ApiResponse<>(true, "Verification submitted", data));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Add specializations to a practitioner profile. Admins and the practitioner
	 * themselves may be allowed; adjust PreAuthorize as needed.
	 */
	@PostMapping("/{id}/specializations")
	@PreAuthorize("hasRole('PRACTITIONER') or hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<String>> addSpecializations(@PathVariable Long id,
			@RequestBody List<String> names, @RequestHeader("Authorization") String breareToken) {

		PractitionerProfile p = practitionerService.addSpecializations(id, names);
		return ResponseEntity.ok(new ApiResponse<>(true, "Specializations added", "DONE"));
	}
}
