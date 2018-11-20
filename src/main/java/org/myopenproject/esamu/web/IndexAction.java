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
		try (EmergencyDao eDao = new EmergencyDao()) {
			List<Emergency> pendent = eDao.findByStatus(Status.PENDENT);
			List<Emergency> progress = eDao.findByStatus(Status.PROGRESS);
			List<Emergency> finished = eDao.findByStatus(Status.FINISHED);
			List<Emergency> canceled = eDao.findByStatus(Status.CANCELED);
			req.setAttribute("pendent", pendent);
			req.setAttribute("progress", progress);
			req.setAttribute("finished", finished);
			req.setAttribute("canceled", canceled);
			req.getRequestDispatcher("index.jsp").forward(req, res);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			LOG.log(Level.SEVERE, "Internal error", e);
		}
	}
}
