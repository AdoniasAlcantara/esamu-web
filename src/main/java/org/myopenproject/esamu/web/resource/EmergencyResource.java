package org.myopenproject.esamu.web.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.myopenproject.esamu.web.dto.EmergencyDto;
import org.myopenproject.esamu.web.dto.ResponseDto;
import org.myopenproject.esamu.web.service.EmergencyService;

@Path("/emergencies")
public class EmergencyResource {
	private EmergencyService service = new EmergencyService();

	@Context
	private UriInfo info;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportNewEmergency(EmergencyDto emergencyDto) {
		ResponseDto responseDto = service.reportNewEmergency(emergencyDto);
		String id = responseDto.getDetails().get("key");
		URI uri;
		
		try {
			uri = new URI(info.getAbsolutePath().toString() + "/" + id);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		return Response.created(uri).entity(responseDto).build();
	}

	@GET
	@Path("/last")
	public long last() {
		return service.getLastEmergencyTimeMillis();
	}
}
