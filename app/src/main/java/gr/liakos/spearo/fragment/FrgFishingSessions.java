package gr.liakos.spearo.fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.application.ClearableAutoCompleteTextView;
import gr.liakos.spearo.application.NumberPicker;
import gr.liakos.spearo.bean.AddressWithCountry;
import gr.liakos.spearo.custom.GridRecyclerView;
import gr.liakos.spearo.enums.Wind;
import gr.liakos.spearo.enums.WindVolume;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.adapter.AutoCompleteSearchAdapter;
import gr.liakos.spearo.model.adapter.CustomSpinnerAdapter;
import gr.liakos.spearo.model.adapter.FishingSessionCatchesAdapter;
import gr.liakos.spearo.model.adapter.FishingSessionRecyclerViewAdapter;
import gr.liakos.spearo.model.adapter.SpeargunsSpinnerAdapterForSessions;
import gr.liakos.spearo.model.adapter.WindSpinnerAdapter;
import gr.liakos.spearo.model.adapter.WindVolumeSpinnerAdapter;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.util.ConsistencyChecker;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DateUtils;
import gr.liakos.spearo.util.LocationUtils;
import gr.liakos.spearo.util.MetricConverter;
import gr.liakos.spearo.util.SpearoUtils;
import gr.liakos.spearo.util.helper.AlertDialogHelper;

public class FrgFishingSessions
		extends Fragment
		implements OnMapReadyCallback {

	/**
	 * Flipper positions
	 */
	static final Integer POSITION_SHOW_SESSIONS = 0;
	static final Integer POSITION_NEW_SESSION = 1;
	static final Integer POSITION_NO_SESSIONS = 2;
	static final Integer POSITION_MAP = 3;

	ViewFlipper sessionsFlipper;

	/**
	 * Ads
	 */
	InterstitialAd mInterstitialAd;
	AdRequest adRequest;
	AdView adView;

	ClearableAutoCompleteTextView autoCompleteFish;
	AutoCompleteSearchAdapter autoCompleteSearchAdapter;
	NumberPicker pickerDepth;
	NumberPicker pickerWeight;
	FishingSessionCatchesAdapter sessionCatchesAdapter;
	Button buttonCheckout;
	Spinner spinnerCatchTime;

	/**
	 * Map.
	 */
	GoogleMap googleMap;
	SupportMapFragment mapFragment;
	AutocompleteSupportFragment autocompleteFragment;
	ImageView transparentImageViewOverMap;

	/**
	 * Lists.
	 */
	List<FishingSession> fishingSessions = new ArrayList<FishingSession>();
	List<FishCatch> sessionCatchesList;
	List<Fish> fishies = new ArrayList<Fish>();

	/**
	 * Other stuff
	 */
	TextView textDateAndLocation;
	FishingSessionRecyclerViewAdapter recyclerViewAdapter;
	View.OnTouchListener inScrollTouchListener;
	SpearoUtils spearoUtils;

	/**
	 * catch stuff
	 */
	Fish selectedFish;
	Integer catchHour = null;
	int catchDateMillis;
	Integer caughtByGunId = null;
	Button addCatchButton;
	RelativeLayout layoutFishCatchMeta;
	LinearLayout layoutInsertButtons;
	LinearLayout layoutFishSelect;
	TextView textViewFishCatchMeta;
	Spinner spinnerSpearGuns;

	/**
	 * session stuff
	 */
	LatLng location;
	long sessionMillis;
	String sessionImgBytes;
	Calendar calendar;//for session date and session moon
	Wind wind;
	WindVolume windVolume;
	String sessionImgUriPath;
	Integer sessionId = Database.INVALID_ID;
	FrameLayout layoutSessionCatches;
	Button folaButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializePlaces();
		spearoUtils = new SpearoUtils(getActivity());
		initializeAds();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View v = inflater.inflate(R.layout.frg_fishing_sessions, container, false);
		fishingSessions = getFishingSessions();

		Collections.sort(fishingSessions);
		createSessionCatchesListView(v);
		createSessionsList(v);
		createCatchTimeSpinner(v);
		setupSpearGunsSpinner(v);
		createAutoCompleteDropDown(v);
		setViews(v);
		setConfirmLocationButton(v);
		setDeleteLocationButton(v);
		setSaveFishingSessionButton(v);
		setAddOneMoreCatchButton(v);
		setCancelAndExitButton(v);
		setAddCatchButton(v);
		setInScrollViewTouchListener();
		setFloatingAddButtonListenersForNewSession(v);
		updateCheckoutButtonText();

		clearCatchViews();
		clearSessionViews();

		if (fishingSessions.isEmpty()) {
			sessionsFlipper.setDisplayedChild(POSITION_NO_SESSIONS);
		}

		adView = v.findViewById(R.id.adViewNewSession);
		new LoadAsyncAd().execute();

		return v;
	}

