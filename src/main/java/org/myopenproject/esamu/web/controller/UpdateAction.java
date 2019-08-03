package org.myopenproject.esamu.web.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;
import org.myopenproject.esamu.data.model.Emergency;
import org.myopenproject.esamu.data.model.Emergency.Status;

public class UpdateAction implements Action {
	private static final String TEMPL_PROGRESS = "4478b4e1-9b9c-46d3-8023-7340e127ee07";
	private static final String TEMPL_FINISHED = "f862154d-9422-45c1-8067-2410aa2bda99";
	private static final String TEMPL_CANCELED = "cac50ba2-da50-4c59-9d88-bae89f6b4781";
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long id;
		int attachment;
		String statusParam = req.getParameter("status");
		
		// Validate parameters
		
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty \"id\" parameter");
			return;
		}
		
		try {
			attachment = Integer.parseInt(req.getParameter("attach"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty attach parameter");
			return;
		}
		
		if (statusParam == null) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Status cannot be empty");
			return;
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		EmergencyDao emergencyDao = new EmergencyDao(em);		
		Emergency emergency = emergencyDao.find(id);
		
		if (emergency != null) {
			em.getTransaction().begin();
			
			Status status = Status.valueOf(statusParam.toUpperCase());
			emergency.setStatus(status);
			
			if (attachment >= 0) {
				emergency.setAttachment(attachment);			
			}
			
			em.getTransaction().commit();
			NotificationService service = new NotificationService(emergency);
			
			switch (status) {
				case PROGRESS:
					service.notifyWithTemplate(TEMPL_PROGRESS); 
					break;
				
				case FINISHED:
					service.notifyWithTemplate(TEMPL_FINISHED);
					break;
					
				case CANCELED:
					service.notifyWithTemplate(TEMPL_CANCELED);
			}
			
			res.sendRedirect(req.getAttribute("root") + "/emergency");
		} else {
			res.sendError(HttpServletResponse.SC_NOT_FOUND, "Emergency not found: ID " + id);
		}
		
		em.close();
	}
}
