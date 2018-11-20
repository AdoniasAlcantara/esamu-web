package org.myopenproject.esamu.web.service.provider;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myopenproject.esamu.common.ResponseDto;
import org.myopenproject.esamu.web.error.ApplicationException;
import org.myopenproject.esamu.web.service.ResponseUtil;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {
	private static final Logger LOG = Logger.getLogger(ApplicationExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(ApplicationException exception) {
		ResponseDto resp = new ResponseDto();
		resp.setStatusCode(exception.getStatusCode());
		resp.setDescription(exception.getMessage());
		resp.setDetails(exception.getDetails());
		
		Map<String, Object> details = exception.getDetails();
		LOG.log(Level.WARNING, exception.getMessage() + (details != null ? ": " + details : ""));
		
		return ResponseUtil.wrap(resp);
	}
}
