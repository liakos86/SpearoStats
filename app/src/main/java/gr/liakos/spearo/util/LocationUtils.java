package gr.liakos.spearo.util;

import gr.liakos.spearo.R;
import gr.liakos.spearo.bean.AddressWithCountry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtils {
	
	/**
	 * User may have or have not clicked on map. We need to display an address for this location.
	 * If he has not clicked the process ends.
	 * If no address if found the process ends with 'unnamed road'.
	 * 
	 * @return
	 * @throws IOException
	 */
	public AddressWithCountry getAddressFromLocation(LatLng location, Context ctx) {
		AddressWithCountry toReturn = new AddressWithCountry();
		Resources resources = ctx.getResources();
		if (location == null){
			toReturn.setAddress(resources.getString(R.string.no_address));
			return toReturn;
		}
		Geocoder myLocation = new Geocoder(ctx, Locale.getDefault());
		List<Address> myList = new ArrayList<Address>();
		try {
			myList = myLocation.getFromLocation(location.latitude, location.longitude, 1);
		} catch (IOException e) {
			toReturn.setAddress(resources.getString(R.string.no_address));
			//toReturn.setAddress(location.latitude + Constants.COMMA_SEP + location.longitude);
			return toReturn;
		}
		
		if (myList.isEmpty()){
			toReturn.setAddress(resources.getString(R.string.no_address));
			return toReturn;
		}
		
		Address address = (Address) myList.get(0);
		String addressStr = fullAddressFrom(address);
		
		toReturn.setAddress(addressStr);
		toReturn.setCountry(address.getCountryCode());
		return toReturn;
	}
	
	
	String fullAddressFrom(Address address){
		String addressStr = Constants.EMPTY;
		if (address.getAddressLine(0) != null){
			addressStr = address.getAddressLine(0);
		}
		
		if (address.getAddressLine(1) != null){
			addressStr = addressStr + Constants.COMMA_SEP  + address.getAddressLine(1) ;
		}
		
		addressStr = addressStr.replace(Constants.UNNAMED_ROAD, Constants.EMPTY);
		
		
		return addressStr;
	}

}
