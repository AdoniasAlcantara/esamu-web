package org.myopenproject.esamu.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.data.Emergency.Status;
import org.myopenproject.esamu.data.dao.EmergencyDao;

public class UpdateAction implements Action {	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long id;
		String statusParam = req.getParameter("status");
		
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty id parameter");
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
				eDao.save(emergency);
				eDao.close();
				
				switch (status) {
				case PROGRESS:
					new NotificationService(emergency).notifyAccepted(null); 
					break;
					
				case FINISHED:
					// TODO Notify user that the emergency has finished
					break;
					
				case CANCELED:
					// TODO Notify that the emergency has been aborted
				}
				
				//res.sendRedirect(req.getAttribute("root") + "/emergency");
			} else {
				res.sendError(HttpServletResponse.SC_NOT_FOUND, "Emergency not found: ID " + id);
			}
		}
	}
}
