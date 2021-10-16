package com.its.auth_service.model.odp.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileResponse {
	private String originalName;
	private int size;
	private String host;
	private String relLocation;
	private String fileType;
	private String thumbnail;
	private String fileExtension;

	public String getUrl() {
		return host + relLocation;
	}

}
