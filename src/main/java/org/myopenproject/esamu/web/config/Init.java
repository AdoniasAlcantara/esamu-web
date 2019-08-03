package org.myopenproject.esamu.web.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.myopenproject.esamu.data.dao.EmergencyDao;
import org.myopenproject.esamu.data.dao.JpaUtil;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@WebListener
public class Init implements ServletContextListener {
	private static final Logger LOG = Logger.getLogger(Init.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		// Set up resources directory
		try {
			Path resPath = Paths.get(context.getRealPath(context.getInitParameter("resources-dir")));
			EmergencyDao.setResourcesPath(resPath.toString());

			if (Files.notExists(resPath)) {
				Files.createDirectory(resPath);				
			}
			
			LOG.info("Resources path: " + resPath);
		} catch (IOException e) {
			throw new RuntimeException("Cannot set up resources directory", e);
		}

		// Initialize Firebase
		try {
			String path = context.getRealPath(context.getInitParameter("firebase-credentials"));
			FileInputStream serviceAccount = new FileInputStream(path);
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl(context.getInitParameter("firebase-url"))
					.build();
			
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Cannot load Firebase. Some features will be unavailable", e);
		}
		
		LOG.info("e-SAMU application has been started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		JpaUtil.close();
		LOG.info("e-SAMU application is shutting down");
	}
}
