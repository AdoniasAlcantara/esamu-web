package org.myopenproject.esamu.web.dto;

import java.util.HashMap;
import java.util.Map;

public class ResponseDto {
	private int statusCode;
	private String description;
	private Map<String, String> details = new HashMap<>();

	public int getStatusCode() {
		return statusCode;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getDetails() {
		return details;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDetails(Map<String, String> details) {
		this.details = details;
	}
}
