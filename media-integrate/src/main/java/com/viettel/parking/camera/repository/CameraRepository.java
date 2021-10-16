package com.viettel.parking.camera.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.viettel.parking.camera.data.model.Camera;

public interface CameraRepository extends MongoRepository<Camera, String> {

	Camera findBy_id(ObjectId _id);

	Camera findByDeviceIdCamera(String deviceIdCamera);
}
