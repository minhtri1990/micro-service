package com.smartcity.its.manager.violation.model.request.scp;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ScpReceiveRequest {
//    @NotNull
//    private String id;
	@NotNull
	private ScpViolation entities;
}
