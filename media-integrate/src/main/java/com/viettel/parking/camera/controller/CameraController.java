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

import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.data.model.Camera;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.repository.CameraRepository;
import com.viettel.parking.camera.service.CameraService;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

	@Autowired
	private CameraRepository cameraRepository;
	@Autowired
	private CameraService cameraService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseResponse<List<Camera>> getAllCamera() {
		BaseResponse<List<Camera>> response = new BaseResponse<List<Camera>>();
		response.setSuccess();
		response.setData(cameraRepository.findAll());
		return response;
	}

	@RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
	public BaseResponse<Camera> getCameraById(@PathVariable("id") ObjectId id) {
		BaseResponse<Camera> response = new BaseResponse<Camera>();
		response.setData(cameraRepository.findBy_id(id));
		response.setSuccess();
		return response;
	}

	@RequestMapping(value = "/deviceIdCamera/{deviceIdCamera}", method = RequestMethod.GET)
	public BaseResponse<Camera> getCameraById(@PathVariable("deviceIdCamera") String deviceIdCamera) {
		return cameraService.findByDeviceIdCamera(deviceIdCamera);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public void modifyCameraById(@PathVariable("id") ObjectId id, @Valid @RequestBody Camera camera) {
		camera.set_id(id);
		cameraRepository.save(camera);
	}

	@RequestMapping(value = "/sync", method = RequestMethod.POST)
	public BaseResponse<List<Camera>> syncCamera(@Valid @RequestBody AccountVMS account) {
		BaseResponse<List<Camera>> cameras = cameraService.syncCamera(account);
		return cameras;
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public void deleteCamera(@PathVariable ObjectId id) {
		cameraRepository.delete(cameraRepository.findBy_id(id));
	}
}