//	private void startShowcaseView() {
//
//		int[] pointA = new int[2];
//		autoCompleteFish.getLocationOnScreen(pointA);
//		Rect rectA = new Rect(pointA[0], pointA[1], pointA[0] + autoCompleteFish.getWidth(), pointA[1] + autoCompleteFish.getHeight());
//
//		ShowcaseConfig config = new ShowcaseConfig();
//		config.setDelay(100);
//		config.setShape(new RectangleShape(rectA, true));
//
//		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity(), Constants.SHOWCASE_FRG_SESSIONS);
//		sequence.setConfig(config);
//
//		String gotIt = getResources().getString(android.R.string.ok);
//
//		sequence.addSequenceItem(autoCompleteFish,
//				getResources().getString(R.string.showcase_auto_comp), gotIt);
//		sequence.addSequenceItem(pickerDepth,
//				getResources().getString(R.string.showcase_depth), gotIt);
//		sequence.addSequenceItem(pickerWeight,
//				getResources().getString(R.string.showcase_weight), gotIt);
//		sequence.addSequenceItem(spinnerCatchTime,
//				getResources().getString(R.string.showcase_catch_time), gotIt);
//		sequence.addSequenceItem(addCatchButton,
//				getResources().getString(R.string.showcase_add_catch), gotIt);
//		sequence.addSequenceItem(buttonCheckout,
//				getResources().getString(R.string.showcase_checkout), gotIt);
//
//		sequence.start();
//	}

	void initializeAds() {
		mInterstitialAd = new InterstitialAd(getActivity());
		mInterstitialAd.setAdUnitId(getResources().getString(R.string.adId_frg_sessions_interstitial_layout));
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
		mInterstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				// Load the next interstitial.
				mInterstitialAd.loadAd(new AdRequest.Builder().build());
			}
		});
	}

	void initializePlaces() {
		String apiKey = getString(R.string.api_google_key_places);
		if (!Places.isInitialized()) {
			Places.initialize(getContext(), apiKey);
		}
		Places.createClient(getContext());
	}

	/**
	 * User does not want to provide location of fishing.
	 *
	 * @param v
	 */
	void setDeleteLocationButton(View v) {
		Button buttonDeleteLocation = ((Button) v.findViewById(R.id.button_delete_location));
		buttonDeleteLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				((ActSpearoStatsMain) getActivity()).collapseAppbar();
				location = null;
				goToNewSessionChild();

			}
		});
	}

	/**
	 * User adds a fish catch to session. Validations applied.
	 *
	 * @param v
	 */
	void setAddCatchButton(View v) {
		addCatchButton = (Button) v.findViewById(R.id.button_add_to_basket);
		addCatchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedFish == null) {
					spearoUtils.snack(sessionsFlipper, R.string.no_fish_name_selected);
					return;
				}

				if (!addCatchToSession()) {
					return;
				}

				spearoUtils.snack(sessionsFlipper, selectedFish.getCommonName() + Constants.SPACE + getResources().getString(R.string.added));
				updateCheckoutButtonText();
				clearCatchViews();

				addSessionCatchesImages();

				//confirmFishCatchesAlertDialog(true);

				layoutFishCatchMeta.setVisibility(View.GONE);

				layoutInsertButtons.setVisibility(View.VISIBLE);
			}
		});
	}

	void addSessionCatchesImages() {

		layoutSessionCatches.removeAllViews();

		int leftMargin = 50;
		int delay = 0;
		for (FishCatch fishCatch : sessionCatchesList) {

			ImageView iv = new ImageView(getContext());

			Drawable drawable = new SpearoUtils(getContext()).getDrawableFromFish(fishCatch.getFish());
			iv.setImageDrawable(drawable);

			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

			lp.setMargins(leftMargin, 50, 0, 0);


			//lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

			lp.height = 150;
			lp.width = 150;
			lp.gravity = Gravity.CENTER;

			leftMargin += 50;
			delay += 200;

			layoutSessionCatches.addView(iv, lp);

			ObjectAnimator animation = ObjectAnimator.ofFloat(iv, "translationX", 80f);
			animation.setDuration(1000);
			animation.setRepeatCount(0);
			animation.setStartDelay(delay);
			//animation.set
			animation.start();


		}

	}

	/**
	 * User finished fish catch entry. If no catches, he confirms his bad luck.
	 *
	 * @param v
	 */
	void setSaveFishingSessionButton(final View v) {
		buttonCheckout = (Button) v.findViewById(R.id.button_checkout_basket);
		buttonCheckout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (sessionCatchesList.isEmpty()) {
					confirmFolaAlertDialog();
					return;
				}
				confirmFishCatchesAlertDialog();
			}
		});
	}

	/**
	 * User is on the insert catches 'page'.
	 * He can return to sessions page. All data entry is deleted.
	 *
	 * @param v
	 */
	void setAddOneMoreCatchButton(final View v) {
		Button buttonCancel = (Button) v.findViewById(R.id.button_add_one_more_catch);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//confirmReturnAlertDialog();
				clearCatchViews();
				layoutFishSelect.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * User is on the insert catches 'page'.
	 * He can return to sessions page. All data entry is deleted.
	 *
	 * @param v
	 */
	void setCancelAndExitButton(final View v) {
		Button buttonCancel = (Button) v.findViewById(R.id.button_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				confirmReturnAlertDialog();
			}
		});
	}

	/**
	 * User cannot insert a new species record.
	 * It has to be official first.
	 *
	 * @param fish
	 * @param attempt
	 */
	void alertRecordAttemptFor(Fish fish, Double attempt) {
		AlertDialogHelper.alertDialogWithPositive(getActivity(), getResources().getString(R.string.cancel), null, fish.getCommonName() + Constants.COLON + Constants.SPACE + getResources().getString(R.string.record_entry_attempt_alert) + Constants.SPACE + attempt);
	}

	/**
	 * Button which confirms the location on map. Validation applied.
	 *
	 * @param v
	 */
	void setConfirmLocationButton(View v) {
		Button buttonConfirmLocation = ((Button) v.findViewById(R.id.button_confirm_location));
		buttonConfirmLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (location == null) {
					spearoUtils.snack(sessionsFlipper, R.string.select_location);
					return;
				}

				collapseAppbar();
				goToNewSessionChild();
			}
		});
	}

	/**
	 * Floating action buttons listeners created.
	 *
	 * @param v
	 */
	void setFloatingAddButtonListenersForNewSession(View v) {
		OnClickListener addListener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				beginNewSessionEntryDialogs();
			}
		};

		FloatingActionButton fabAdd = (FloatingActionButton) v.findViewById(R.id.fab_add);
		fabAdd.setOnClickListener(addListener);
		FloatingActionButton fabAdd2 = (FloatingActionButton) v.findViewById(R.id.fab_add_2);
		fabAdd2.setOnClickListener(addListener);
	}

	/**
	 * begin actions.
	 */
	public void beginNewSessionEntryDialogs() {
		collapseAppbar();
		initializeMap();
		beginDateAlertDialog();
	}

	/**
	 * Create the autocomplete with the fish to select in a fish catch.
	 *
	 * @param v
	 */
	void createAutoCompleteDropDown(View v) {
		autoCompleteFish = (ClearableAutoCompleteTextView) v.findViewById(R.id.auto_complete_fish);
		autoCompleteFish.setContext(getActivity().getApplicationContext());

		refreshFish();
		autoCompleteSearchAdapter = new AutoCompleteSearchAdapter(this, fishies);
		autoCompleteFish.setAdapter(autoCompleteSearchAdapter);
		autoCompleteFish.setDropDownVerticalOffset(4);

	}

	/**
	 * The session catches list. It is being populated by user input catches.
	 * It is not visible until confirmation upon persistence.
	 *
	 * @param v
	 */
	void createSessionCatchesListView(View v) {
		sessionCatchesList = new ArrayList<FishCatch>();
		sessionCatchesAdapter = new FishingSessionCatchesAdapter(this, getActivity(), R.layout.fish_catch_row_with_delete, sessionCatchesList);
	}

	/**
	 * The google map fragment can become null when being into a Fragment.
	 * We need to recreate it and always replace the map view with this map fragment.
	 */
	void initializeMap() {
		if (mapFragment == null) {
			mapFragment = SupportMapFragment.newInstance();
			mapFragment.getMapAsync(this);
		}
		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
		// R.id.map is a layout
		transaction.replace(R.id.googleMap, mapFragment).commit();
	}

	/**
	 * Spinner to select a catch hour window, e.g. 08:00-09:00
	 *
	 * @param v
	 */
	void createCatchTimeSpinner(View v) {
		String[] catchHoursArray = getResources().getStringArray(R.array.catch_hours);
		final List<String> catchHours = Arrays.asList(catchHoursArray);
		spinnerCatchTime = (Spinner) v.findViewById(R.id.spinnerCatchTime);
		CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, catchHoursArray, Arrays.asList(catchHoursArray));
		spinnerCatchTime.setAdapter(adapter);
		spinnerCatchTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position == 0) {
					catchHour = null;
					return;
				}
				catchHour = Integer.valueOf(catchHours.get(position).substring(0, 2));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

	}

	/**
	 * User has selected date, wind, wind power and location optionally.
	 * Moon will be automatically calculated.
	 *
	 */
	void goToNewSessionChild() {
		sessionsFlipper.setDisplayedChild(POSITION_NEW_SESSION);
		layoutFishSelect.setVisibility(View.VISIBLE);

		AddressWithCountry addressWithCountry = new LocationUtils().getAddressFromLocation(location, getActivity().getApplicationContext());
		String addressFromLocation = addressWithCountry.getAddress();
		if (addressFromLocation != null) {
			textDateAndLocation.setText(DateUtils.dateFromMillis(sessionMillis) + Constants.COMMA_SEP + addressFromLocation);
		} else {
			textDateAndLocation.setText(DateUtils.dateFromMillis(sessionMillis));
		}

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			startShowcaseView();
//		}

	}

	/**
	 * If no sessions available, show the empty view with add button.
	 */
	void goToSessionsChild() {
		if (fishingSessions.isEmpty()) {
			sessionsFlipper.setDisplayedChild(POSITION_NO_SESSIONS);
			return;
		}
		sessionsFlipper.setDisplayedChild(POSITION_SHOW_SESSIONS);
	}

	/**
	 * All actions regarding saving a session.
	 * Views are then cleared. Objects are re-initialized.
	 * The interstitial ad is shown.
	 */
	void saveSessionActions() {
		FishingSession newSession = saveOrUpdateFishingSession();
		clearCatchViews();
		clearSessionViews();
		updateSessions();
		informApplicationForNewSession();
		updateCheckoutButtonText();
		markSuspiciousSession(newSession);
		showAd();
		sessionsFlipper.setDisplayedChild(POSITION_SHOW_SESSIONS);
		((SpearoApplication) getActivity().getApplication()).uploadSessions();
		((SpearoApplication) getActivity().getApplication()).checkForSpearGunUpdate(newSession.getFishCatches());

	}

	/**
	 * The main activity handles the permission check for image selection.
	 * Also the image byte array is returned from main act.
	 */
	void selectImage() {
		((ActSpearoStatsMain) getActivity()).selectImage();
	}

	/**
	 * A user can easily corrupt the mongo data if he starts inserting abnormal catches.
	 * So a suspicious session is one that contains a catch that weights over 50% of the official record (or allowed weight).
	 * The consequitive suspicious sessions are saved and the total number of suspicious sessions.
	 *
	 * @param newSession
	 */
	void markSuspiciousSession(FishingSession newSession) {
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		int suspiciousStreak = app_preferences.getInt(Constants.SUSPICIOUS_STREAK, 0);
		Editor editor = app_preferences.edit();
		boolean suspiciousSession = ConsistencyChecker.isSuspiciousSession(newSession);
		if (suspiciousSession) {
			int suspiciousCount = app_preferences.getInt(Constants.SUSPICIOUS_COUNT, 0);
			editor.putInt(Constants.SUSPICIOUS_COUNT, ++suspiciousCount);
			editor.putInt(Constants.SUSPICIOUS_STREAK, ++suspiciousStreak);

		} else {
			editor.putInt(Constants.SUSPICIOUS_STREAK, 0);
		}
		editor.apply();
	}

	/**
	 * Display ad
	 */
	void showAd() {
		if (mInterstitialAd.isLoaded()) {
			mInterstitialAd.show();
		}
	}

	/**
	 * The map is visible and user can select a fishing spot.
	 */
	void beginMapInteraction() {
		((ActSpearoStatsMain) getActivity()).collapseAppbar();
		sessionsFlipper.setDisplayedChild(POSITION_MAP);
	}

	void informApplicationForNewSession() {
		((SpearoApplication) getActivity().getApplication()).setSessionsHaveChanged(true);
	}

	/**
	 * {@link GridRecyclerView} view to display the sessions.
	 * {@link FishingSessionRecyclerViewAdapter} to handle the data.
	 * {@link GridLayoutManager} to handle the grid animations.
	 *
	 * @param v
	 */
	public void createSessionsList(View v) {
		GridRecyclerView recyclerView = v.findViewById(R.id.recyclerView);
		recyclerViewAdapter = new FishingSessionRecyclerViewAdapter(getActivity(), fishingSessions, this);
		recyclerView.setAdapter(recyclerViewAdapter);

		GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(manager);
	}

	/**
	 * {@link AutoCompleteSearchAdapter} calls this on click of a view.
	 *
	 * @param fish
	 */
	public void setSelectedFish(Fish fish) {
		autoCompleteFish.dismissDropDown();
		ActSpearoStatsMain activity = (ActSpearoStatsMain) getActivity();
		selectedFish = Fish.getFromId(activity, fish.getFishId());
		String addedFishText = activity.getResources().getString(R.string.prey) + selectedFish.getCommonName();
		autoCompleteFish.setText(addedFishText);

		String addedFishButtonText = activity.getResources().getString(R.string.click_to_add) + Constants.SPACE + Constants.SINGLE_QUOTE +  selectedFish.getCommonName() + Constants.SINGLE_QUOTE;
		addCatchButton.setText(addedFishButtonText);

		textViewFishCatchMeta.setText(getActivity().getResources().getString(R.string.textview_fish_meta_question, selectedFish.getCommonName()));
		textViewFishCatchMeta.setCompoundDrawablesWithIntrinsicBounds(new SpearoUtils(getContext()).getDrawableFromFish(fish), null, null, null);

		layoutFishSelect.setVisibility(View.GONE);
		layoutFishCatchMeta.setVisibility(View.VISIBLE);
		folaButton.setVisibility(View.GONE);
	}

	/**
	 * If no catches, it does not need a mongo upload.
	 * After saving the session display the interstitial ad.
	 */
	FishingSession saveOrUpdateFishingSession() {
		FishingSession session = new FishingSession();

		session.setFishingSessionId(sessionId);
		session.setFishingDate(sessionMillis);
		session.setSessionImage(sessionImgBytes);
		session.setSessionImageUriPath(sessionImgUriPath);

		if (sessionCatchesList.isEmpty() || sessionId > 0) {
			session.setUploadedToMongo(true);
		}
		else {
			session.getFishCatches().addAll(sessionCatchesList);
		}
		if (location != null) {
			session.setLatitude(location.latitude);
			session.setLongitude(location.longitude);
		}

		session.setSessionWind(wind);
		if (Wind.NO_WIND.equals(wind)){
			windVolume = WindVolume.NOT_KNOWN;
		}

		session.setSessionWindVolume(windVolume);

		ActSpearoStatsMain activity = (ActSpearoStatsMain) getActivity();
		Database db = new Database(activity);
		db.addOrUpdateFishingSession(session);
		return session;
	}

	/**
	 * {@link AutocompleteSupportFragment} used to provide map location suggestions.
	 * A transparent image over map is needed in order to catch the scrolling events,
	 * on top of the scroll view of the appbarlayout.
	 * Flipper for changing screens.
	 *
	 * @param v
	 */
	void setViews(View v) {

		autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
		autocompleteFragment.setHint(getResources().getString(R.string.map_search_hint));
		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				LatLng point = place.getLatLng();
				addMarker(point);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12.0f));
			}

			@Override
			public void onError(Status status) {
				spearoUtils.snack(sessionsFlipper, "Place could not be selected");
			}
		});

		transparentImageViewOverMap = (ImageView) v.findViewById(R.id.transparent_img);
		sessionsFlipper = (ViewFlipper) v.findViewById(R.id.fishing_sessions_flipper);
		textDateAndLocation = (TextView) v.findViewById(R.id.textview_session_date_location);

		layoutFishSelect = v.findViewById(R.id.layout_fish_select);
		layoutFishCatchMeta = v.findViewById(R.id.layout_fish_catch_meta);
		layoutInsertButtons = v.findViewById(R.id.layout_complete_session_or_add_fish_layout);
		textViewFishCatchMeta = v.findViewById(R.id.textview_fish_meta_question);

		layoutSessionCatches = v.findViewById(R.id.frameLayoutSessionCatches);

		folaButton = v.findViewById(R.id.folaButton);
		folaButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sessionCatchesList.isEmpty()){
					return;
				}

				confirmFolaAlertDialog();
			}
		});

		setupPickers(v);
	}

	/**
	 * Pickers for depth and weight.
	 *
	 * @param v
	 */
	void setupPickers(View v) {
		pickerDepth = (NumberPicker) v.findViewById(R.id.picker_catch_depth);
		pickerWeight = (NumberPicker) v.findViewById(R.id.picker_catch_weight);
		spearoUtils.setupMetricWeightPicker(pickerWeight);
		spearoUtils.setupMetricDepthPicker(pickerDepth);
	}

	void setupSpearGunsSpinner(View v){

		List<Speargun> guns = new ArrayList<>(((SpearoApplication) getActivity().getApplication()).getSpearGuns());
		guns.add(0, Speargun.dummy(guns.size(), getActivity().getResources()));

		spinnerSpearGuns = v.findViewById(R.id.spinner_gun_fish);
		SpeargunsSpinnerAdapterForSessions speargunsAdapter = new SpeargunsSpinnerAdapterForSessions(getActivity(), android.R.layout.simple_spinner_item, guns);
		spinnerSpearGuns.setAdapter(speargunsAdapter);

		spinnerSpearGuns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				caughtByGunId = guns.get(position).getGunId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

	}

	/**
	 * If a new gun is added via the {@link FrgSpeargunStats}, we need to present it to the user.
	 */
	public void reloadGunsSpinner(){
		if (!((SpearoApplication) requireActivity().getApplication()).isSpearGunsUpdated()){
			return;
		}

		SpearoApplication application = ((SpearoApplication) requireActivity().getApplication());
		application.setSpearGunsUpdated(false);

		List<Speargun> guns = new ArrayList<>(application.getSpearGuns());
		guns.add(0, Speargun.dummy(guns.size(), application.getResources()));
		SpeargunsSpinnerAdapterForSessions speargunsAdapter = new SpeargunsSpinnerAdapterForSessions(getActivity(), android.R.layout.simple_spinner_item, guns);
		spinnerSpearGuns.setAdapter(speargunsAdapter);
	}

	/**
	 * Clear views and objects concerning the fish catch.
	 */
	void clearCatchViews() {
		caughtByGunId = null;
		pickerDepth.setValue(0);
		pickerWeight.setValue(0);
		selectedFish = null;
		autoCompleteFish.setText(Constants.EMPTY);
		autoCompleteFish.dismissDropDown();
		spinnerCatchTime.setSelection(0);
		catchHour = null;
		catchDateMillis = 0;
		addCatchButton.setText(getActivity().getResources().getText(R.string.add_catch));

		layoutFishCatchMeta.setVisibility(View.INVISIBLE);
		layoutInsertButtons.setVisibility(View.INVISIBLE);
		layoutFishSelect.setVisibility(View.INVISIBLE);
	}

	/**
	 * Clear views and objects concerning the fish session.
	 */
	void clearSessionViews() {
		autocompleteFragment.setText(Constants.EMPTY);
		sessionId = Database.INVALID_ID;
		location = null;
		sessionImgBytes = null;
		sessionImgUriPath = null;
		wind = Wind.NOT_KNOWN;
		windVolume = WindVolume.NOT_KNOWN;
		if (googleMap != null) {
			googleMap.clear();
		}
		sessionCatchesList.clear();
		sessionCatchesAdapter.notifyDataSetChanged();
		sessionMillis = 0L;
		textDateAndLocation.setText(Constants.EMPTY);
		updateCheckoutButtonText();
		folaButton.setVisibility(View.VISIBLE);
	}

	/**
	 * If user tries to insert a record catch it is not allowed.
	 */
	boolean addCatchToSession() {
		SharedPreferences app_preferences = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		double weightFromPickers = MetricConverter.convertWeightFromPickers(app_preferences, pickerWeight.getValue());
		if (selectedFish.isRecordCatch(weightFromPickers)) {
			alertRecordAttemptFor(selectedFish, pickerWeight.getValue());
			return false;
		}

		FishCatch fishCatchToAdd = new FishCatch();
		int selectedFishId = selectedFish.getFishId().intValue();
		fishCatchToAdd.setFishId(selectedFishId);
		ActSpearoStatsMain activity = (ActSpearoStatsMain) getActivity();
		Fish fish = Fish.getFromId(activity, selectedFishId);
		fishCatchToAdd.setFish(fish);
		fishCatchToAdd.setDepthMeters(MetricConverter.convertDepthFromPickers(app_preferences, pickerDepth.getValue()));
		fishCatchToAdd.setWeight(weightFromPickers);
		fishCatchToAdd.setCaughtWith(caughtByGunId);

		if (catchHour != null) {
			fishCatchToAdd.setCatchHour(catchHour);
		}
		sessionCatchesList.add(fishCatchToAdd);
		sessionCatchesAdapter.notifyDataSetChanged();

		return true;
	}

	/**
	 * When a new session is persisted we need to update the grid view list.
	 * Just adding it is not enough. We need to sort the collection.
	 * We also update the appbar fish count.
	 */
	public void updateSessions() {
		int fish = 0;

		fishingSessions.clear();
		List<FishingSession> fresh = getDbFishingSessions();
		for (FishingSession fishingSession : fresh) {
			fishingSessions.add(fishingSession);
			fish += fishingSession.getFishCatches().size();
		}
		Collections.sort(fishingSessions);

		recyclerViewAdapter.notifyDataSetChanged();
		((ActSpearoStatsMain) getActivity()).fixCatchesNum(fish);

		if (fishingSessions.isEmpty()) {
			sessionsFlipper.setDisplayedChild(POSITION_NO_SESSIONS);
		}

	}

	/**
	 * When the map is ready we need to parameterize it.
	 * Also we check the main act for permissions in order to enable user location and move camera to current position.
	 *
	 * @param gMap
	 */
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
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(parentAct.getCurrentLatLng(), 12);
			googleMap.animateCamera(cameraUpdate);
		}

		googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				addMarker(point);
			}
		});
	}

	/**
	 * When user long clicks the map, we add a marker and save the location.
	 *
	 * @param point
	 */
	void addMarker(LatLng point) {
		googleMap.clear();
		location = point;

		View markerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_layout_sessions, null);
		TextView numTxt = (TextView) markerView.findViewById(R.id.fish_num_txt);
		String markerText = getResources().getString(R.string.marker_text);
		numTxt.setText(markerText);
		googleMap.addMarker(new MarkerOptions()
				.position(point)
				.title(markerText)
				.icon(BitmapDescriptorFactory.fromBitmap(spearoUtils.createDrawableFromView(getActivity(), markerView))));

	}

	/**
	 * Main act calls this with a byte array converted to string.
	 * If user did not provide permission, or he regretted the image insertion, the string is null.
	 *
	 * @param sessionBytes
	 */
	public void setSessionBytesAndSave(String sessionBytes) {
		this.sessionImgBytes = sessionBytes;
		saveSessionActions();
	}

	public void setSessionUriAndSave(String uriPath) {
		this.sessionImgUriPath = uriPath;
		saveSessionActions();
	}

	List<FishingSession> getFishingSessions(){
		return ((SpearoApplication) this.getActivity().getApplication()).getFishingSessions();
	}

	List<FishingSession> getDbFishingSessions(){
		ActSpearoStatsMain activity = (ActSpearoStatsMain) this.getActivity();
		return ((SpearoApplication) activity.getApplication()).getDbFishingSessions();
	}

	public void reloadFishiesOnNewRecord(){
		if (!((SpearoApplication) getActivity().getApplication()).isNewCommunityDataRecord()){
			return;
		}
		
		refreshFish();
		
		autoCompleteSearchAdapter = new AutoCompleteSearchAdapter(this, fishies);
		autoCompleteFish.setAdapter(autoCompleteSearchAdapter);
		autoCompleteSearchAdapter.notifyDataSetChanged();
		((SpearoApplication)getActivity().getApplication()).setNewCommunityDataRecord(false);
	}
	
	void refreshFish() {
		List<Fish> fishies2 = getFishies();
		fishies.clear();
		for (Fish fish : fishies2) {
			fishies.add(fish);
		}
	}

	List<Fish> getFishies(){
		ActSpearoStatsMain activity = (ActSpearoStatsMain) getActivity();
		List<Fish> loadedFish = ((SpearoApplication) activity.getApplication()).getFishies();
		if (loadedFish == null || loadedFish.isEmpty()){
			loadedFish = new Database(activity).fetchFishFromDb();
			((SpearoApplication)activity.getApplication()).getFishies().clear();
			((SpearoApplication)activity.getApplication()).getFishies().addAll(loadedFish);
		}
		
		return loadedFish;
	}

	/**
	 * Called from the alert prompt that confirms the fish catches before persistence.
	 *
	 * @param fishCatch
	 */
	public void deleteTransientFishCatch(FishCatch fishCatch) {
		sessionCatchesList.remove(fishCatch);
		sessionCatchesAdapter.notifyDataSetChanged();
		updateCheckoutButtonText();

		addSessionCatchesImages();
	}

	/**
	 * Every time a new fish is added the checkout button will indicate the number of fish added.
	 */
	void updateCheckoutButtonText() {
		int fishCatchesSize = sessionCatchesList.size();
		if (fishCatchesSize == 0){
			buttonCheckout.setText(getResources().getString(R.string.fola));
			return;
		}
     	String fishText = fishCatchesSize == 1 ? getResources().getString(R.string.fish) : getResources().getString(R.string.fishes);
		buttonCheckout.setText(getResources().getString(R.string.save_session) + Constants.SPACE + fishCatchesSize + Constants.SPACE + fishText);
	}

	void setInScrollViewTouchListener(){
		 inScrollTouchListener = new View.OnTouchListener() {

	    	    @Override
	    	    public boolean onTouch(View v, MotionEvent event) {
	    	        int action = event.getAction();
	    	        switch (action) {
	    	           case MotionEvent.ACTION_DOWN:

						case MotionEvent.ACTION_MOVE:
							// Disallow ScrollView to intercept touch events.
	    	        	   ((ActSpearoStatsMain)getActivity()).interceptEvents(true);
	    	                // Disable touch on transparent view
	    	                return false;

	    	           case MotionEvent.ACTION_UP:
	    	                // Allow ScrollView to intercept touch events.
	    	        	   ((ActSpearoStatsMain)getActivity()).interceptEvents(false);
	    	                return true;

						default:
	    	                return true;
	    	        }   
	    	    }
	    	};
	    	transparentImageViewOverMap.setOnTouchListener(inScrollTouchListener);
	 }

	void collapseAppbar(){
		((ActSpearoStatsMain)getActivity()).collapseAppbar();
	}

	public void collapseAppbarIfNeeded() {
		if (POSITION_SHOW_SESSIONS != sessionsFlipper.getDisplayedChild()){
			collapseAppbar();
		}
	}
	
	void clearAndGoToSessions() {
		clearCatchViews();
        clearSessionViews();
    	goToSessionsChild();
	}

	public static FrgFishingSessions init(int val) {
		FrgFishingSessions truitonList = new FrgFishingSessions();
        Bundle args = new Bundle();
        args.putInt("val", val);
        truitonList.setArguments(args);
        return truitonList;
    }
	
	 private void placeAds() {
	        adRequest = new AdRequest.Builder().build();
	    }

