package org.myopenproject.esamu.web.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class InvalidEntityException extends ApplicationException {
	private static final long serialVersionUID = -5603230100889468974L;
	private Map<String, String> details;
	
	public InvalidEntityException(String message) {
		this(message, null);
	}
	
	public InvalidEntityException(String message, Map<String, String> details) {
		super(422, message, null);
		this.details = details;
	}
	
	public InvalidEntityException(Set<? extends ConstraintViolation<?>> violations) {
		super(422, "Entity doesn't meet constraints", null);
		details = new HashMap<>();
		
		for (ConstraintViolation<?> cv : violations)
			details.put(cv.getPropertyPath().toString(), cv.getMessage());
	}
	
	@Override
	public Map<String, String> getDetails() {		
		return details;
	}
}
