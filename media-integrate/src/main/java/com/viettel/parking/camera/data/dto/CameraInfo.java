package com.viettel.parking.camera.data.dto;

import java.util.List;

public class CameraInfo {
	public String device_id_camera;
	public String postion;
	public long camera_id;
	public long site_id;
	public String type_camera;
	public long server_id;
	public String device_server_id;
	public long historyDay;
	public long deployment_situation;
	public long deployment_status;
	public long is_ptz;
	public String lastupdatetime;
	public long camera_order;
	public String camera_role;
	public PositionMap position_map;
	public String camera_status;
	public String cdn_type;
	public List<SourceProfile> source_profile = null;
	public List<Profile> profile = null;

	public String getDevice_id_camera() {
		return device_id_camera;
	}

	public void setDevice_id_camera(String device_id_camera) {
		this.device_id_camera = device_id_camera;
	}

	public String getPostion() {
		return postion;
	}

	public void setPostion(String postion) {
		this.postion = postion;
	}

	public long getCamera_id() {
		return camera_id;
	}

	public void setCamera_id(long camera_id) {
		this.camera_id = camera_id;
	}

	public long getSite_id() {
		return site_id;
	}

	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}

	public String getType_camera() {
		return type_camera;
	}

	public void setType_camera(String type_camera) {
		this.type_camera = type_camera;
	}

	public long getServer_id() {
		return server_id;
	}

	public void setServer_id(long server_id) {
		this.server_id = server_id;
	}

	public String getDevice_server_id() {
		return device_server_id;
	}

	public void setDevice_server_id(String device_server_id) {
		this.device_server_id = device_server_id;
	}

	public long getHistoryDay() {
		return historyDay;
	}

	public void setHistoryDay(long historyDay) {
		this.historyDay = historyDay;
	}

	public long getDeployment_situation() {
		return deployment_situation;
	}

	public void setDeployment_situation(long deployment_situation) {
		this.deployment_situation = deployment_situation;
	}

	public long getDeployment_status() {
		return deployment_status;
	}

	public void setDeployment_status(long deployment_status) {
		this.deployment_status = deployment_status;
	}

	public long getIs_ptz() {
		return is_ptz;
	}

	public void setIs_ptz(long is_ptz) {
		this.is_ptz = is_ptz;
	}

	public String getLastupdatetime() {
		return lastupdatetime;
	}

	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}

	public long getCamera_order() {
		return camera_order;
	}

	public void setCamera_order(long camera_order) {
		this.camera_order = camera_order;
	}

	public String getCamera_role() {
		return camera_role;
	}

	public void setCamera_role(String camera_role) {
		this.camera_role = camera_role;
	}

	public PositionMap getPosition_map() {
		return position_map;
	}

	public void setPosition_map(PositionMap position_map) {
		this.position_map = position_map;
	}

	public String getCamera_status() {
		return camera_status;
	}

	public void setCamera_status(String camera_status) {
		this.camera_status = camera_status;
	}

	public String getCdn_type() {
		return cdn_type;
	}

	public void setCdn_type(String cdn_type) {
		this.cdn_type = cdn_type;
	}

	public List<SourceProfile> getSource_profile() {
		return source_profile;
	}

	public void setSource_profile(List<SourceProfile> source_profile) {
		this.source_profile = source_profile;
	}

	public List<Profile> getProfile() {
		return profile;
	}

	public void setProfile(List<Profile> profile) {
		this.profile = profile;
	}

}
