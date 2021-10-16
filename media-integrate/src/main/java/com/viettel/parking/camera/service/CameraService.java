package com.viettel.parking.camera.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.parking.camera.data.dto.CameraInfo;
import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.data.model.Camera;
import com.viettel.parking.camera.network.VmsApi;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.network.reponse.ListCameraResponse;
import com.viettel.parking.camera.network.reponse.LoginVMSResponse;
import com.viettel.parking.camera.repository.AccountVMSRepository;
import com.viettel.parking.camera.repository.CameraRepository;

@Service
public class CameraService extends BaseService {

	@Autowired
	private VmsApi vmsApi;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountVMSRepository accountVMSRepository;
	@Autowired
	private CameraRepository cameraRepository;

	public BaseResponse<List<Camera>> syncCamera(@Valid AccountVMS account) {
		AccountVMS storeAccount = accountVMSRepository.findByUsername(account.getUsername());
		// validate token
		LoginVMSResponse tokenRes = vmsApi.validateToken(storeAccount.getToken());
		if (!tokenRes.isSuccess()) {
			storeAccount = accountService.reloadToken(storeAccount);
		}
		BaseResponse<List<Camera>> response = new BaseResponse<List<Camera>>();
		ListCameraResponse listCameraResponse = vmsApi.getCamera(storeAccount);
		if (listCameraResponse == null) {
			response.setError();
			return response;
		}

		List<Camera> cameras = new ArrayList<Camera>();
		List<CameraInfo> CAMS_LIST = listCameraResponse.getCAMS_LIST();
		for (CameraInfo cameraInfo : CAMS_LIST) {
			// check duplicate
			Camera storeCamera = cameraRepository.findByDeviceIdCamera(cameraInfo.getDevice_id_camera());
			Camera camera = new Camera();
			if (storeCamera == null) {
				camera.set_id(ObjectId.get());
			} else {
				camera.set_id(storeCamera.getObjectId());
			}

			camera.setDeviceIdCamera(cameraInfo.getDevice_id_camera());
			camera.setPostion(cameraInfo.getPostion());
			camera.setCameraId(cameraInfo.getCamera_id());
			camera.setSiteId(cameraInfo.getSite_id());
			camera.setTypeCamera(cameraInfo.getType_camera());
			camera.setServerId(cameraInfo.getServer_id());
			camera.setDeviceServerId(cameraInfo.getDevice_server_id());
			camera.setHistoryDay(cameraInfo.getHistoryDay());
			camera.setDeploymentSituation(cameraInfo.getDeployment_situation());
			camera.setDeploymentStatus(cameraInfo.getDeployment_status());
			camera.setIsPtz(cameraInfo.getIs_ptz());
			camera.setLastupdatetime(cameraInfo.getLastupdatetime());
			camera.setCameraOrder(cameraInfo.getCamera_order());
			camera.setCameraRole(cameraInfo.getCamera_role());
			camera.setPositionMap(cameraInfo.getPosition_map());
			camera.setCameraStatus(cameraInfo.getCamera_status());
			camera.setCdnType(cameraInfo.getCdn_type());
			camera.setSourceProfile(cameraInfo.getSource_profile());
			camera.setProfile(cameraInfo.getProfile());
			cameraRepository.save(camera);
			cameras.add(camera);
		}

		response.setData(cameras);
		response.setSuccess();
		return response;
	}

	/**
	 * findByDeviceIdCamera
	 * 
	 * @param deviceIdCamera
	 * @return
	 */
	public BaseResponse<Camera> findByDeviceIdCamera(String deviceIdCamera) {
		AccountVMS storeAccount = accountVMSRepository.findAll().get(0);
		// validate token
		LoginVMSResponse tokenRes = vmsApi.validateToken(storeAccount.getToken());
		if (!tokenRes.isSuccess()) {
			storeAccount = accountService.reloadToken(storeAccount);
			syncCamera(storeAccount);
		}
		BaseResponse<Camera> response = new BaseResponse<Camera>();
		response.setSuccess();
		response.setData(cameraRepository.findByDeviceIdCamera(deviceIdCamera));
		return response;
	}

	/**
	 * Lay url play back
	 * 
	 * @param deviceIdCamera
	 * @return
	 */
	public String getPlayBackUrl(String deviceIdCamera) {
		// ko check token
		Camera camera = cameraRepository.findByDeviceIdCamera(deviceIdCamera);
		return vmsApi.getPlayBack(camera);
	}

	/**
	 * Lay url play back
	 * 
	 * @param deviceIdCamera
	 * @return
	 */
	public String getPlayBackByDuration(String deviceIdCamera, long second) {
		// ko check token
		Camera camera = cameraRepository.findByDeviceIdCamera(deviceIdCamera);
		return vmsApi.getPlayBackByDuration(camera, second);
	}

}
