package com.smartcity.its.manager.media.service.Impl;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.media.MediaConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Queue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

class UploadFileServiceImplTest {

	@Mock
	private MediaConfig mediaConfig;
	@InjectMocks 
	UploadFileServiceImpl service;


	@BeforeEach
	void setUp() {
		service = new UploadFileServiceImpl();
		MockitoAnnotations.initMocks(this);
		when(mediaConfig.getMediaHost()).thenReturn("1");
		when(mediaConfig.getDocumentDir()).thenReturn("1");
		when(mediaConfig.getImageDir()).thenReturn("1");
		when(mediaConfig.getVideoDir()).thenReturn("1");
		when(mediaConfig.getSupportTypes()).thenReturn(new String[]{"jpg","pdf","mp4"});
		when(mediaConfig.getThumbnailSize()).thenReturn(20);
	}

	@AfterEach
	void clear() {
		Queue<File> queue = new ArrayDeque<>();
		File folder = Paths.get("1").toAbsolutePath().toAbsolutePath().toFile();
		if(folder == null) return;
		queue.add(folder);
		while(!queue.isEmpty()) {
			File top = queue.peek();
			queue.poll();
			if(top == null || top.listFiles() == null) continue;
			for(File file:top.listFiles()) {
				if(file.isDirectory()) queue.add(file);
				else file.delete();
			}
			top.deleteOnExit();
		}
		System.out.println("after");
	}

	@Test
	void init() throws IOException {
		Assertions.assertDoesNotThrow(() -> service.init());
	}

	@Test
	void uploadFilesImage() throws IOException {
		service.init();
		InputStream is = getClass().getResourceAsStream("viettel.jpg");
		MultipartFile multipartFile = new MockMultipartFile("file","D:/file.jpg","image/jpg", is);
		System.out.println(multipartFile.getInputStream());
		Response response = service.uploadFiles(new MultipartFile[]{multipartFile});
		System.out.println(response);
		assertThat(response.getCode(), Matchers.equalTo(200));
	}

	@Test
	void uploadFilesText() throws IOException {
		service.init();
		InputStream is = getClass().getResourceAsStream("test.pdf");
		MultipartFile multipartFile = new MockMultipartFile("file","D:/file.pdf","application/pdf", is);
		System.out.println(multipartFile.getInputStream());
		Response response = service.uploadFiles(new MultipartFile[]{multipartFile});
		System.out.println(response);
		assertThat(response.getCode(), Matchers.equalTo(200));
	}

//	@Test
//	void uploadFilesVideo() throws IOException {
//		service.init();
//		InputStream is = getClass().getResourceAsStream("viettel.jpg");
//		MultipartFile multipartFile = new MockMultipartFile("file","D:/file.mp4","video/mp4", is);
//		System.out.println(multipartFile.getInputStream());
//		Response response = service.uploadFiles(new MultipartFile[]{multipartFile});
//		System.out.println(response);
//		assertThat(response.getCode(), Matchers.equalTo(200));
//	}

	@Test
	void deleteFiles() {
		Response response = service.deleteFile(new String[]{"1","2"});
		assertThat(response.getCode(), Matchers.equalTo(200));
	}

	@Test
	void uploadFiles1ThrowException2() throws Exception {
		//TODO: sua dau vao phu hop voi nghiep vu
		MultipartFile[] files = null;

		//TODO: sua dau ra cua mock method de tao thanh cac testcase khac

		//TODO: thay doi assert result phu hop voi nghiep vu
		Assertions.assertThrows(Exception.class, () -> {
			service.uploadFiles(files);
		});
	}

	@Test
	void deleteFile2ThrowException1() throws Exception {
		//TODO: sua dau vao phu hop voi nghiep vu
		String[] filePaths = null;

		//TODO: thay doi assert result phu hop voi nghiep vu
		Assertions.assertThrows(Exception.class, () -> {
			service.deleteFile(filePaths);
		});
	}


}