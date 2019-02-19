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
	
	public void notifyMessage(String title, String msg) {
		NotificationDto notification = prepare();
		
		if (title != null) {
			HashMap<String, String> titleMap = new HashMap<>();
			titleMap.put("en", title);
			notification.setTitle(titleMap);
		}
		
		HashMap<String, String> msgMap = new HashMap<>();
		msgMap.put("en", msg);
		notification.setMessage(msgMap);
		
		push(notification);
	}
	
	public void notifyWithTemplate(String template) {
		NotificationDto notification = prepare();
		notification.setTemplate(template);
		push(notification);
	}
	
	public static void setUrl(String url) {
		NotificationService.url = url;
	}
	
	public static void setApiKey(String apiKey) {
		NotificationService.apiKey = apiKey;
	}
	
	private NotificationDto prepare() {
		NotificationDto notification = new NotificationDto();
		notification.setAppId(apiKey);
		notification.setUserId(new String[] {emergency.getUser().getNotificationKey()});
		
		HashMap<String, String> data = new HashMap<>();
		data.put("status", Integer.toString(emergency.getStatus().ordinal()));
		data.put("emergency_id", Long.toString(emergency.getId()));
		
		if (emergency.getAttachment() >= 0) {
			data.put("attach", Integer.toString(emergency.getAttachment()));
			HashMap<String, String> action = new HashMap<>();
			action.put("id", "1");
			action.put("text", "Ver primeiros socorros");
			@SuppressWarnings("unchecked")
			Map<String, String>[] array = (Map<String, String>[]) new Map[1];
			array[0] = action;
			notification.setAction(array);
		}
		
		notification.setData(data);		
		return notification;
	}
	
	private void push(NotificationDto notification) {
		try {
			Response response = target.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(notification, MediaType.APPLICATION_JSON), Response.class);
			
			LOG.info("Response: notification ID " + emergency.getUser().getNotificationKey() 
				+ ": " + response.getStatus() + "; " + response.readEntity(String.class));
		} catch (RuntimeException e) {
			LOG.warning("Failed to send notification. Notification ID " + emergency.getUser().getNotificationKey());
		}
	}
}
