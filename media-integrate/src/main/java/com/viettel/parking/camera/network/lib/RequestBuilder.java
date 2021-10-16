package com.viettel.parking.camera.network.lib;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

public abstract class RequestBuilder {

	private RestTemplate restClient = new RestTemplate();

	private String url;

	private String contentType;

	private HttpHeaders httpHeaders = new HttpHeaders();

	public RequestBuilder setRestClient(RestTemplate restClient) {
		this.restClient = restClient;
		return this;
	}

	protected RestTemplate getRestClient() {
		return restClient;
	}

	public String getUrl() {
		return url;
	}

	public String getContentType() {
		return contentType;
	}

	public HttpHeaders getHttpHeaders() {
		return httpHeaders;
	}

	public RequestBuilder(String url) {
		super();
		this.url = url;
	}

	public RequestBuilder setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public RequestBuilder addHttpHeaders(String headerName, @Nullable String headerValue) {
		this.httpHeaders.add(headerName, headerValue);
		return this;
	}

	public abstract RequestBuilder addPathParameter(String key, String value);

	public abstract RequestBuilder addApplicationJsonBody(Object object);

	public abstract RequestBuilder build();

	public abstract <T> T getResponse(Class<T> objectClass);

}
