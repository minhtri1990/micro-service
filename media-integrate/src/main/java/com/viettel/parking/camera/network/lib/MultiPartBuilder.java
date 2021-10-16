package com.viettel.parking.camera.network.lib;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MultiPartBuilder extends RequestBuilder {
	private MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();

	public MultiPartBuilder(String url) {
		super(url);
	}

	@Override
	public RequestBuilder addPathParameter(String key, String value) {
		formVars.add(key, value);
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
		RestTemplate restTemplate = this.getRestClient();
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		final String result = restTemplate.postForObject(this.getUrl(), formVars, String.class);
		return ParseUtil.getPaser().parseJsonToObject(result, objectClass);
	}

}
