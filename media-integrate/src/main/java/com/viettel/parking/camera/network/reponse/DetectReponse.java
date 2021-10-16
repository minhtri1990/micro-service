package com.viettel.parking.camera.network.reponse;

import com.viettel.parking.camera.data.dto.HeaderData;

public class DetectReponse {
	private HeaderData header;
	private Object result;

	public HeaderData getHeader() {
		return header;
	}

	public void setHeader(HeaderData header) {
		this.header = header;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
