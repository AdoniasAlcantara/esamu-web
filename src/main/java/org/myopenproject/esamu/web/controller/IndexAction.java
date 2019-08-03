package org.myopenproject.esamu.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;
import org.myopenproject.esamu.data.model.Emergency;
import org.myopenproject.esamu.data.model.Emergency.Status;

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

		EntityManager em = JpaUtil.getEntityManager();
		EmergencyDao eDao = new EmergencyDao(em);
		List<Emergency> list1 = null;
		List<Emergency> list2 = null;
		String color1;
		String color2;
		String status1;
		String status2;

		if (active) {
			List<Emergency> all = eDao.summary(Status.PENDENT, Status.PROGRESS);	
			
			list1 = all.stream()
					.filter(e -> e.getStatus() == Status.PENDENT)
					.collect(Collectors.toList());
			
			list2 = all.stream()
					.filter(e -> e.getStatus() == Status.PROGRESS)
					.collect(Collectors.toList());
			
			status1 = "PENDENTE";
			status2 = "PROGRESSO";
			color1 = "text-warning";
			color2 = "text-primary";
		} else {
			List<Emergency> all = eDao.summary(Status.FINISHED, Status.CANCELED);
			
			list1 = all.stream()
					.filter(e -> e.getStatus() == Status.FINISHED)
					.collect(Collectors.toList());
			
			list2 = all.stream()
					.filter(e -> e.getStatus() == Status.CANCELED)
					.collect(Collectors.toList());
			
			status1 = "CONCLUÍDO";
			status2 = "CANCELADO";
			color1 = "text-success";
			color2 = "text-muted";
		}
		
		em.close();

		req.setAttribute("list1", list1);
		req.setAttribute("list2", list2);
		req.setAttribute("color1", color1);
		req.setAttribute("color2", color2);
		req.setAttribute("status1", status1);
		req.setAttribute("status2", status2);
		req.setAttribute("active", active);

		req.getRequestDispatcher(req.getAttribute("pages") + "index.jsp").forward(req, res);
	}
}
