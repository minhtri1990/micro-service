package com.smartcity.its.manager.media.controller;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.media.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@DependsOn("UploadFileService")
@RequestMapping("/v1")
public class MediaController {

	@Autowired
	private UploadFileService fileService;

	@GetMapping("/test")
	public String test() {
		return "media service";
	}

	@PostMapping("/upload-files")
	public Object uploadFiles(@RequestBody MultipartFile[] files) {
		return fileService.uploadFiles(files);
	}

	@PostMapping("/delete-files")
	public Object deleteFiles(@RequestBody String[] files) {
		return fileService.deleteFile(files);
	}
}
