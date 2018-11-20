package org.myopenproject.esamu.web.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.myopenproject.esamu.common.EmergencyDto;
import org.myopenproject.esamu.common.ResponseDto;
import org.myopenproject.esamu.common.UserDto;
import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.UserDao;
import org.myopenproject.esamu.web.service.provider.JsonMessageBodyHandler;

import com.google.api.client.util.Base64;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmergencyResourceTest {
	static WebTarget target;
	static long emergencyId;
	static final String userId = "bYmjnWsB4FQvnG43f8TAH3K1z0q2";
	
	@BeforeClass
	public static void init() {
		// Creating request
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		client.register(JsonMessageBodyHandler.class);
		target = client.target("http://localhost:8080/esamu-web/").path("/service/users");
	}
	
	@AfterClass
	public static void cleanUp() {
		EmergencyDao.setResourcesPath("/home/adonias/Workspace/java/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/esamu-web/img");
		
		try (UserDao uDao = new UserDao();
			 EmergencyDao eDao = new EmergencyDao())
		{
			eDao.remove(emergencyId);
			uDao.remove(userId);
		}
	}
	
	@Test
	public void test1SignUpSuccess() {		
		UserDto user = new UserDto();
		user.setId(userId); // Firebase test user
		user.setName("Username");
		user.setPhone("+5512345678900");
		user.setNotificationKey("b128e848-25a2-4f82-9bf4-ac8c63e4882d"); // OneSignal test user
		
		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
		ResponseDto responseDto = response.readEntity(ResponseDto.class); 
		
		assertEquals(responseDto.toString(), Response.Status.CREATED.getStatusCode(), response.getStatus());
	}
	
	@Test
	public void test2SignUpFail() {
		UserDto user = new UserDto();
		user.setId("3EeyQIDa9FNcbGv1kYjS44ErOlz2xxxxxxxx");
		user.setName("");
		user.setNotificationKey("9998888777666555444333222111000");
		
		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON), Response.class);
		assertEquals(422, response.getStatus());
		
		ResponseDto responseDto = response.readEntity(ResponseDto.class);
		assertNotNull(responseDto.toString(), responseDto);
	}
	
	@Test
	public void test3ReportNewEmergency() throws IOException {
		Path path = Paths.get(getClass().getClassLoader().getResource("picture.jpg").getFile());
		byte[] picture = Files.readAllBytes(path);
		
		EmergencyDto emergencyDto = new EmergencyDto();
		emergencyDto.setUserId(userId);
		emergencyDto.setLatitude("0");
		emergencyDto.setLongitude("0");
		emergencyDto.setImei("123456789012345");
		emergencyDto.setAddress("Address");
		emergencyDto.setCity("City");
		emergencyDto.setState("State");
		emergencyDto.setCountry("Country");
		emergencyDto.setPostalCode("12345678");
		emergencyDto.setPicture(Base64.encodeBase64String(picture));
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		client.register(JsonMessageBodyHandler.class);
		WebTarget target = client.target("http://localhost:8080/esamu-web/service/").path("/emergencies");
		Response response = target.request(MediaType.APPLICATION_JSON)
			.post(Entity.entity(emergencyDto, MediaType.APPLICATION_JSON), Response.class);
		ResponseDto rDto = response.readEntity(ResponseDto.class);
		
		assertEquals(rDto.toString(), Response.Status.CREATED.getStatusCode(), response.getStatus());
		emergencyId = Long.parseLong((String) rDto.getDetails().get("key"));
	}
}
