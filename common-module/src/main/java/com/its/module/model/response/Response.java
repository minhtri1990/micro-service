package com.its.module.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> extends BaseResponse {
	private Integer code;
	private String message;
	private Meta meta;
	private T data;
	private Integer errorCode;
	private String devMessage;
}
