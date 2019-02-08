package org.myopenproject.esamu.web;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.data.Emergency.Status;
import org.myopenproject.esamu.data.dao.EmergencyDao;

public class IndexAction implements Action {
	private static final Logger LOG = Logger.getLogger(IndexAction.class.getName());
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String activeParam = req.getParameter("active");
		boolean active = true;
		
		if (activeParam != null) {
			try {
				active = Boolean.parseBoolean(activeParam);
			} catch (NumberFormatException e) {
				LOG.warning("Active parameter ignored. Set active to " + active + " (default)");
			}
		}
		
		try (EmergencyDao eDao = new EmergencyDao()) {
			List<Emergency> list1 = null;
			List<Emergency> list2 = null;
			String color1;
			String status1;
			String color2;
			String status2;
			
			if (active) {
				list1 = eDao.summary(Status.PENDENT);
				list2 = eDao.summary(Status.PROGRESS);
				color1 = "text-warning";
				status1= "PENDENTE";
				color2 = "text-primary";
				status2 = "PROGRESSO";
			} else {
				list1 = eDao.summary(Status.FINISHED);
				list2 = eDao.summary(Status.CANCELED);
				color1 = "text-success";
				status1= "CONCLUÍDO";
				color2 = "text-muted";
				status2 = "CANCELADO";
			}
			
			req.setAttribute("list1", list1);
			req.setAttribute("list2", list2);	
			req.setAttribute("color1", color1);
			req.setAttribute("color2", color2);
			req.setAttribute("status1", status1);
			req.setAttribute("status2", status2);
			req.setAttribute("active", active);
			
			req.getRequestDispatcher(req.getAttribute("pages") + "index.jsp").forward(req, res);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			LOG.log(Level.SEVERE, "Internal error", e);
		}
	}
}
