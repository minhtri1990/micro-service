package com.viettel.parking.camera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viettel.parking.camera.data.model.DetectStream;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.repository.DetectStreamRepository;

@RestController
@RequestMapping("/live")
public class CameraDetectController {

	@Autowired
	private DetectStreamRepository detectStreamRepository;

	@RequestMapping(value = "/camera/{deviceIdCamera}/detect", method = RequestMethod.GET)
	public BaseResponse<DetectStream> getDetectStreamById(@PathVariable("deviceIdCamera") String deviceIdCamera) {
		DetectStream detect = detectStreamRepository.findByDeviceIdCamera(deviceIdCamera);
		BaseResponse<DetectStream> response = new BaseResponse<DetectStream>();
		if (detect != null) {
			response.setData(detect);
			response.setSuccess();
		}
		return response;
	}
}
