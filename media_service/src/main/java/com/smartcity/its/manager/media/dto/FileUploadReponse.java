package com.smartcity.its.manager.media.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadReponse {
	@JsonProperty("files")
	private List<FileProperties> fileProperties;
}
