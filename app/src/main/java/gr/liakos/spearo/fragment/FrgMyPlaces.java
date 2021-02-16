package gr.liakos.spearo.fragment;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.adapter.FishingSessionInfoWindowAdapter;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.ObjectUtils;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class FrgMyPlaces extends Fragment
		implements OnMapReadyCallback {

	GoogleMap googleMap;
	SupportMapFragment mapFragment;
	Map<String, FishingSession> sessions2MarkerIds;
	ImageView transparentImageViewOverMap;
	AdRequest adRequest;
	AdView adView;
	boolean mapInitialized = false;

	@Override
	public void setUserVisibleHint(boolean visible) {
		super.setUserVisibleHint(visible);
		if (visible && isResumed() && !mapInitialized) {
			initializeMap();
			mapInitialized = true;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.frg_my_map, container, false);

		adView = ((AdView) view.findViewById(R.id.adViewMyPlaces));
		new LoadAsyncAd().execute();

		transparentImageViewOverMap = (ImageView) view.findViewById(R.id.transparent_img);
		setImageEvents();
		return view;
	}

	public void initializeMap() {
		if (mapFragment == null) {
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.getMapAsync(this);
		}
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		// R.id.map is a layout
		transaction.replace(R.id.googleMap, mapFragment, null).commit();


	}

	public static FrgMyPlaces init(int val) {
		FrgMyPlaces truitonList = new FrgMyPlaces();
		Bundle args = new Bundle();
		args.putInt("val", val);
		truitonList.setArguments(args);
		return truitonList;
	}

	public void zoomInLocation(LatLng latLng){
		if (latLng == null){
			return;
		}

		if (googleMap == null){
			return;
		}

		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6.0f));
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap gMap) {
		googleMap = gMap;
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		googleMap.setIndoorEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(true);

		ActSpearoStatsMain parentAct = (ActSpearoStatsMain) getActivity();
		if (parentAct.isMapPermissionGiven()) {
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		}

		if (parentAct.getCurrentLatLng() != null){
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(parentAct.getCurrentLatLng(), 6);
			googleMap.animateCamera(cameraUpdate);
		}

		drawMapMarkers(false);
	}

	public void redrawMarkersIfNeeded() {
		
		if (googleMap == null){
			return;
		}
		
		if (((SpearoApplication)getActivity().getApplication()).isSessionsHaveChanged()){
    		drawMapMarkers(true);
    	}
		
	}

	/**
	 * Gets the fishing sessions from app or db.
	 * Discards the ones with no location specified.
	 * Adds a custom marker for each session.
	 * Adds a custom info window for each marker.
	 * 
	 * @param shouldLoadFromDb
	 */
	void drawMapMarkers(boolean shouldLoadFromDb) {
		sessions2MarkerIds = new HashMap<String, FishingSession>();
		googleMap.clear();
		List<FishingSession> sessions = new ArrayList<FishingSession>();
		if (shouldLoadFromDb){
			ActSpearoStatsMain activity = (ActSpearoStatsMain)getActivity();
			sessions = new Database(activity).fetchFishingSessionsFromDb();
		}else {
			sessions = ((SpearoApplication)getActivity().getApplication()).getFishingSessions();
		}
		
		for (FishingSession fishingSession : sessions) {
			if (ObjectUtils.isNullOrZero(fishingSession.getLatitude()) || 
					ObjectUtils.isNullOrZero(fishingSession.getLongitude())){
				continue;//user did not provide location
			}
			addMarkerFor(fishingSession);
		}
		
		 googleMap.setInfoWindowAdapter(new FishingSessionInfoWindowAdapter(getActivity(), sessions2MarkerIds));
		
	}

	void addMarkerFor(FishingSession fishingSession) {
		View markerView = null;
		if (fishingSession.getFishCatches().isEmpty()){
			markerView= ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_layout_fola, null);
		}else{
			markerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_layout, null);
		}
		
		TextView numTxt = (TextView) markerView.findViewById(R.id.fish_num_txt);
		numTxt.setText(String.valueOf(fishingSession.getFishCatches().size()));
		
		LatLng point = new LatLng(fishingSession.getLatitude(), fishingSession.getLongitude());
		Marker addMarker = googleMap.addMarker(new MarkerOptions()
		.position(point)
		.title("Title")
		.snippet("Description")
		.icon(BitmapDescriptorFactory.fromBitmap(new SpearoUtils(getActivity()).createDrawableFromView(getActivity(), markerView))));
		
		sessions2MarkerIds.put(addMarker.getId(), fishingSession);
	
	}
	
	 public void hideKeyboardFrom() {
		    InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
	}
	
	 
	 void setImageEvents(){
	    	

	    	transparentImageViewOverMap.setOnTouchListener(new View.OnTouchListener() {

	    	    @Override
	    	    public boolean onTouch(View v, MotionEvent event) {
	    	        int action = event.getAction();
	    	        switch (action) {
	    	           case MotionEvent.ACTION_DOWN:
	    	                // Disallow ScrollView to intercept touch events.
	    	        	   ((ActSpearoStatsMain)getActivity()).interceptEvents(true);
	    	                // Disable touch on transparent view
	    	                return false;

	    	           case MotionEvent.ACTION_UP:
	    	                // Allow ScrollView to intercept touch events.
	    	        	   ((ActSpearoStatsMain)getActivity()).interceptEvents(false);
	    	                return true;

	    	           case MotionEvent.ACTION_MOVE:
	    	        	   ((ActSpearoStatsMain)getActivity()).interceptEvents(true);
	    	                return false;

	    	           default: 
	    	                return true;
	    	        }   
	    	    }
	    	});
	    }
	 
	 
	    private void placeAds() {
	        adRequest = new AdRequest.Builder().build();
	    }

	 
	 private class LoadAsyncAd extends AsyncTask<Void, Void, Void> {

	        protected void onPreExecute() {
	        }

	        @Override
	        protected Void doInBackground(Void... unused) {
	            placeAds();
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void result) {
	            adView.loadAd(adRequest);
	        }

	    }

}
