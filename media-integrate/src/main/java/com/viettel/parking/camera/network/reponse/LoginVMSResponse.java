package com.viettel.parking.camera.network.reponse;

import com.viettel.parking.camera.data.dto.Site;

public class LoginVMSResponse {

	private boolean success;
	private String message;
	private long user_id;
	private String fullname;
	private String address;
	private String email;
	private long role_admin;
	private String token;
	private Site site;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getRole_admin() {
		return role_admin;
	}

	public void setRole_admin(long role_admin) {
		this.role_admin = role_admin;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
