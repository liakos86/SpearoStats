package gr.liakos.spearo;

import gr.liakos.spearo.application.BaseFrgActivityWithBottomButtons;
import gr.liakos.spearo.application.NonSwipeableViewPager;
import gr.liakos.spearo.async.AsyncLoadProfilePic;
import gr.liakos.spearo.billing.BillingHelper;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.fragment.FrgFishingStats;
import gr.liakos.spearo.fragment.FrgFishingStatsGlobal;
import gr.liakos.spearo.fragment.FrgMyPlaces;
import gr.liakos.spearo.model.adapter.MyPagerAdapter;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


//import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ActSpearoStatsMain 
extends BaseFrgActivityWithBottomButtons
implements LocationListener {

    private Menu menu;
    
    /**
     * A NonSwipeableViewPager that does not allow swiping
     */
    NonSwipeableViewPager mPager;
    
    AppBarLayout mAppBarLayout;

    LocationManager locationManager;

    LatLng currentLatLng;

    boolean mapPermissionGiven;

    /**
     * The total size of the pager objects
     */
    private static final int PAGER_SIZE = 4;
	private BillingHelper billingHelper;


	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupPager();
        setProfilePicSource();
        setupPurchaseListener();
        setupFabSettings();
        setupAppBarScrolling();
        fixAppBarTextViews();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkMapPermissions();
    }

	@RequiresApi(api = Build.VERSION_CODES.M)
	void checkMapPermissions(){
		if ((ContextCompat.checkSelfPermission(
				this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED) &&
				(ContextCompat.checkSelfPermission(
						this, Manifest.permission.ACCESS_FINE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED)){
			mapPermissionGiven = true;
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
			//You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
		}else{
			requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
					Constants.PERMISSIONS_MAP_CODE);
		}
	}
    
	@Override
    protected void onResume() {
    	super.onResume();
    	((SpearoApplication)getApplication()).checkForMongoUpload();
    }

	void setupAppBarScrolling() {
		mAppBarLayout = findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    hideOption(R.id.action_info);
                }
            }
        });
        
        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nsv.setFillViewport (true);
        nsv.setNestedScrollingEnabled(true);
	}

	void setupFabSettings() {
		   FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fab_settings);
		        fabSettings.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	startSettings();
		            }
		        });
	}

	void setProfilePicSource() {
		ImageView changeProfilePic = (ImageView) findViewById(R.id.expandedImage);
		if (SpearoUtils.customProfPicIsSet(getApplication())){
			new AsyncLoadProfilePic(getApplication(), changeProfilePic, null).execute();
		}else{
			Uri defaultImageUri = Uri.parse("android.resource://gr.liakos.spearo/"+R.drawable.spear2_edited); // use this default image uri if user didn't saved any image to sharedprefrence .
			changeProfilePic.setImageURI(defaultImageUri);
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        hideOption(R.id.action_info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	startSettings();
            return true;
        } else if (id == R.id.action_info) {
        	alertFragmentInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void alertFragmentInfo() {
		int currentItem = mPager.getCurrentItem();
		String text = Constants.EMPTY;
		if (currentItem ==0){
			text= getResources().getString(R.string.frg_fishing_sessions);
		}else if (currentItem == 1){
			text = getResources().getString(R.string.frg_personal_stats);
		}else if (currentItem == 2){
			text = getResources().getString(R.string.frg_global_stats);
		}else if (currentItem == 3){
			text = getResources().getString(R.string.frg_my_places);
		}
		
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	dialog.cancel();
            }

        })
        .setMessage(text);
		
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();
	}

	private void hideOption(int id) {
    	if (menu == null){
    		return;
    	}
    	
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
    	if (menu == null){
    		return;
    	}
    	
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }
    
    public static int getPagerSize() {
		return PAGER_SIZE;
	}
    
    void startSettings() {
        Intent intent = new Intent(this, ActSettings.class);
        startActivity(intent);
        finish();
    }
    
    /**
     * Initializes the pager, sets adapter and listener
     * Sets the bottom buttons.
     */
    @SuppressWarnings("deprecation")
	private void setupPager() {
        mPager = findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getPagerSize()));
        setBottomButtons(mPager);
        setSelectedBottomButton(bottomButtons, 0);

        mPager.setOnPageChangeListener(new NonSwipeableViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                ((SpearoApplication) getApplication()).setPosition(position);
                mPager.setCurrentItem(position);
                setSelectedBottomButton(bottomButtons, position);

                Fragment fragment = getActiveFragment(getSupportFragmentManager(), position);
                if (fragment instanceof FrgFishingSessions) {
                    ((FrgFishingSessions) fragment).collapseAppbarIfNeeded();
                    ((FrgFishingSessions) fragment).reloadFishiesOnNewRecord();
                }else if (fragment instanceof FrgFishingStatsGlobal) {
                }else if (fragment instanceof FrgFishingStats) {
                   ((FrgFishingStats) fragment).fixAverageStatsIfNeeded();
                }else if (fragment instanceof FrgMyPlaces){
                	collapseAppbar();
                	FrgMyPlaces frgMyPlaces = (FrgMyPlaces) fragment;
					frgMyPlaces.redrawMarkersIfNeeded();
					frgMyPlaces.hideKeyboardFrom();
					frgMyPlaces.zoomInLocation(currentLatLng);
                }
                
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }
    
    public Fragment getActiveFragment(FragmentManager fragmentManager, int position) {
        final String name = makeFragmentName(mPager.getId(), position);
        final Fragment fragmentByTag = fragmentManager.findFragmentByTag(name);
        if (fragmentByTag == null) {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            fragmentManager.dump("", null, new PrintWriter(outputStream, true), null);
        }
        return fragmentByTag;
    }
    
    private String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }
    
    public void collapseAppbar() {
    	mAppBarLayout.setExpanded(false);
	}

	/**
     * Sets the state of the pressed button to 'selected'
     *
     * @param bottomButtons
     * @param position
     */
    private void setSelectedBottomButton(Map<Integer, Integer> bottomButtons, int position) {
        for (int key = 0; key < bottomButtons.size(); key++) {
            LinearLayout btn = findViewById(bottomButtons.get(key));
            btn.setSelected(key == position);
        }
    }


    public void interceptEvents(boolean doIntercept){
    	NestedScrollView nsv = findViewById(R.id.nestedScrollView);
    	nsv.requestDisallowInterceptTouchEvent(doIntercept);
    }

	
    public void fixCatchesNum(int total){
    	TextView textCatchesNum = (TextView) findViewById(R.id.textCatchesNum);
    	textCatchesNum.setText(String.valueOf(total));
    }
    
    public void fixAppBarTextViews() {
		CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
		User user = ((SpearoApplication)getApplication()).getUser();// SpearoUtils.getUserFromSp(getApplicationContext(), sp);// (SpearoApplication) getApplication();
		String username = user.getUsername();
		ctl.setTitle(username);
	}
    
    
    /**
	 * The response from app-store is not handled, so we do it manually.
	 */
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
//	     if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
//	         super.onActivityResult(requestCode, resultCode, data);
//	     }
	     
	     if (requestCode == Constants.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
	            if (data == null) {
	            	//new SpearoUtils(this).snack(getResources().getString(R.string.error));
	                return;
	            }
	           
	            Uri selectedImage = data.getData();
				if (selectedImage == null){
					//new SpearoUtils(this).snack(findViewById(R.id.parent_layout), "Wrong path or content type");
					return;
				}
	            
				sendBytes(selectedImage);
	        }
	 }

	public void snack(int message) {
		String snack = getResources().getString(message);
		Snackbar.make(this.findViewById(R.id.parent_main), snack, Snackbar.LENGTH_LONG)
        .setAction("Action", null).show();
	}
	
	@SuppressLint("MissingPermission")
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
         int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     switch (requestCode) {
         case Constants.PERMISSION_STORAGE:
             // If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0 &&
                     grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            	 openImageIntent();
             }  else {
            	 Snackbar.make(this.findViewById(R.id.parent_main), getResources().getText(R.string.pic_cannot_be_added), Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show();  
            	 sendBytes(null);
            }
             return;

             case Constants.PERMISSIONS_MAP_CODE:

				 if (grantResults.length > 0 &&
						 grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
					 mapPermissionGiven = true;
				 }

				 return;
         }
     }
	
	public void openImageIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
		
	}
	
	
	@SuppressLint("NewApi")
	public void selectImage() {
		if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
			openImageIntent();
        } else {
        	requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_DOCUMENTS },
                    Constants.PERMISSION_STORAGE);
        }
	}
	
	
		void sendBytes(Uri uri){
			FrgFishingSessions frgSessions = (FrgFishingSessions) getActiveFragment(getSupportFragmentManager(), 0);
			String saveThis = null;
			if (uri != null){
				byte[] result = storeImageByteArray(uri);
				saveThis = Base64.encodeToString(result, Base64.NO_WRAP);
			}
			frgSessions.setSessionBytesAndSave(saveThis);
		}

		private byte[] storeImageByteArray(Uri uri) {
	   	  	
	        byte[] data = null;
	        try {
	            ContentResolver cr = getBaseContext().getContentResolver();
	            InputStream inputStream = cr.openInputStream(uri);
	            Options options = new Options();
	            options.inScaled = false;
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
	            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	            data = baos.toByteArray();
	            scaledBitmap.recycle();
	            return data;
	        } catch (FileNotFoundException e) {
	            return null;
	        }
		}
		
	

	/**
     * Retrive google licence key from Google Play Console:
     * Development tools --> Services and APIs
     */
	public void queryInventoryForPremium(AsyncListener listener) {

		billingHelper.queryInventory(listener);
	}
	
	void setupPurchaseListener() {
		billingHelper = new BillingHelper(this);
	}


	public void purchase(View view) {
		billingHelper.purchase();
	}

	//public void consume(View view){
	//	billingHelper.consume(view);
	//}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(billingHelper!=null){
			billingHelper.endConnection();
		}
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		locationManager.removeUpdates(this);
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		currentLatLng = latLng;
	}

	@Override
	public void onProviderEnabled(@NonNull String provider) {

	}

	@Override
	public void onProviderDisabled(@NonNull String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	public LatLng getCurrentLatLng() {
		return currentLatLng;
	}

	public void setCurrentLatLng(LatLng currentLatLng) {
		this.currentLatLng = currentLatLng;
	}

	public boolean isMapPermissionGiven() {
		return mapPermissionGiven;
	}

	public void setMapPermissionGiven(boolean mapPermissionGiven) {
		this.mapPermissionGiven = mapPermissionGiven;
	}

}
	