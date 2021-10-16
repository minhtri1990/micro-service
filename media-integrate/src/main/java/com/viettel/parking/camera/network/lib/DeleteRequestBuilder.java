package com.viettel.parking.camera.network.lib;

import org.springframework.web.client.RestTemplate;

public class DeleteRequestBuilder extends RequestBuilder {

	public DeleteRequestBuilder(String url) {
		super(url);
	}

	@Override
	public RequestBuilder addPathParameter(String key, String value) {
		return this;
	}

	@Override
	public RequestBuilder addApplicationJsonBody(Object object) {
		return this;
	}

	@Override
	public RequestBuilder build() {
		return this;
	}

	@Override
	public <T> T getResponse(Class<T> objectClass) {
		RestTemplate restTemplate = getRestClient();
		restTemplate.delete(getUrl());
		return null;
	}

}
