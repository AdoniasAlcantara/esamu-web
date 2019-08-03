package org.myopenproject.esamu.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.google.gson.annotations.SerializedName;

public class EmergencyDto {
	@SerializedName("user_id")
	@NotEmpty
	private String userId;

	@NotNull
	private String imei;

	private Double latitude;
	private Double longitude;

	@NotEmpty
	private String picture;

	private String video;
	private String voice;

	public String getUserId() {
		return userId;
	}

	public String getImei() {
		return imei;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public String getPicture() {
		return picture;
	}

	public String getVideo() {
		return video;
	}

	public String getVoice() {
		return voice;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
}
