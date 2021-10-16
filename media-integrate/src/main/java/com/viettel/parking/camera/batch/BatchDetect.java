package com.viettel.parking.camera.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.viettel.parking.camera.data.model.DetectStream;
import com.viettel.parking.camera.network.DetectApi;
import com.viettel.parking.camera.network.reponse.DetectReponse;
import com.viettel.parking.camera.repository.DetectStreamRepository;
import com.viettel.parking.camera.service.DetectStreamService;
import com.viettel.parking.camera.utils.GsonUtils;

@EnableScheduling
@Component
public class BatchDetect {

	private static final Log logger = LogFactory.getLog(BatchDetect.class);

	@Autowired
	private DetectStreamRepository detectStreamRepository;
	@Autowired
	private DetectStreamService detectStreamService;
	@Autowired
	private DetectApi detectApi;

	@Async
	@Scheduled(fixedRate = 30000, initialDelay = 10000)
	public void scheduleCallDetect() throws InterruptedException {
		// get all stream detect
		List<DetectStream> detectStreams = detectStreamRepository.findAll();

		if (detectStreams == null || detectStreams.size() == 0) {
			return;
		}
		// check running status
		for (DetectStream detectStream : detectStreams) {
			if (validate(detectStream)) {
				process(detectStream);
			}
		}
	}

	/**
	 * validate trang thai detect Stream
	 * 
	 * @param detectStream
	 * @return
	 */
	private boolean validate(DetectStream detectStream) {
		if (detectStream.getLastDetectDate() == null) {
			return true;
		}

		Calendar lastDetectDate = Calendar.getInstance();
		lastDetectDate.setTime(detectStream.getLastDetectDate());
		lastDetectDate.add(Calendar.MINUTE, 3);

		Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(new Date());
		if (lastDetectDate.before(currentTime)) {
			return true;
		}

		if (detectStream.isRuningDetect()) {
			return false;
		}
		return true;
	}

	/**
	 * xu ly detect
	 * 
	 * @param detectStream
	 */
	private void process(DetectStream detectStream) {
		Thread thread = new Thread() {
			public void run() {
				// marker stream
				detectStream.setRuningDetect(true);
				detectStreamService.update(detectStream);
				// call detect api
				DetectReponse response = detectApi.getDetect(detectStream);
				if (response == null) {
					detectStream.setRuningDetect(false);
					detectStreamService.update(detectStream);
					return;
				}
				// check số lượng
				if (isPublishDetect(response, detectStream)) {
					// notifi sentilo
					detectApi.publishDetect(detectStream, response);
				}
				// luu lai db
				DetectStream detect = detectStreamRepository.findByDeviceIdCamera(detectStream.getDeviceIdCamera());
				if (detect != null) {
					detect.setRuningDetect(false);
					detect.setLastDetectDate(new Date());
					detect.setLastDetectValue(GsonUtils.parseToJson(response));
					detectStreamService.update(detect);
				}
			}

		};
		thread.start();
	}

	/**
	 * isPublishDetect
	 * 
	 * @param response
	 * @param detectStream
	 * @return
	 */
	private boolean isPublishDetect(DetectReponse response, DetectStream detectStream) {
		logger.info("detect response :  " + GsonUtils.toString(response));
		Integer currentNumber = response.getHeader().getNumObjects();

		if (StringUtils.isEmpty(detectStream.getSentiloIdentityKey())) {
			logger.info("return SentiloIdentityKey false");
			return false;
		}

		if (currentNumber == null) {
			logger.info("return 1 false");
			return false;
		}

		DetectStream detect = detectStreamRepository.findByDeviceIdCamera(detectStream.getDeviceIdCamera());
		if (detect == null) {
			return true;
		}
		String lastDetectValue = detect.getLastDetectValue();
		if (StringUtils.isEmpty(lastDetectValue)) {
			return true;
		}
		DetectReponse lastDetect = GsonUtils.parseJsonToObject(detect.getLastDetectValue(), DetectReponse.class);
		logger.info("lastDetect response :  " + GsonUtils.toString(lastDetect));

		if (lastDetect == null) {
			return true;
		}

		Integer lastNumber = lastDetect.getHeader().getNumObjects();
		if (lastNumber == null) {
			return true;
		}

		logger.info("lastNumber : " + lastNumber.intValue());
		logger.info("currentNumber : " + currentNumber.intValue());

		if (lastNumber.intValue() != currentNumber.intValue()) {
			logger.info("return 2 true");
			return true;
		}

		logger.info("return 3 false");
		return false;
	}

}
