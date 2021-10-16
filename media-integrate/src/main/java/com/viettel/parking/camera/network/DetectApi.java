package com.viettel.parking.camera.network;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.viettel.parking.camera.data.dto.ObservationUpdate;
import com.viettel.parking.camera.data.model.DetectStream;
import com.viettel.parking.camera.network.reponse.DetectReponse;
import com.viettel.parking.camera.service.CameraService;
import com.viettel.parking.camera.utils.GsonUtils;

@Service
public class DetectApi extends BaseRestApi {

	private static final Logger LOG = LogManager.getLogger(DetectApi.class);

	@Autowired
	CameraService cameraService;

	/**
	 * getDetectk
	 * 
	 * @param camera
	 * @return
	 */
	public DetectReponse getDetect(DetectStream detectStream) {
		try {
			String uri = detectStream.getUrlAiDetecte();
			String configDetect = detectStream.getUrlConfigDetect();
			//String streaming = cameraService.getPlayBackUrl(detectStream.getDeviceIdCamera());
			String streaming  = detectStream.getStreamUrl();
			if(StringUtils.isEmpty(streaming)) {
				streaming = cameraService.getPlayBackUrl(detectStream.getDeviceIdCamera());
			}
			
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Accept", "application/json");
			httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
			final RestTemplate restTemplate = getHttpClientLocal();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
			final MultiValueMap<String, String> formVars = new LinkedMultiValueMap<>();
			formVars.add("threshold", "0.5");
			formVars.add("video_url", streaming);
			formVars.add("config", configDetect);
			final String result = restTemplate.postForObject(uri.toString(), formVars, String.class);
			DetectReponse response = GsonUtils.parseJsonToObject(result, DetectReponse.class);
			return response;
		} catch (Exception e) {
			LOG.info("Exception getDetect : " + e.getMessage());
			e.printStackTrace();
			LOG.info(e);
			return null;
		}

	}

	/**
	 * publish Detect
	 * 
	 * @param detectStream
	 * @param detectReponse
	 */
	public void publishDetect(DetectStream detectStream, DetectReponse detectReponse) {
		try {
			String uri = detectStream.getUrlSentiloPublish();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Accept", "application/json");
			httpHeaders.add("Content-Type", "application/json");
			httpHeaders.add("IDENTITY_KEY", detectStream.getSentiloIdentityKey());
			final RestTemplate restTemplate = getHttpClient();

			ObservationUpdate data = new ObservationUpdate();
			if (detectReponse == null) {
				data.addObservation("-1");
			} else {
				data.addObservation(String.valueOf(detectReponse.getHeader().getNumObjects()));
			}

			LOG.info("publishDetect : " + uri);
			LOG.info("sentiloIdentityKey : " + detectStream.getSentiloIdentityKey());
			LOG.info("detect response : " + GsonUtils.toString(data));

			HttpEntity<ObservationUpdate> request = new HttpEntity<>(data, httpHeaders);
			final ResponseEntity<String> result = restTemplate.exchange(uri.toString(), HttpMethod.PUT, request,
					String.class);
			LOG.info(result);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info(e);
			LOG.info("Exception publishDetect : " + e.getMessage());
		}

	}

}
