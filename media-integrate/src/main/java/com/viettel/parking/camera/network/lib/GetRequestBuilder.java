package com.viettel.parking.camera.network.lib;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetRequestBuilder extends RequestBuilder {

	private HashMap<String, String> pathParameter = new HashMap<>();

	public GetRequestBuilder(String url) {
		super(url);
	}


	@Override
	public RequestBuilder addPathParameter(String key, String value) {
		pathParameter.put(key, value);
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
		String uri = this.getUrl();
		for (Map.Entry<String, String> entry : pathParameter.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			uri = uri.replaceAll("{" + key + "}", value);
		}
		ResponseEntity<T> response = restTemplate.getForEntity(uri, objectClass);
		return response.getBody();
	}

}
