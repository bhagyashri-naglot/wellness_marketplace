package com.infy.wellness.utils.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

	@Value("${file.upload-dir:/apps/wellness/uploads}")
	private String uploadDir;

	@Override
	public String upload(MultipartFile file) {
		try {
			Path basePath = Paths.get(uploadDir).toAbsolutePath().normalize();
			Files.createDirectories(basePath);

			LocalDate date = LocalDate.now();
			Path datedFolder = basePath.resolve(date.getYear() + "/" + date.getMonthValue());
			Files.createDirectories(datedFolder);

			String originalName = file.getOriginalFilename();
			String extension = "";

			if (originalName != null && originalName.contains(".")) {
				extension = originalName.substring(originalName.lastIndexOf("."));
			}

			String uniqueName = UUID.randomUUID().toString() + extension;

			Path targetPath = datedFolder.resolve(uniqueName);

			Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

			return "/uploads/" + date.getYear() + "/" + date.getMonthValue() + "/" + uniqueName;

		} catch (IOException e) {
			throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
		}
	}
}
