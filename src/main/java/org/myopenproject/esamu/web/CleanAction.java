package org.myopenproject.esamu.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myopenproject.esamu.data.dao.EmergencyDao;

public class CleanAction implements Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try (EmergencyDao eDao = new EmergencyDao()) {
			eDao.clean();
			res.sendRedirect(req.getAttribute("root") + "/emergency");
		}
	}

}
