package com.smartcity.its.manager.media.service;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.media.dto.FileUploadReponse;
import org.springframework.web.multipart.MultipartFile;



public interface UploadFileService {
	Response<?> uploadFiles(MultipartFile[] files);
	Response<?> uploadImageBase64(String imageString);
	Response<?> deleteFile(String[] filePaths);
}
