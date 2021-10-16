package com.its.auth_service.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.its.auth_service.model.odp.response.UploadFileResponse;
import com.its.module.model.response.Response;

@FeignClient(name = "its-media-service")
public interface UploadFileFeignClient {

	@RequestMapping(value = "/media-management/v1/upload-files",
			method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	Response<UploadFileResponse> uploadFile(@RequestPart(value = "files") MultipartFile file);

}
