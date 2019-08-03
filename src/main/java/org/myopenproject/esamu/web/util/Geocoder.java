package org.myopenproject.esamu.web.util;

import org.myopenproject.esamu.data.model.Location;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class Geocoder {
	private GeoApiContext context = new GeoApiContext
			.Builder()
			.apiKey("AIzaSyAjvfhxOax1hHlNFcjFl_PabqQJsz-TwCw")
			.build();
	
	@SuppressWarnings("incomplete-switch")
	public Location fetchLocation(double latitude, double longitude) {
		LatLng latLng = new LatLng(latitude, longitude);
		Location location = null;

		try {
			GeocodingResult[] result = GeocodingApi.reverseGeocode(context, latLng).await();
			location = new Location();
			location.setLatitude(latitude);
			location.setLongitude(longitude);
			location.setAddress(result[0].formattedAddress);

			for (AddressComponent addr : result[0].addressComponents) {
				for (AddressComponentType type : addr.types) {
					switch (type) {
					case ADMINISTRATIVE_AREA_LEVEL_1:
						location.setCity(addr.longName);
						break;

					case ADMINISTRATIVE_AREA_LEVEL_2:
						location.setState(addr.longName);
						break;

					case COUNTRY:
						location.setCountry(addr.longName);
						break;

					case POSTAL_CODE:
						location.setPostalCode(addr.longName.replace("-", ""));
					}
				}
			}
		} catch (Throwable e) {
			new RuntimeException(e);
		}

		return location;
	}
}
