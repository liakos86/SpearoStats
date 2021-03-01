package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import gr.liakos.spearo.R;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DateUtils;
import gr.liakos.spearo.util.LocationUtils;
import gr.liakos.spearo.util.SpearoUtils;

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
		sessionInfoTextViewDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar_30,0,0,0);

		if (currentSession.getLatitude() !=null && currentSession.getLongitude() != null){
			sessionInfoTextViewLocation.setVisibility(View.VISIBLE);
			LatLng loc = new LatLng(currentSession.getLatitude(), currentSession.getLongitude());
			String addressFromLocation = new LocationUtils().getAddressFromLocation(loc, activity.getApplicationContext()).getAddress();
			sessionInfoTextViewLocation.setText(addressFromLocation);
			sessionInfoTextViewLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.place_marker__30,0,0,0);
		}else{
			sessionInfoTextViewLocation.setVisibility(View.GONE);
		}
		sessionInfoTextViewFish.setText(fishString);
		sessionInfoTextViewFish.setCompoundDrawablesWithIntrinsicBounds(R.drawable.catch_fish_30,0,0,0);

		setSessionImg(currentSession, resources, img);
		
		marker.setInfoWindowAnchor(0.5f, 1f);
	}

	/**
	 * If there is no image set, set icon to the first fish.
	 * If there is a bytearray or uri path for image try to set it.
	 * If user has deleted the image, catch the exception and set the first fish icon.
	 *
	 * @param fishingSession
	 * @param res
	 */
	void setSessionImg(FishingSession fishingSession, Resources res, ImageView fishingSessionIcon) {
		if (fishingSession.getSessionImage() == null && fishingSession.getSessionImageUriPath()==null){
			setFirstFishIcon(fishingSession, res, fishingSessionIcon);
		}else{
			String uriPath = fishingSession.getSessionImageUriPath();

			if (uriPath != null){
				try {
					Uri sessionUri = Uri.parse(uriPath);
					fishingSessionIcon.setImageURI(sessionUri);
					if(fishingSessionIcon.getDrawable() == null){//the image referenced by the uri is deleted
						setFirstFishIcon(fishingSession, res, fishingSessionIcon);
					}

				}catch(Exception e){
					setFirstFishIcon(fishingSession, res, fishingSessionIcon);
				}

				return;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			byte[] array = Base64.decode(fishingSession.getSessionImage(), Base64.NO_WRAP);
			Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length, options);
			fishingSessionIcon.setImageBitmap(bmp);
		}
	}

	private void setFirstFishIcon(FishingSession fishingSession, Resources res, ImageView fishingSessionIcon) {
		Drawable drawableFromFish = null;
		if (fishingSession.getFishCatches().isEmpty()){
			drawableFromFish = ResourcesCompat.getDrawable(res, R.drawable.jelly_grid, null);
		}else{
			drawableFromFish = new SpearoUtils(activity).getGridDrawableFromFish(fishingSession.getFishCatches().get(0).getFish());
		}
		fishingSessionIcon.setImageDrawable(drawableFromFish);
	}

}
