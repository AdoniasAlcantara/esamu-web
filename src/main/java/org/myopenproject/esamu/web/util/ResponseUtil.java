package org.myopenproject.esamu.web.util;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.myopenproject.esamu.web.dto.ResponseDto;

public class ResponseUtil {
	private ResponseUtil() {}
	
	public static Response wrap(ResponseDto resp) {
		return Response.status(resp.getStatusCode())
				.entity(resp)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
