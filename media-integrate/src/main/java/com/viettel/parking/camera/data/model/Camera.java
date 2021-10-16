package com.viettel.parking.camera.data.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.viettel.parking.camera.data.dto.PositionMap;
import com.viettel.parking.camera.data.dto.Profile;
import com.viettel.parking.camera.data.dto.SourceProfile;
import com.viettel.parking.camera.data.dto.Stream;

public class Camera {

	public static String TYPE_HD = "HD";
	public static String TYPE_SD = "SD";

	@Id
	private ObjectId _id;

	public String deviceIdCamera;
	public String postion;
	public long cameraId;
	public long siteId;
	public String typeCamera;
	public long serverId;
	public String deviceServerId;
	public long historyDay;
	public long deploymentSituation;
	public long deploymentStatus;
	public long isPtz;
	public String lastupdatetime;
	public long cameraOrder;
	public String cameraRole;
	public PositionMap positionMap;
	public String cameraStatus;
	public String cdnType;
	public List<SourceProfile> sourceProfile = null;
	public List<Profile> profile = null;

	public String get_id() {
		return _id.toHexString();
	}

	public ObjectId getObjectId() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getDeviceIdCamera() {
		return deviceIdCamera;
	}

	public void setDeviceIdCamera(String deviceIdCamera) {
		this.deviceIdCamera = deviceIdCamera;
	}

	public String getPostion() {
		return postion;
	}

	public void setPostion(String postion) {
		this.postion = postion;
	}

	public long getCameraId() {
		return cameraId;
	}

	public void setCameraId(long cameraId) {
		this.cameraId = cameraId;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getTypeCamera() {
		return typeCamera;
	}

	public void setTypeCamera(String typeCamera) {
		this.typeCamera = typeCamera;
	}

	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	public String getDeviceServerId() {
		return deviceServerId;
	}

	public void setDeviceServerId(String deviceServerId) {
		this.deviceServerId = deviceServerId;
	}

	public long getHistoryDay() {
		return historyDay;
	}

	public void setHistoryDay(long historyDay) {
		this.historyDay = historyDay;
	}

	public long getDeploymentSituation() {
		return deploymentSituation;
	}

	public void setDeploymentSituation(long deploymentSituation) {
		this.deploymentSituation = deploymentSituation;
	}

	public long getDeploymentStatus() {
		return deploymentStatus;
	}

	public void setDeploymentStatus(long deploymentStatus) {
		this.deploymentStatus = deploymentStatus;
	}

	public long getIsPtz() {
		return isPtz;
	}

	public void setIsPtz(long isPtz) {
		this.isPtz = isPtz;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public long getCameraOrder() {
		return cameraOrder;
	}

	public void setCameraOrder(long cameraOrder) {
		this.cameraOrder = cameraOrder;
	}

	public String getCameraRole() {
		return cameraRole;
	}

	public void setCameraRole(String cameraRole) {
		this.cameraRole = cameraRole;
	}

	public PositionMap getPositionMap() {
		return positionMap;
	}

	public void setPositionMap(PositionMap positionMap) {
		this.positionMap = positionMap;
	}

	public String getCameraStatus() {
		return cameraStatus;
	}

	public void setCameraStatus(String cameraStatus) {
		this.cameraStatus = cameraStatus;
	}

	public String getCdnType() {
		return cdnType;
	}

	public void setCdnType(String cdnType) {
		this.cdnType = cdnType;
	}

	public List<SourceProfile> getSourceProfile() {
		return sourceProfile;
	}

	public void setSourceProfile(List<SourceProfile> sourceProfile) {
		this.sourceProfile = sourceProfile;
	}

	public List<Profile> getProfile() {
		return profile;
	}

	public void setProfile(List<Profile> profile) {
		this.profile = profile;
	}

	public String getWss(String type) {
		List<Profile> proFiles = getProfile();
		for (Profile profile : proFiles) {
			String name = profile.getName();
			if (StringUtils.isNotEmpty(name) && name.contains(type)) {
				List<Stream> streams = profile.getStreams();
				for (Stream stream : streams) {
					if (stream.getProtocol().contains("WS")) {
						if (stream.getSource().startsWith("wss")) {
							return stream.getSource();
						} else {
							return stream.getSource().replace("ws://", "wss://");
						}

					}
				}
			}
		}
		return "";
	}

	public Profile getProfile(String type) {
		List<Profile> proFiles = getProfile();
		for (Profile profile : proFiles) {
			String name = profile.getName();
			if (StringUtils.isNotEmpty(name) && name.contains(type)) {
				return profile;
			}
		}
		return null;
	}
}
