package org.myopenproject.esamu.web.config;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;

public class EsamuApplication extends Application {
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("jersey.config.server.provider.packages",
				"org.myopenproject.esamu.web.config," +
				"org.myopenproject.esamu.web.provider," +
				"org.myopenproject.esamu.web.resource");
		
		return properties;
	}
}
