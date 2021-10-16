package com.its.auth_service.model.odp.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UploadFileResponse {
	List<FileResponse> files;
}
