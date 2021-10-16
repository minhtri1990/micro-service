package com.viettel.parking.camera.data.dto;

public class Stream {
	public String thumb;
	public String network;
	public String protocol;
	public String device_id;
	public long channel_id;
	public String source;

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public long getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(long channel_id) {
		this.channel_id = channel_id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
