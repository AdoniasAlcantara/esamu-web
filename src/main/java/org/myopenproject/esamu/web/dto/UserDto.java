package org.myopenproject.esamu.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserDto {
	@NotEmpty
	private String id;
	
	@NotEmpty
	@Pattern(regexp = "\\+55\\d{9,11}")
	private String phone;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String notificationKey;

	public String getId() {
		return id;
	}

	public String getPhone() {
		return phone;
	}

	public String getName() {
		return name;
	}

	public String getNotificationKey() {
		return notificationKey;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNotificationKey(String notificationKey) {
		this.notificationKey = notificationKey;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("UserDto [")
				.append("id=").append(id)
				.append(", phone=").append(phone)
				.append(", name=").append(name)
				.append(", notificationKey=")
				.append(notificationKey)
				.append("]");
		
		return builder.toString();
	}
}
