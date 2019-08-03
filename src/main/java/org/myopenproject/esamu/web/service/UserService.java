package org.myopenproject.esamu.web.service;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;

import org.jboss.logging.Logger;
import org.myopenproject.esamu.data.dao.JpaUtil;
import org.myopenproject.esamu.data.dao.UserDao;
import org.myopenproject.esamu.data.dao.Validator;
import org.myopenproject.esamu.data.model.User;
import org.myopenproject.esamu.web.dto.UserDto;
import org.myopenproject.esamu.web.error.AuthException;
import org.myopenproject.esamu.web.error.InvalidEntityException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

public class UserService {
	private static final Logger LOG = Logger.getLogger(UserService.class.getName());

	public void signIn(UserDto userDto) {
		User user = dtoToEntity(userDto);

		try {
			// Check user supplied key against the Firebase user key
			UserRecord firebaseUserRecord = FirebaseAuth
					.getInstance()
					.getUser(user.getId().toString());

			if (firebaseUserRecord.isDisabled()) {
				throw new AuthException("User is disabled. ID " + user.getId());
			}
		} catch (FirebaseAuthException e) {
			throw new AuthException("User must authenticate phone number before signing up. ID " + userDto.getId());
		}

		// Insert/update user
		EntityManager em = JpaUtil.getEntityManager();
		UserDao userDao = new UserDao(em);

		em.getTransaction().begin();
		userDao.save(user);
		em.getTransaction().commit();
		em.close();

		LOG.info("Saved user. Id " + user.getId());
	}

	private User dtoToEntity(UserDto dto) {
		// Validation
		Set<ConstraintViolation<UserDto>> violations = Validator.validate(dto);

		if (violations != null && !violations.isEmpty()) {
			throw new InvalidEntityException(violations);			
		}

		// Convert DTO to user entity
		User user = new User();
		user.setId(dto.getId());
		user.setPhone(dto.getPhone());
		user.setName(dto.getName());
		user.setNotificationKey(dto.getNotificationKey());

		return user;
	}
}
