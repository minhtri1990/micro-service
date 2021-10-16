package com.viettel.parking.camera.network.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class SoapRequestBuilder extends SoapBuilder {

	private String objectJsonBody;

	public SoapRequestBuilder(String url) {
		super(url);
	}

	@Override
	public SoapBuilder addXmlBody(String object) {
		this.objectJsonBody = object;
		return this;
	}

	@Override
	public SoapBuilder build() {
		return this;
	}

	@Override
	public <T> T getResponse(Class<T> objectClass) {
		HttpPost httpPost = new HttpPost(this.getUrl());
		println("---------- REQUEST TO VIETTEL -----------");
		println("Request data: " + this.objectJsonBody);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
		httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
		httpPost.setConfig(requestConfig);
		StringEntity requestEntity = new StringEntity(this.objectJsonBody, "UTF-8");
		httpPost.setEntity(requestEntity);
		try {
			CloseableHttpResponse response = getHttpClient2().execute(httpPost);
			String out = EntityUtils.toString(response.getEntity(), "UTF-8");
			String responseString = parseData(out);
			println("parseData data: " + responseString);
			JSONObject jsonObject = JSONObject.fromObject(responseString);
			String data = jsonObject.getString("data");
			println("Response data: " + data);
			return ParseUtil.getPaser().parseJsonToObject(data, objectClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// verify du lieu Viettel tra ve
		return null;

	}

	@Override
	public String getStringResponse() {
		HttpPost httpPost = new HttpPost(this.getUrl());
		println("---------- REQUEST TO VIETTEL -----------");
		println("Request data: " + this.objectJsonBody);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).build();
		httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
		httpPost.addHeader("Accept", "text/xml;charset=UTF-8");
		httpPost.setConfig(requestConfig);
		StringEntity requestEntity = new StringEntity(this.objectJsonBody, "UTF-8");
		httpPost.setEntity(requestEntity);
		try {
			CloseableHttpResponse response = getHttpClient2().execute(httpPost);
			String out = EntityUtils.toString(response.getEntity(), "UTF-8");
			String responseString = parseData(out);
			println("parseData data: " + responseString);
			return responseString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// verify du lieu Viettel tra ve
		return null;

	}

	private static String parseData(String out) {
		try {
			return StringEscapeUtils.unescapeXml(StringUtils.substringsBetween(out, "<return>", "</return>")[0]);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private CloseableHttpClient getHttpClient2() {
		try {
			HttpHost host = new HttpHost(this.getUrl(), 443);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("https", new SSLConnectionSocketFactory(getSslContextAll(), getHostNameVerifierAll()))
					.register("http", new PlainConnectionSocketFactory()).build();
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			cm.setMaxTotal(200);
			cm.setDefaultMaxPerRoute(20);
			cm.setMaxPerRoute(new HttpRoute(host), 50);
			HttpClientBuilder builder = HttpClients.custom().setConnectionManager(cm);
//			if (isUseProxy()) {
//				builder.setProxy(new HttpHost(HttpNetwork.getProxyHost(), HttpNetwork.getProxyPort()));
//			}
			return builder.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// todo khong su dung tren moi truong production
	private static SSLContext getSslContextAll() {
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			// no keystore
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			return sc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// todo khong su dung tren moi truong production
	private static X509HostnameVerifier getHostNameVerifierAll() {
		return new X509HostnameVerifier() {
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}

			public void verify(String s, SSLSocket sslSocket) throws IOException {
			}

			public void verify(String s, X509Certificate x509Certificate) throws SSLException {
			}

			public void verify(String s, String[] strings, String[] strings1) throws SSLException {
			}
		};
	}

	public String soapMessageToString(SOAPMessage message) {
		String result = null;

		if (message != null) {
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				result = baos.toString();
			} catch (Exception e) {
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return result;
	}
}
