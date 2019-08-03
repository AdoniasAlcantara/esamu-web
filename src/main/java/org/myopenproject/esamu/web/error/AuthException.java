package org.myopenproject.esamu.web.error;

public class AuthException extends ApplicationException {
	private static final long serialVersionUID = 1L;
	
	public AuthException(String message) {
		super(401, message, null);
	}
}
