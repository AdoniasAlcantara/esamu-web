package org.myopenproject.esamu.web;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmergencyServlet")
public class EmergencyServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(EmergencyServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionParam = req.getParameter("action");
		
		if (actionParam == null)
			actionParam = "index";
		
		try {
			Class<?> clazz = Class.forName(getClass().getPackage() + "." + actionParam + "Action");
			Action action = (Action) clazz.newInstance();
			action.execute(req, resp);
			LOG.fine("Request for action: " + actionParam);
		} catch (ReflectiveOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Action \"" + actionParam + "\" not found.");
			LOG.warning("Nonexistent action: " + actionParam);
		}
	}
}
