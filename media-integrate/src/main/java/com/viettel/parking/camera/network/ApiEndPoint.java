package com.viettel.parking.camera.network;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ApiEndPoint {

	@Value("${system.vms.host}")
	private String vmsHost;

	@Value("${system.vms.store}")
	private String vmsStore;

	@Value("${system.vms.version}")
	private String version;

	@Bean
	public String getVmsHost() {
		return vmsHost;
	}

	@Bean
	public String getVersion() {
		return version;
	}

	@Bean
	public String getVmsStore() {
		return vmsStore;
	}

	public void setVmsStore(String vmsStore) {
		this.vmsStore = vmsStore;
	}

	public String getApiLogin() {
		return getVmsHost() + "/api/login?action=login&username=%s&pas" + "sw" + "ord=%s";
	}

	public String getApiPlayBack() {
		// "playback/mpegts/${camera_id}xyz${profile_id}_${start_time}_${duaration}.m3u8";
		return getVmsStore() + "/playback/mpegts/%sxyz%s_%s_%s.m3u8";
	}

	public String getApiValidateToken() {
		return getVmsHost() + "/api/cms_api?token=%s";
	}

	public String getApiGetCamera() {
		return getVmsHost() + "/api/cms_api/getCamera?version=%s&site_id=%s&token=%s";
	}

}
