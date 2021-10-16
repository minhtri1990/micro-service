package com.viettel.parking.camera.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.viettel.parking.camera.data.model.Camera;
import com.viettel.parking.camera.data.model.DetectStream;

public interface DetectStreamRepository extends MongoRepository<DetectStream, String> {
	DetectStream findBy_id(ObjectId _id);

	DetectStream findByDeviceIdCamera(String deviceIdCamera);
}
