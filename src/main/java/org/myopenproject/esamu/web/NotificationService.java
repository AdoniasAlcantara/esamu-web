package org.myopenproject.esamu.web;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.myopenproject.esamu.common.NotificationDto;
import org.myopenproject.esamu.data.Emergency;
import org.myopenproject.esamu.web.service.provider.JsonMessageBodyHandler;

public class NotificationService {
	private static final Logger LOG = Logger.getLogger(NotificationService.class.getName());
	private static String url;
	private static String apiKey;
	private WebTarget target;
	private Emergency emergency;	
	
	public NotificationService(Emergency emergency) {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		client.register(JsonMessageBodyHandler.class);
		
		target = client.target(url);
		this.emergency = emergency;
	}
	
	public void notifyAccepted(Map<String, Object> attachment) {
		NotificationDto notification = new NotificationDto();
		notification.setAppId(apiKey);
		notification.setUserId(new String[] {emergency.getUser().getNotificationKey()});
		notification.setData(attachment);
		HashMap<String, String> message = new HashMap<>();
		message.put("en", "O resgate está a caminho. Chegaremos em breve.");
		notification.setMessage(message);
		
		Response response = target.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(notification, MediaType.APPLICATION_JSON), Response.class);
		
		LOG.info("Response for Notification ID " + emergency.getUser().getNotificationKey() 
				+ ": " + response.getStatus() + "; " + response.readEntity(String.class));
	}
	
	public void notifyCanceled() {
		
	}
	
	public void notifyFinished() {
		
	}
	
	public static void setUrl(String url) {
		NotificationService.url = url;
	}
	
	public static void setApiKey(String apiKey) {
		NotificationService.apiKey = apiKey;
	}
}
