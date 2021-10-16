package com.viettel.parking.camera.data.dto;

public class Site {

	public long site_id;
	public String site_name;
	public String site_address;
	public String site_image;

	public long getSite_id() {
		return site_id;
	}

	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getSite_address() {
		return site_address;
	}

	public void setSite_address(String site_address) {
		this.site_address = site_address;
	}

	public String getSite_image() {
		return site_image;
	}

	public void setSite_image(String site_image) {
		this.site_image = site_image;
	}

}
