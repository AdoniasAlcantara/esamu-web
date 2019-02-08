package org.myopenproject.esamu.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.data.dao.EmergencyDao;

public class DetailsAction implements Action {
	private static final Logger LOG = Logger.getLogger(DetailsAction.class.getName());
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long id;
		
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			LOG.warning("Invalid or empty id parameter");
			return;
		}
		
		try (EmergencyDao eDao = new EmergencyDao()) {
			Emergency emergency = eDao.find(id);
		
			if (emergency != null) {
				req.setAttribute("emergency", emergency);
				req.getRequestDispatcher(req.getAttribute("pages") + "details.jsp").forward(req, res);
			} else {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			LOG.log(Level.SEVERE, "Internal error", e);
		}
	}
}