//	public void editSession(FishingSession item) {
//		clearCatchViews();
//		clearSessionViews();
//		sessionId = item.getFishingSessionId();
//		sessionCatchesList.addAll(item.getFishCatches());
//		sessionCatchesAdapter.notifyDataSetChanged();
//		sessionMillis = item.getFishingDate();
//		location = new LatLng(item.getLatitude(), item.getLongitude());
//		updateCheckoutButtonText();
//		collapseAppbar();
//		goToNewSessionChild();
//		AlertDialogHelper.alertDialogWithPositive(getActivity(), getResources().getString(android.R.string.ok), null, getResources().getString(R.string.editing_session_alert));
//	}

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


	/**
	 * ALERT DIALOGS
	 *
	 *
	 */

	/**
	 * User confirms his bad luck, when no catches.
	 */
	void confirmFolaAlertDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_fola_alert_dialog, null);
		dialogBuilder.setView(dialogView);

		AlertDialog alertDialog = dialogBuilder.create();

		Button positiveButton = dialogView.findViewById(R.id.alertConfirmButton);
		positiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				saveSessionActions();

			}
		});

		Button negativeButton = dialogView.findViewById(R.id.alertCloseButton);
		negativeButton.setText(getResources().getString(R.string.close));
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});

		alertDialog.show();

	}

	/**
	 * Before persisting a session, the user has to confirm the catches he entered.
	 * The width and height of alert window is fixed.
	 */
	void confirmFishCatchesAlertDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_fish_catches_alert_dialog, null);

		Rect displayRectangle = new Rect();
		Window window = getActivity().getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

		dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
		dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.6f));
		dialogBuilder.setView(dialogView);

		ListView sessionCatchesListView = (ListView) dialogView.findViewById(R.id.listViewSessionCatches);
		sessionCatchesListView.setAdapter(sessionCatchesAdapter);
