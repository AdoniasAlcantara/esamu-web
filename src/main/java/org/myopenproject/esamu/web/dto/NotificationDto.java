package org.myopenproject.esamu.web.dto;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class NotificationDto {
	@SerializedName("app_id")
	private String appId;
	
	@SerializedName("include_player_ids")
	private String[] userId;
	
	@SerializedName("headings")
	private Map<String, String> title;
	
	@SerializedName("contents")
	private Map<String, String> message;
	
	@SerializedName("data")
	private Map<String, String> data;
	
	@SerializedName("template_id")
	private String template;
	
	@SerializedName("buttons")
	private Map<String, String>[] actions;

	public String getAppId() {
		return appId;
	}

	public String[] getUserId() {
		return userId;
	}

	public Map<String, String> getTitle() {
		return title;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public Map<String, String> getData() {
		return data;
	}

	public String getTemplate() {
		return template;
	}

	public Map<String, String>[] getActions() {
		return actions;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setUserId(String[] userId) {
		this.userId = userId;
	}

	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setActions(Map<String, String>[] actions) {
		this.actions = actions;
	}
}
