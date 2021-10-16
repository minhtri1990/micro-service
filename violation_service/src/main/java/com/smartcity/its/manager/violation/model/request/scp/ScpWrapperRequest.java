package com.smartcity.its.manager.violation.model.request.scp;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ScpWrapperRequest {
   // private List<ScpReceiveRequest> data;
	List<ScpViolation> entities;
}
