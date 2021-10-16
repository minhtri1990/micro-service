package com.viettel.parking.camera.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.parking.camera.data.dto.Profile;
import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.data.model.Camera;
import com.viettel.parking.camera.network.lib.HttpNetwork;
import com.viettel.parking.camera.network.reponse.ListCameraResponse;
import com.viettel.parking.camera.network.reponse.LoginVMSResponse;
import com.viettel.parking.camera.utils.EncodeUtil;

@Service
public class VmsApi extends BaseRestApi {

	@Autowired
	private ApiEndPoint apiEndPoint;

	/**
	 * login hệ thống vms
	 */
	public LoginVMSResponse login(AccountVMS account) {
		String username = account.getUsername();
		String keypwd = EncodeUtil.sha256hex(account.getKeypwd());
		String loginApi = String.format(apiEndPoint.getApiLogin(), username, keypwd);
		// dang nhap vao he thong vms
		LoginVMSResponse response = HttpNetwork.get(loginApi).setRestClient(getHttpClient()).build()
				.getResponse(LoginVMSResponse.class);
		return response;
	}

	/**
	 * login hệ thống vms
	 */
	public LoginVMSResponse reLogin(AccountVMS account) {
		String username = account.getUsername();
		String keypwd = account.getKeypwd();
		String loginApi = String.format(apiEndPoint.getApiLogin(), username, keypwd);
		// dang nhap vao he thong vms
		LoginVMSResponse response = HttpNetwork.get(loginApi).setRestClient(getHttpClient()).build()
				.getResponse(LoginVMSResponse.class);
		return response;
	}

	/**
	 * validate token
	 */
	public LoginVMSResponse validateToken(String token) {
		// validate token
		String apiValidateToken = String.format(apiEndPoint.getApiValidateToken(), token);
		// truong hợp token het han thi login
		// dang nhap vao he thong vms
		LoginVMSResponse response = HttpNetwork.get(apiValidateToken).setRestClient(getHttpClient()).build()
				.getResponse(LoginVMSResponse.class);
		return response;
	}

	/**
	 * lay danh sach camera
	 * 
	 * @param storeAccount
	 * @return
	 */
	public ListCameraResponse getCamera(AccountVMS storeAccount) {
		// lay danh sach camera
		String version = apiEndPoint.getVersion();
		long site_id = storeAccount.getSite().getSite_id();
		String token = storeAccount.getToken();
		String apiGetCamera = String.format(apiEndPoint.getApiGetCamera(), version, String.valueOf(site_id), token);
		// luu tru danh sach camera
		ListCameraResponse response = HttpNetwork.get(apiGetCamera).setRestClient(getHttpClient()).build()
				.getResponse(ListCameraResponse.class);
		return response;
	}

	/**
	 * lấy sanh sách play back
	 * 
	 * @param camera
	 * @return
	 */
	public String getPlayBack(Camera camera) {
		String camera_id = camera.getDeviceIdCamera();
		Profile profile = camera.getProfile(Camera.TYPE_HD);
		long profileId = profile.getOutput_camera_id();
		long duaration = 60000;
		long startTime = System.currentTimeMillis() - duaration;
		// "playback/mpegts/${camera_id}xyz${profile_id}_${start_time}_${duaration}.m3u8";
		String getApiPlayBack = String.format(apiEndPoint.getApiPlayBack(), camera_id, String.valueOf(profileId),
				String.valueOf(startTime), String.valueOf(duaration));
		return getApiPlayBack;
	}

	/**
	 * lấy sanh sách play back
	 * 
	 * @param camera
	 * @return
	 */
	public String getPlayBackByDuration(Camera camera, long second) {
		String camera_id = camera.getDeviceIdCamera();
		Profile profile = camera.getProfile(Camera.TYPE_HD);
		long profileId = profile.getOutput_camera_id();
		long duaration = second * 1000;
		if (duaration < 60000) {
			duaration = 60000;
		}
		long startTime = System.currentTimeMillis() - duaration;
		// "playback/mpegts/${camera_id}xyz${profile_id}_${start_time}_${duaration}.m3u8";
		String getApiPlayBack = String.format(apiEndPoint.getApiPlayBack(), camera_id, String.valueOf(profileId),
				String.valueOf(startTime), String.valueOf(duaration));
		return getApiPlayBack;
	}

//	public static void main(String[] args) {
//		System.out.println(System.currentTimeMillis() - 40000);
//
//	}

}
