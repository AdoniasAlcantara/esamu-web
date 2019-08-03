package org.myopenproject.esamu.web.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;

public class CleanAction implements Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		EntityManager em = JpaUtil.getEntityManager();
		EmergencyDao emergencyDao = new EmergencyDao(em);
		
		try {
			em.getTransaction().begin();
			emergencyDao.clean();
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			em.getTransaction().rollback();
		}
		
		res.sendRedirect(req.getAttribute("root") + "/emergency");
	}
}
