package com.viettel.parking.camera.data.dto;

public class HeaderData {
	private Integer frameHeight;
	private Integer frameWidth;
	private String name;
	private Integer numObjects;
	private Double threshold;
	private String version;

	public Integer getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(Integer frameHeight) {
		this.frameHeight = frameHeight;
	}

	public Integer getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(Integer frameWidth) {
		this.frameWidth = frameWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumObjects() {
		return numObjects;
	}

	public void setNumObjects(Integer numObjects) {
		this.numObjects = numObjects;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
