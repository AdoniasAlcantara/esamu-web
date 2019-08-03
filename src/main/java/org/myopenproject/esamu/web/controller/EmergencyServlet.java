package org.myopenproject.esamu.web.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/emergency/*")
public class EmergencyServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(EmergencyServlet.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		NotificationService.setUrl(getServletContext().getInitParameter("onesignal-url"));
		NotificationService.setApiKey(getServletContext().getInitParameter("onesignal-key"));
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String actionParam = req.getParameter("action");
		
		if (actionParam == null) {
			actionParam = "index";			
		}
		
		try {
			req.setAttribute("root", getServletContext().getContextPath());
			req.setAttribute("pages", "/WEB-INF/jsp/");
			getActionInstance(actionParam).execute(req, resp);
			LOG.info("Request for action: " + actionParam);
		} catch (ReflectiveOperationException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Action \"" + actionParam + "\" not found.");
			LOG.warning("Nonexistent action: " + actionParam);
		}
	}
	
	private Action getActionInstance(String actionParam) throws ReflectiveOperationException {
		String packageName = getClass().getPackage().getName(); 
		String className = actionParam.substring(0, 1).toUpperCase() + actionParam.substring(1) + "Action";
		return (Action) Class.forName(packageName + "." + className).newInstance();
	}
}
