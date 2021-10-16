package com.smartcity.its.manager.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class FileProperties {
	private String originalName;
	private String uploadedName;
	private long size;
	private String host;
	private String relLocation;
	private String fileType;
	private String thumbnail;
	private String fileExtension;
}
