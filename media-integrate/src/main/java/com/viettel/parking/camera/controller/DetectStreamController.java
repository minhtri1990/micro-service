package com.viettel.parking.camera.controller;

import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viettel.parking.camera.data.model.DetectStream;
import com.viettel.parking.camera.repository.DetectStreamRepository;
import com.viettel.parking.camera.service.CameraService;

@RestController
@RequestMapping("/api/detectStream")
public class DetectStreamController {

	@Autowired
	private DetectStreamRepository detectStreamRepository;
	@Autowired
	CameraService cameraService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<DetectStream> getAllDetectStream() {
		return detectStreamRepository.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public DetectStream getDetectStreamById(@PathVariable("id") ObjectId id) {
		return detectStreamRepository.findBy_id(id);
	}

	@RequestMapping(value = "/playback/{deviceIdCamera}", method = RequestMethod.GET)
	public String getPlayBackUrl(@PathVariable("deviceIdCamera") String deviceIdCamera) {
		String streaming = cameraService.getPlayBackUrl(deviceIdCamera);
		return streaming;
	}

	@RequestMapping(value = "/playback/{deviceIdCamera}/{second}", method = RequestMethod.GET)
	public String getPlayBackUrl(@PathVariable("deviceIdCamera") String deviceIdCamera,
			@PathVariable("second") long second) {
		String streaming = cameraService.getPlayBackByDuration(deviceIdCamera, second);
		return streaming;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public void modifyDetectStreamById(@PathVariable("id") ObjectId id, @Valid @RequestBody DetectStream detectStream) {
		detectStream.set_id(id);
		detectStreamRepository.save(detectStream);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public DetectStream createDetectStream(@Valid @RequestBody DetectStream detectStream) {
		detectStream.set_id(ObjectId.get());
		detectStream.setRuningDetect(false);
		detectStreamRepository.save(detectStream);
		return detectStream;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public void deleteDetectStream(@PathVariable ObjectId id) {
		detectStreamRepository.delete(detectStreamRepository.findBy_id(id));
	}
}
