package com.viettel.parking.camera.network.lib;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

public abstract class SoapBuilder {

	private RestTemplate restClient = new RestTemplate();

	private String url;

	private String contentType;

	private HttpHeaders httpHeaders = new HttpHeaders();

	public void println(String s) {
		if (HttpNetwork.isLog()) {
			System.out.println(s);
		}
	}

	public SoapBuilder setRestClient(RestTemplate restClient) {
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

	public SoapBuilder(String url) {
		super();
		this.url = url;
	}

	public SoapBuilder setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public SoapBuilder addHttpHeaders(String headerName, @Nullable String headerValue) {
		this.httpHeaders.add(headerName, headerValue);
		return this;
	}

	public abstract SoapBuilder addXmlBody(String object);

	public abstract SoapBuilder build();

	public abstract <T> T getResponse(Class<T> objectClass);

	public abstract String getStringResponse();

}
