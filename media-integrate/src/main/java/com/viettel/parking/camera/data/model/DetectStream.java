package com.viettel.parking.camera.data.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class DetectStream {

	@Id
	private ObjectId _id;
	public String deviceIdCamera;
	public String postion;

	private String urlConfigDetect;
	private String urlAiDetecte;
	private String urlSentiloPublish;
	private String sentiloIdentityKey;

	private String streamUrl;
	private String originAdress;

	private String lastDetectValue;
	private Date lastDetectDate;

	private boolean isRuningDetect = false;

	private Date createDate;
	private Date updateDate;

	public String get_id() {
		return _id.toHexString();
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

	public String getUrlConfigDetect() {
		return urlConfigDetect;
	}

	public void setUrlConfigDetect(String urlConfigDetect) {
		this.urlConfigDetect = urlConfigDetect;
	}

	public String getUrlAiDetecte() {
		return urlAiDetecte;
	}

	public void setUrlAiDetecte(String urlAiDetecte) {
		this.urlAiDetecte = urlAiDetecte;
	}

	public String getUrlSentiloPublish() {
		return urlSentiloPublish;
	}

	public void setUrlSentiloPublish(String urlSentiloPublish) {
		this.urlSentiloPublish = urlSentiloPublish;
	}

	public String getSentiloIdentityKey() {
		return sentiloIdentityKey;
	}

	public void setSentiloIdentityKey(String sentiloIdentityKey) {
		this.sentiloIdentityKey = sentiloIdentityKey;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

	public void setStreamUrl(String streamUrl) {
		this.streamUrl = streamUrl;
	}

	public String getOriginAdress() {
		return originAdress;
	}

	public void setOriginAdress(String originAdress) {
		this.originAdress = originAdress;
	}

	public Date getLastDetectDate() {
		return lastDetectDate;
	}

	public void setLastDetectDate(Date lastDetectDate) {
		this.lastDetectDate = lastDetectDate;
	}

	public String getLastDetectValue() {
		return lastDetectValue;
	}

	public void setLastDetectValue(String lastDetectValue) {
		this.lastDetectValue = lastDetectValue;
	}

	public boolean isRuningDetect() {
		return isRuningDetect;
	}

	public void setRuningDetect(boolean isRuningDetect) {
		this.isRuningDetect = isRuningDetect;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