//		dialogBuilder.setPositiveButton(
//				getResources().getString(R.string.confirm),
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int id) {
//						if (sessionCatchesList.isEmpty()) {
//							confirmFolaAlertDialog();
//							return;
//						}
//						alertSessionImage();
//					}
//				});
//
//
//		dialogBuilder
//				.setNegativeButton(
//						getResources().getString(R.string.close),
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog, int id) {
//								clearCatchViews();
//							}
//
//						});

		AlertDialog alertDialog = dialogBuilder.create();
		Button positiveButton = dialogView.findViewById(R.id.alertConfirmButton);
		positiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				if (sessionCatchesList.isEmpty()) {
					confirmFolaAlertDialog();
					return;
				}
				alertSessionImage();

			}
		});

		Button negativeButton = dialogView.findViewById(R.id.alertCloseButton);
		negativeButton.setText(getResources().getString(R.string.close));
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				//clearCatchViews();

			}
		});



		alertDialog.show();
	}

	/**
	 * Confirm the return to sessions view.
	 */
	void confirmReturnAlertDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				clearAndGoToSessions();
			}
		})
				.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}

				})
				.setMessage(getResources().getString(R.string.cancel_session_entry_alert));

		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
	}

	/**
	 * User selects the date he went fishing in milliseconds.
	 */
	void beginDateAlertDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_date_alert_dialog, null);
		dialogBuilder.setView(dialogView);
		DatePicker sessionPicker = (DatePicker) dialogView.findViewById(R.id.sessionDatePicker);
		TextView sessionDateTextView = (TextView) dialogView.findViewById(R.id.sessionDateText);
		sessionDateTextView.setText(getResources().getString(R.string.session_date));



		dialogBuilder
