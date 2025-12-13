package com.infy.wellness.utils.files;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
	String upload(MultipartFile file);
}