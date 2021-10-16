package com.viettel.parking.camera.data.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.viettel.parking.camera.data.dto.Site;

public class AccountVMS {

	@Id
	private ObjectId _id;
	// login
	private String username;
	private String keypwd;
	// user info
	public long userId;
	public String fullname;
	public String address;
	public String email;
	public long roleAdmin;
	public String token;
	public String tokenApi;
	public Site site;

	public String getTokenApi() {
		return tokenApi;
	}

	public void setTokenApi(String tokenApi) {
		this.tokenApi = tokenApi;
	}

	public String get_id() {
		return _id.toHexString();
	}

	public ObjectId getObjId() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKeypwd() {
		return keypwd;
	}

	public void setKeypwd(String keypwd) {
		this.keypwd = keypwd;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public long getRoleAdmin() {
		return roleAdmin;
	}

	public void setRoleAdmin(long roleAdmin) {
		this.roleAdmin = roleAdmin;
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

}
