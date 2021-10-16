package com.viettel.parking.camera.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.parking.camera.data.model.DetectStream;
import com.viettel.parking.camera.repository.DetectStreamRepository;

@Service
public class DetectStreamService extends BaseService {

	@Autowired
	private DetectStreamRepository detectStreamRepository;

	/**
	 * Lay url play back
	 * 
	 * @param deviceIdCamera
	 * @return
	 */
	public void update(DetectStream object) {
		DetectStream store = detectStreamRepository.findByDeviceIdCamera(object.getDeviceIdCamera());
		if (store != null) {
			detectStreamRepository.save(object);
		}
	}

}
