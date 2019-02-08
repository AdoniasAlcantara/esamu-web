package org.myopenproject.esamu.web.service.resource;

import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.myopenproject.esamu.common.EmergencyDto;
import org.myopenproject.esamu.common.ResponseDto;
import org.myopenproject.esamu.common.UserDto;
import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.data.Emergency.Status;
import org.myopenproject.esamu.data.Location;
import org.myopenproject.esamu.data.Multimedia;
import org.myopenproject.esamu.data.User;
import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.UserDao;
import org.myopenproject.esamu.data.dao.Validator;
import org.myopenproject.esamu.web.error.AuthException;
import org.myopenproject.esamu.web.error.InvalidEntityException;
import org.myopenproject.esamu.web.service.ResponseUtil;

import com.google.api.client.util.Base64;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

@Path("/")
public class EmergencyResource {
	private static final Logger LOG = Logger.getLogger(EmergencyResource.class.getName());
	private static long lastEmergency = System.currentTimeMillis();
	
	@Context
	private UriInfo info;
	
	@POST @Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(UserDto  userDto) {
		LOG.fine("Sign in request. Id " + userDto.getId());
		
		// Convert DTO to user entity
		User user = new User();
		user.setId(userDto.getId());
		user.setPhone(userDto.getPhone());
		user.setName(userDto.getName());
		user.setNotificationKey(userDto.getNotificationKey());
		
		// Validation
		Set<ConstraintViolation<User>> violations = Validator.validate(user);
		
		if (violations != null)
			throw new InvalidEntityException(violations);
		
		// Check user supplied key against the Firebase user key
		try {
			UserRecord fUser = FirebaseAuth.getInstance().getUser(user.getId().toString());
			
			if (fUser.isDisabled())
				throw new AuthException("User is disabled. ID " + userDto.getId());
			
			// Save/update user
			try (UserDao uDao = new UserDao()) {
				uDao.save(user);
				LOG.fine("User added. Id " + user.getId());
			}
		} catch (FirebaseAuthException e) {
			throw new AuthException("User must authenticate the phone number before signing up. ID " + userDto.getId());
		}
		
		// Send response		
		ResponseDto resp = new ResponseDto();
		resp.setStatusCode(Response.Status.CREATED.getStatusCode());
		resp.setDescription("User added successfully. Id " + userDto.getId());
		return ResponseUtil.wrap(resp);
	}
	
	@POST @Path("/emergencies")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportNewEmergency(EmergencyDto emergencyDto) {
		User user;
		
		// Check user authorization
		try (UserDao uDao = new UserDao()) {
			user = uDao.find(emergencyDto.getUserId());
		}
		
		if (user == null)
			throw new AuthException("Unauthorized user. Id " + emergencyDto.getUserId());
		
		// Convert DTO to emergency entity
		Emergency emergency = new Emergency();
		emergency.setUser(user);
		emergency.setImei(emergencyDto.getImei());
		emergency.setStart(new Date());
		emergency.setStatus(Status.PENDENT);
		
		Location location = new Location();
		location.setLatitude(Double.parseDouble(emergencyDto.getLatitude()));
		location.setLongitude(Double.parseDouble(emergencyDto.getLongitude()));
		location.setAddress(emergencyDto.getAddress());
		location.setCity(emergencyDto.getCity());
		location.setState(emergencyDto.getState());
		location.setCountry(emergencyDto.getCountry());
		location.setPostalCode(emergencyDto.getPostalCode());
		emergency.setLocation(location);
		
		Multimedia multimedia = new Multimedia();
		multimedia.setPicture(Base64.decodeBase64(emergencyDto.getPicture()));
		emergency.setMultimedia(multimedia);
		
		// Validate
		Set<ConstraintViolation<Emergency>> violations = Validator.validate(emergency);
		
		if (violations != null)
			throw new InvalidEntityException(violations);
		
		// Persist it
		try (EmergencyDao eDao = new EmergencyDao()) {
			eDao.save(emergency);
		}
		
		refreshLastEmergency();
		// TODO send notification to user
		
		// Response
		ResponseDto resp = new ResponseDto();
		resp.setStatusCode(Response.Status.CREATED.getStatusCode());
		resp.setDescription("Emergency has been reported " + emergency.getId());
		resp.addDetail("resource", info.getAbsolutePath().toString() + "/" + emergency.getId());
		resp.addDetail("key", Long.toString(emergency.getId()));
		resp.addDetail("timestamp", Long.toString(emergency.getStart().getTime()));
		return ResponseUtil.wrap(resp);
	}
	
	@GET @Path("/last")
	public String last() {
		return Long.toString(lastEmergency);
	}
	
	private synchronized static void refreshLastEmergency() {
		lastEmergency = System.currentTimeMillis();
	}
}
