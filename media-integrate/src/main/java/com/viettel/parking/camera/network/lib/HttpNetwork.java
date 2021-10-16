package com.viettel.parking.camera.network.lib;

public class HttpNetwork {

	public static boolean log = false;

	public static boolean isLog() {
		return log;
	}

	public static void setLog(boolean log) {
		HttpNetwork.log = log;
	}

	public static GetRequestBuilder get(String url) {
		return new GetRequestBuilder(url);
	}

	public static DeleteRequestBuilder delete(String url) {
		return new DeleteRequestBuilder(url);
	}

	public static PostRequestBuilder post(String url) {
		return new PostRequestBuilder(url);
	}

	public static MultiPartBuilder postMultiPart(String url) {
		return new MultiPartBuilder(url);
	}

	public static PutRequestBuilder put(String url) {
		return new PutRequestBuilder(url);
	}

	public static SoapRequestBuilder soap(String url) {
		return new SoapRequestBuilder(url);
	}
}
