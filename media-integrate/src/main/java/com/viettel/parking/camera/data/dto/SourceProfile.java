package com.viettel.parking.camera.data.dto;

import java.util.List;

public class SourceProfile {
	public String name;
	public long output_camera_id;
	public String bitrate;
	public String resolution;
	public String type_output;
	public List<Stream> streams = null;
	public String encodeMode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getOutput_camera_id() {
		return output_camera_id;
	}

	public void setOutput_camera_id(long output_camera_id) {
		this.output_camera_id = output_camera_id;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getType_output() {
		return type_output;
	}

	public void setType_output(String type_output) {
		this.type_output = type_output;
	}

	public List<Stream> getStreams() {
		return streams;
	}

	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}

	public String getEncodeMode() {
		return encodeMode;
	}

	public void setEncodeMode(String encodeMode) {
		this.encodeMode = encodeMode;
	}

}
