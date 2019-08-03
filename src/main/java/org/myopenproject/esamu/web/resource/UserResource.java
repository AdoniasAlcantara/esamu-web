package org.myopenproject.esamu.web.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.myopenproject.esamu.web.dto.UserDto;
import org.myopenproject.esamu.web.service.UserService;

@Path("/users")
public class UserResource {
	private UserService service = new UserService();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response signIn(UserDto userDto) {
		service.signIn(userDto);
		return Response.accepted().build();
	}
}
