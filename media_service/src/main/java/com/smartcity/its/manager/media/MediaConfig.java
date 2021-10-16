package com.smartcity.its.manager.media;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration("MediaConfig")
@Data
public class MediaConfig {
	@Value("${file.storage.imageDir}")
	private String imageDir;

	@Value("${file.storage.documentDir}")
	private String documentDir;

	@Value("${file.storage.videoDir}")
	private String videoDir;

	@Value("${file.supportTypes}")
	private String[] supportTypes;

	@Value("${file.host}")
	private String mediaHost;

	private int thumbnailSize = 720;
}
