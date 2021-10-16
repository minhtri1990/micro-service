package com.smartcity.its.manager.media.common;

import com.smartcity.its.manager.media.common.Enum.FileType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileUtils {

	public static boolean validateSupportFile(MultipartFile file, String[] fileTypes) {
		String originalName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileExtension = NameUtils.getFileExtension(originalName);
		return Arrays.asList(fileTypes).contains(fileExtension.toLowerCase());
	}

	public static FileType getFileType(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType.startsWith(Constants.IMAGE_CONTENT_TYPE))
			return FileType.IMAGE_FILE;
		if (contentType.startsWith(Constants.VIDEO_CONTENT_TYPE))
			return FileType.VIDEO_FILE;
		return FileType.DOCUMENT_FILE;
	}

}