//				.
//				setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//
//			}
//		}).setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				clearAndGoToSessions();
//			}
//		})
				.setCancelable(false);

		AlertDialog alertDialog = dialogBuilder.create();
		Button positiveButton = dialogView.findViewById(R.id.alertConfirmButton);
		positiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				int day = sessionPicker.getDayOfMonth();
				int month = sessionPicker.getMonth();
				int year = sessionPicker.getYear();
				calendar = Calendar.getInstance();
				calendar.set(year, month, day);
				sessionMillis = calendar.getTime().getTime();
				beginWindInteraction();
			}
		});

		Button negativeButton = dialogView.findViewById(R.id.alertCloseButton);
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				clearAndGoToSessions();
			}
		});

		alertDialog.show();
	}

	/**
	 * User selects the wind direction and volume.
	 */
	void beginWindInteraction() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.custom_wind_alert_dialog, null);
		dialogBuilder.setView(dialogView);
		Spinner spinnerWind = (Spinner) dialogView.findViewById(R.id.spinner_wind);
		String[] windsArray = getResources().getStringArray(R.array.winds);
		final List<Wind> winds = Wind.forSpinner();
		WindSpinnerAdapter adapter = new WindSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, windsArray, winds);
		spinnerWind.setAdapter(adapter);

		Spinner spinnerWindVolume = (Spinner) dialogView.findViewById(R.id.spinner_wind_volume);
		String[] windsVolArray = getResources().getStringArray(R.array.windVolumes);
		final List<WindVolume> windVols = WindVolume.forSpinner();
		WindVolumeSpinnerAdapter volAdapter = new WindVolumeSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, windsVolArray, windVols);
		spinnerWindVolume.setAdapter(volAdapter);

		spinnerWind.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				wind = Wind.ofPosition(winds.get(position).getPosition());

				if (Wind.NO_WIND.equals(wind)){
					spinnerWindVolume.setVisibility(View.INVISIBLE);
					windVolume = WindVolume.NOT_KNOWN;
				}else{
					spinnerWindVolume.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		spinnerWindVolume.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				windVolume = WindVolume.ofPosition(windVols.get(position).getPosition());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});

		dialogBuilder
//				.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				beginMapInteraction();
//			}
//		}).setNegativeButton(getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				clearAndGoToSessions();
//			}
//		})
				.setCancelable(false);

		AlertDialog alertDialog = dialogBuilder.create();

		Button positiveButton = dialogView.findViewById(R.id.alertConfirmButton);
		positiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				beginMapInteraction();
			}
		});

		Button negativeButton = dialogView.findViewById(R.id.alertCloseButton);
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				clearAndGoToSessions();

			}
		});


		alertDialog.show();
	}

	/**
	 * Prompt for image insertion for session.
	 */
	void alertSessionImage() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
		dialogBuilder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				selectImage();
			}
		})
				.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						saveSessionActions();
					}

				})
				.setMessage(getResources().getString(R.string.session_image));

		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
	}

}
