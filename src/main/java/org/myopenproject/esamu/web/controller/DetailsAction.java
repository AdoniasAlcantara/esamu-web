package org.myopenproject.esamu.web.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;
import org.myopenproject.esamu.data.model.Emergency;

public class DetailsAction implements Action {	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		long id;
		
		try {
			id = Long.parseLong(req.getParameter("id"));
		} catch (NumberFormatException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty \"id\" parameter");
			return;
		}
		
		EntityManager em = JpaUtil.getEntityManager();
		EmergencyDao emergencyDao = new EmergencyDao(em);
		Emergency emergency = emergencyDao.find(id);
		em.close();
		
		if (emergency != null) {
			req.setAttribute("emergency", emergency);
			req.getRequestDispatcher(req.getAttribute("pages") + "details.jsp").forward(req, res);
		} else {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
