package com.viettel.parking.camera.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.viettel.parking.camera.data.model.AccountVMS;

public interface AccountVMSRepository extends MongoRepository<AccountVMS, String> {

	AccountVMS findBy_id(ObjectId _id);

	AccountVMS findByUsername(String username);

	AccountVMS findByUsernameAndKeypwd(String username, String keypwd);

	AccountVMS findByTokenApi(String tokenApi);

	AccountVMS findByUserId(long userId);
}
