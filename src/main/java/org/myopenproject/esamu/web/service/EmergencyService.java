package org.myopenproject.esamu.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;
import org.myopenproject.esamu.data.dao.UserDao;
import org.myopenproject.esamu.data.dao.Validator;
import org.myopenproject.esamu.data.model.Emergency;
import org.myopenproject.esamu.data.model.Emergency.Status;
import org.myopenproject.esamu.data.model.Location;
import org.myopenproject.esamu.data.model.Multimedia;
import org.myopenproject.esamu.web.dto.EmergencyDto;
import org.myopenproject.esamu.web.dto.ResponseDto;
import org.myopenproject.esamu.web.error.AuthException;
import org.myopenproject.esamu.web.error.InvalidEntityException;
import org.myopenproject.esamu.web.util.Geocoder;

import com.google.api.client.util.Base64;

public class EmergencyService {
	private static long lastEmergency = System.currentTimeMillis();
	
	private EntityManager em;
	private EmergencyDao emergencyDao;
	private UserDao userDao;

	public EmergencyService() {
		em = JpaUtil.getEntityManager();
		emergencyDao = new EmergencyDao(em);
		userDao = new UserDao(em);
	}

	public ResponseDto reportNewEmergency(EmergencyDto emergencyDto) {
		// Check if user already exists
		if (userDao.find(emergencyDto.getUserId()) == null) {
			throw new AuthException("Unauthorized user. Id " + emergencyDto.getUserId());
		}
		
		Emergency emergency = dtoToEntity(emergencyDto);
		
		// Save
		em.getTransaction().begin();
		emergencyDao.save(emergency);
		em.getTransaction().commit();
		em.close();

		updateLastEmergencyTimeMillis();
		return createResponse(emergency);
	}
	
	public long getLastEmergencyTimeMillis() {
		return lastEmergency;
	}

	private Emergency dtoToEntity(EmergencyDto dto) {
		// Validate
		Set<ConstraintViolation<EmergencyDto>> violations = Validator.validate(dto);

		if (violations != null && !violations.isEmpty()) {
			throw new InvalidEntityException(violations);
		}
		
		Emergency emergency = new Emergency();
		emergency.setUser(userDao.find(dto.getUserId()));
		emergency.setImei(dto.getImei());
		emergency.setStart(new Date());
		emergency.setStatus(Status.PENDENT);
		
		if (dto.getLatitude() != null && dto.getLongitude() != null) {
			Geocoder geocoder = new Geocoder();
			Location location = geocoder.fetchLocation(dto.getLatitude(), dto.getLongitude());
			
			if (location != null) {
				emergency.setLocation(location);	
				location.setEmergency(emergency);
			}			
		}
		
		Multimedia multimedia = new Multimedia();
		multimedia.setPicture(Base64.decodeBase64(dto.getPicture()));
		emergency.setMultimedia(multimedia);
		
		return emergency;
	}
	
	private ResponseDto createResponse(Emergency emergency) {
		ResponseDto response = new ResponseDto();
		response.setDescription("Emergency created");
		Map<String, String> details = new HashMap<>();
		details.put("key", Long.toString(emergency.getId()));
		details.put("location", emergency.getLocation().getAddress());
		details.put("timestamp", Long.toString(emergency.getStart().getTime()));
		details.put("status", Status.PENDENT.name());
		response.setDetails(details);
		
		return response;
	}
	
	private synchronized static void updateLastEmergencyTimeMillis() {
		lastEmergency = System.currentTimeMillis();
	}
}
