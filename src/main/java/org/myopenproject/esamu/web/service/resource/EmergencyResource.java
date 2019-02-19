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
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

@Path("/")
public class EmergencyResource {
	private static final Logger LOG = Logger.getLogger(EmergencyResource.class.getName());
	private static long lastEmergency = System.currentTimeMillis();

	@Context
	private UriInfo info;

	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signIn(UserDto uDto) {
		LOG.fine("Sign in request. Id " + uDto.getId());

		// Convert DTO to user entity
		User user = new User();
		user.setId(uDto.getId());
		user.setPhone(uDto.getPhone());
		user.setName(uDto.getName());
		user.setNotificationKey(uDto.getNotificationKey());

		// Validation
		Set<ConstraintViolation<User>> violations = Validator.validate(user);

		if (violations != null)
			throw new InvalidEntityException(violations);

		// Check user supplied key against the Firebase user key
		try {
			UserRecord ur = FirebaseAuth.getInstance().getUser(user.getId().toString());

			if (ur.isDisabled())
				throw new AuthException("User is disabled. ID " + uDto.getId());

			// Save/update user
			try (UserDao uDao = new UserDao()) {
				uDao.save(user);
				LOG.info("Save user. Id " + user.getId());
			}
		} catch (FirebaseAuthException e) {
			throw new AuthException("User must authenticate the phone number before signing up. ID " + uDto.getId());
		}

		// Send response
		ResponseDto resp = new ResponseDto();
		resp.setStatusCode(Response.Status.CREATED.getStatusCode());
		resp.setDescription("User added successfully. Id " + uDto.getId());
		return ResponseUtil.wrap(resp);
	}

	@POST
	@Path("/emergencies")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response reportNewEmergency(EmergencyDto eDto) {
		User user;

		// Check user authorization
		try (UserDao uDao = new UserDao()) {
			user = uDao.find(eDto.getUserId());
		}

		if (user == null)
			throw new AuthException("Unauthorized user. Id " + eDto.getUserId());

		// Convert DTO to emergency entity
		Emergency emergency = new Emergency();
		emergency.setUser(user);
		emergency.setImei(eDto.getImei());
		emergency.setStart(new Date());
		emergency.setStatus(Status.PENDENT);

		double lat = Double.parseDouble(eDto.getLatitude());
		double lng = Double.parseDouble(eDto.getLongitude());
		Location location = fetchLocation(lat, lng);
		
		if (location != null)
			emergency.setLocation(location);

		Multimedia multimedia = new Multimedia();
		multimedia.setPicture(Base64.decodeBase64(eDto.getPicture()));
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

		// Send response
		ResponseDto resp = new ResponseDto();
		resp.setStatusCode(Response.Status.CREATED.getStatusCode());
		resp.setDescription("Emergency has been reported " + emergency.getId());
		resp.addDetail("resource", info.getAbsolutePath().toString() + "/" + emergency.getId());
		resp.addDetail("key", Long.toString(emergency.getId()));
		resp.addDetail("timestamp", Long.toString(emergency.getStart().getTime()));
		return ResponseUtil.wrap(resp);
	}

	@GET
	@Path("/last")
	public String last() {
		return Long.toString(lastEmergency);
	}

	@SuppressWarnings("incomplete-switch")
	private Location fetchLocation(double lat, double lng) {
		if (lat == 0 && lng == 0)
			return null;

		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyAjvfhxOax1hHlNFcjFl_PabqQJsz-TwCw").build();
		LatLng latLng = new LatLng(lat, lng);
		Location location = null;

		try {
			GeocodingResult[] result = GeocodingApi.reverseGeocode(context, latLng).await();
			location = new Location();
			location.setLatitude(lat);
			location.setLongitude(lng);
			location.setAddress(result[0].formattedAddress);

			for (AddressComponent addr : result[0].addressComponents) {
				for (AddressComponentType type : addr.types) {
					switch (type) {
					case ADMINISTRATIVE_AREA_LEVEL_1:
						location.setCity(addr.longName);
						break;

					case ADMINISTRATIVE_AREA_LEVEL_2:
						location.setState(addr.longName);
						break;

					case COUNTRY:
						location.setCountry(addr.longName);
						break;

					case POSTAL_CODE:
						location.setPostalCode(addr.longName.replace("-", ""));
					}
				}
			}

		} catch (Throwable e) {
			new RuntimeException(e); // What a Terrible Failure :(
		}

		return location;
	}

	private synchronized static void refreshLastEmergency() {
		lastEmergency = System.currentTimeMillis();
	}
}
