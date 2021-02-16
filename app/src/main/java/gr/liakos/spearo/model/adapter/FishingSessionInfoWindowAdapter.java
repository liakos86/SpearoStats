package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
//import gr.liakos.spearo.custom.CircularImageView;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DateUtils;
import gr.liakos.spearo.util.LocationUtils;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import androidx.core.content.res.ResourcesCompat;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedImageView;

public class FishingSessionInfoWindowAdapter 
implements InfoWindowAdapter{
	
	View mymarkerview;
	
	Map<String, FishingSession> sessions2MarkerIds;
	
	Activity activity;

	public FishingSessionInfoWindowAdapter(Activity act, Map<String, FishingSession> sessions2MarkerIds) {
		this.sessions2MarkerIds = sessions2MarkerIds;
		activity = act;
        mymarkerview = activity.getLayoutInflater().inflate(R.layout.custom_map_info_window, null);
    }

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		render(marker, mymarkerview);
		return mymarkerview;
	}
	
	void render(Marker marker, View view) {
		FishingSession currentSession = null;
		for (Map.Entry<String, FishingSession> sessionIdEntry : sessions2MarkerIds.entrySet()) {
			if (sessionIdEntry.getKey().equals(marker.getId())) {
				currentSession = sessionIdEntry.getValue();
			}
		}
		List<FishCatch> fishCatches = currentSession.getFishCatches();

		//CircularImageView img = (CircularImageView) view.findViewById(R.id.info_window_fish);
		// img = (ImageView) view.findViewById(R.id.info_window_icon);
		RoundedImageView img = (RoundedImageView) view.findViewById(R.id.info_window_icon);
		TextView sessionInfoTextViewDate = (TextView) view.findViewById(R.id.info_window_text_date);
		TextView sessionInfoTextViewFish = (TextView) view.findViewById(R.id.info_window_text_fish);
		TextView sessionInfoTextViewLocation = (TextView) view.findViewById(R.id.info_window_text_location);

		Resources resources = activity.getResources();

		String catchesString = fishCatches.size() == 1 ? resources.getString(R.string.fish): resources.getString(R.string.fishes);
		String fishString = fishCatches.size() ==0 ? resources.getString(R.string.fola) : (fishCatches.size() + Constants.SPACE + catchesString);

		Calendar cal = DateUtils.getCalendarWithTime(currentSession.getFishingDate());
		String newDateString = String.valueOf(cal.get(Calendar.YEAR));
		newDateString += Constants.COMMA_SEP + DateUtils.getDayAndMonth(cal, activity.getApplicationContext());
		sessionInfoTextViewDate.setText(newDateString);

		if (currentSession.getLatitude() !=null && currentSession.getLongitude() != null){
			sessionInfoTextViewLocation.setVisibility(View.VISIBLE);
			LatLng loc = new LatLng(currentSession.getLatitude(), currentSession.getLongitude());
			String addressFromLocation = new LocationUtils().getAddressFromLocation(loc, activity.getApplicationContext()).getAddress();
			sessionInfoTextViewLocation.setText(addressFromLocation);
		}else{
			sessionInfoTextViewLocation.setVisibility(View.GONE);
		}
		sessionInfoTextViewFish.setText(fishString);

		SpearoUtils spearoUtils = new SpearoUtils(activity);
		if (currentSession.getSessionImage() != null){
			Options options = new Options();
            options.inScaled = false;
			byte[] array = Base64.decode(currentSession.getSessionImage(), Base64.NO_WRAP);
        	Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length, options);
			img.setImageBitmap(bmp);
		}else{
			if (fishCatches.isEmpty()){
				img.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jelly_grid, null));
			}else{
				img.setImageDrawable(spearoUtils.getGridDrawableFromFish( fishCatches.get(0).getFish()));
			}
		}
		
		marker.setInfoWindowAnchor(0.5f, 1f);
	}

}
