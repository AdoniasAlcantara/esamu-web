package org.myopenproject.esamu.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.data.Emergency.Status;
import org.myopenproject.esamu.data.dao.EmergencyDao;

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
		
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty id parameter");
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
		
		try (EmergencyDao eDao = new EmergencyDao()) {
			Emergency emergency = eDao.find(id);
			
			if (emergency != null) {
				Status status = Status.valueOf(statusParam.toUpperCase());
				emergency.setStatus(status);
				
				if (attachment >= 0)
					emergency.setAttachment(attachment);
				
				eDao.save(emergency);
				eDao.close();
				
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
		}
	}
}
