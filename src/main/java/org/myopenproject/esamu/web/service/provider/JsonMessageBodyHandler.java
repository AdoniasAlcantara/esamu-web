package org.myopenproject.esamu.web.service.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Provider
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class JsonMessageBodyHandler implements MessageBodyWriter<Object>, MessageBodyReader<Object> {
	private static final Logger LOG = Logger.getLogger(JsonMessageBodyHandler.class.getName());
	private static final String UTF_8 = "UTF-8";
	private Gson gson;

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream) 
	{
		Object obj = null;
		
		try (InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8)) {			
			Type jsonType;
			
			if (type.equals(genericType))
				jsonType = type;
			else
				jsonType = genericType;
			
			obj = getGson().fromJson(streamReader, jsonType);
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Cannot read object.", e);
		}
		
		return obj;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	@Override
	public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, 
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) 
			throws IOException, WebApplicationException 
	{
		try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8)) {
			Type jsonType;
			
			if (type.equals(genericType))
				jsonType = type;
			else
				jsonType = genericType;
			
			getGson().toJson(object, jsonType, writer);
		}catch (IOException e) {
			LOG.log(Level.WARNING, "Cannot write object.", e);
		}
	}
	
	private Gson getGson() {
		if (gson == null) {
			final GsonBuilder gsonBuilder = new GsonBuilder();
			gson = gsonBuilder.disableHtmlEscaping()
					.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
					.setPrettyPrinting()
					.serializeNulls()
					.create();
		}
		
		return gson;
	}
}
