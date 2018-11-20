package org.myopenproject.esamu.web.error;

import java.util.Map;

public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = -2517627432942500640L;
	private int code;
	
	public ApplicationException(int code, String message, Throwable exception) {
		super(message, exception);
		this.code = code;
	}
	
	public int getStatusCode() {
		return code;
	}
	
	public Map<String, Object> getDetails() {
		return null;
	}
}
