package com.viettel.parking.camera.network.lib;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PutRequestBuilder extends RequestBuilder {
	private String objectJsonBody;

	public PutRequestBuilder(String url) {
		super(url);
	}

	@Override
	public RequestBuilder addPathParameter(String key, String value) {
		return this;
	}

	@Override
	public RequestBuilder addApplicationJsonBody(Object object) {
		this.objectJsonBody = ParseUtil.getPaser().getString(object);
		return this;
	}

	@Override
	public RequestBuilder build() {
		return this;
	}

	@Override
	public <T> T getResponse(Class<T> objectClass) {
		RestTemplate rest = this.getRestClient();
		MultiValueMap<String, String> headers = getHttpHeaders();
		HttpEntity<String> request = new HttpEntity<String>(objectJsonBody, headers);
		ResponseEntity<T> response = rest.exchange(getUrl(), HttpMethod.PUT, request, objectClass);
		return response.getBody();
	}

}
