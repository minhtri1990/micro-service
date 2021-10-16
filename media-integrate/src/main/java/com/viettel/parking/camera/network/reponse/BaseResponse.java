package com.viettel.parking.camera.network.reponse;

public class BaseResponse<T> {

	private boolean success;
	private String message;
	private long errorCode = 0;
	private T data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess() {
		this.success = true;
	}

	public void setError() {
		this.success = false;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

}
