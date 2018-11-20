package org.myopenproject.esamu.web.service.provider;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myopenproject.esamu.common.ResponseDto;
import org.myopenproject.esamu.web.service.ResponseUtil;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(Throwable exception) {
		ResponseDto resp = new ResponseDto();
		
		if (exception instanceof WebApplicationException) {
			resp.setStatusCode(((WebApplicationException) exception).getResponse().getStatus());
			resp.setDescription(exception.getMessage());
		} else {
			resp.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
			resp.setDescription("An internal server error occurred");
			LOG.log(Level.SEVERE, exception.getMessage(), exception);
		}
		
		return ResponseUtil.wrap(resp);
	}

}
