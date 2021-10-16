package com.viettel.parking.camera.network.reponse;

import java.util.List;

import com.viettel.parking.camera.data.dto.CameraInfo;

public class ListCameraResponse {

	public List<CameraInfo> CAMS_LIST = null;
	public long TOTAL;
	public long PAGE;
	public long PER_PAGE;

	public List<CameraInfo> getCAMS_LIST() {
		return CAMS_LIST;
	}

	public void setCAMS_LIST(List<CameraInfo> cAMS_LIST) {
		CAMS_LIST = cAMS_LIST;
	}

	public long getTOTAL() {
		return TOTAL;
	}

	public void setTOTAL(long tOTAL) {
		TOTAL = tOTAL;
	}

	public long getPAGE() {
		return PAGE;
	}

	public void setPAGE(long pAGE) {
		PAGE = pAGE;
	}

	public long getPER_PAGE() {
		return PER_PAGE;
	}

	public void setPER_PAGE(long pER_PAGE) {
		PER_PAGE = pER_PAGE;
	}

}
