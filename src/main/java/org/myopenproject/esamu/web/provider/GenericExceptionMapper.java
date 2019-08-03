package org.myopenproject.esamu.web.provider;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.myopenproject.esamu.web.dto.ResponseDto;
import org.myopenproject.esamu.web.util.ResponseUtil;

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
			
			// Get stack trace as string
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			
			// Add stack trace to response
			Map<String, String> details = new HashMap<>();
			details.put("cause", exception.getMessage());
			details.put("stack_trace", sw.toString());
			resp.setDetails(details);
			
			LOG.log(Level.SEVERE, exception.getMessage(), exception);
		}
		
		return ResponseUtil.wrap(resp);
	}

}
