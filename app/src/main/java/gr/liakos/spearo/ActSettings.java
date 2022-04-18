package gr.liakos.spearo;

import gr.liakos.spearo.async.AsyncLoadProfilePic;
import gr.liakos.spearo.async.AsyncSaveFishRequestMongo;
import gr.liakos.spearo.def.AsyncSaveUserListener;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;
import gr.liakos.spearo.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.material.snackbar.Snackbar;

public class ActSettings
extends Activity implements AsyncSaveUserListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_settings);
		
		setProfilePicSource(null);
		setProfilePicChangeListener();
		setupCheckBox();
		
		setupUsernameButton();
		setupIcons8Link();
		setupFishRequestButton();
		
		setupEmailTextView();

		setupLikeButton();

		getInsights();
	}

	private void setupLikeButton() {
		Button likeButton = findViewById(R.id.button_fb_like);
		likeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(Constants.FACEBOOK_URL); // missing 'http://' will cause crashed
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);

			}
		});
	}

	void getPageToken(){
//		/* make the API call */
//		new GraphRequest(
//				AccessToken.getCurrentAccessToken(),
//				"/"+ Constants.FACEBOOK_APP_ID +"?fields=access_token",
//				null,
//				HttpMethod.GET,
//				new GraphRequest.Callback() {
//					public void onCompleted(GraphResponse response) {
//						Toast.makeText(getApplicationContext(), response.getRawResponse(), Toast.LENGTH_LONG).show();
//						try {
//							pageToken = response.getJSONObject().getString("access_token");
//						} catch (Exception e) {
//							Toast.makeText(getApplicationContext(), "TOKEN ERROR", Toast.LENGTH_LONG).show();
//						}
//						getInsights();
//					}
//				}
//		).executeAsync();
	}

	void getInsights(){
		AccessToken accessToken = new AccessToken(Constants.PAGE_ACCESS_TOKEN, Constants.FACEBOOK_APP_ID, "10158556348971234", null, null, null, null, null, null, null);

		new GraphRequest(
				accessToken,
				"/" + Constants.FACEBOOK_APP_ID +"/insights?metric=page_fans",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
						String fans= "";
						try {
							 fans = response.getJSONObject().getJSONArray("data").getJSONObject(0).getJSONArray("values").getJSONObject(0).getString("value");
							updateLikeButtonText(fans);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
		).executeAsync();
	}

	private void updateLikeButtonText(String fans) {
		Button likeButton = findViewById(R.id.button_fb_like);
		likeButton.setText("(" + fans + ") Like");
	}

	void setupEmailTextView() {
		TextView email = findViewById(R.id.text_email_us);
		email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setData(Uri.parse("mailto:"));
				emailIntent.setType("text/plain");
				emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{getResources().getString(R.string.email_spearo)});
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_subject));
				emailIntent.putExtra(Intent.EXTRA_TEXT   , getResources().getString(R.string.email_text));
				
				startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.email_chooser_title)));
				
			}
		});
		
	}

	void setupFishRequestButton() {
        Button buttonRequest = findViewById(R.id.button_request_fish);
        buttonRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				closeKeyboard();
				EditText requestEt = (EditText) findViewById(R.id.editTextRequestFish);
		        String searchString = requestEt.getText().toString();
		        if (searchString.length() < 5 || searchString.length() > 20){
		        	snack(getResources().getString(R.string.request_length));
		        	return;
		        }

		        String matchingFishName = fishNameContaining(searchString);
		        if (matchingFishName != null){
		        	snack(getResources().getString(R.string.fish_already_exists, matchingFishName));
		        	return;
				}
		        
		        uploadRequest(searchString);
		        snack(getResources().getString(R.string.request_sent));
		        hideRequestLayout();
			}
		});
		
	}

	String fishNameContaining(String fishName) {
		List<Fish> allFish = ((SpearoApplication) getApplication()).getFishies();
		return StringUtils.fishNameContaining(allFish, fishName);
	}

	void closeKeyboard() {
		SpearoUtils.hideSoftKeyboard(this);
	}

	void hideRequestLayout() {
		LinearLayout layoutRequest = (LinearLayout) findViewById(R.id.requestFishLayout);
		layoutRequest.setVisibility(View.INVISIBLE);
	}

	void uploadRequest(String fishName) {
		new AsyncSaveFishRequestMongo((SpearoApplication) getApplication(), fishName).execute();
	}

	void setupIcons8Link() {
		TextView icons8text = ((TextView) findViewById(R.id.icons8text));
        icons8text.setText(getResources().getString(R.string.icons8));
        icons8text.setMovementMethod(LinkMovementMethod.getInstance());
	}

	void setupUsernameButton() {
        EditText username = (EditText) findViewById(R.id.editTextUsername);
        String usernameSp = ((SpearoApplication)getApplication()).getUser().getUsername();
		username.setText(usernameSp);
        username.setSelection(usernameSp.length() );
        Button returnToMain = (Button) findViewById(R.id.button_return_to_main);
        returnToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startMain();
			}
		});
        
		Button saveUsernameButton = (Button) findViewById(R.id.button_save_username);
		saveUsernameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!validateUsername()){
					return;
				}
				saveNewUsername();
			}
		});
	}

	void saveNewUsername() {
		closeKeyboard();
		
		EditText username = findViewById(R.id.editTextUsername);
		SharedPreferences app_preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		User userFromSp = SpearoUtils.getUserFromSp(getApplicationContext(), app_preferences);
		if (!userFromSp.getUsername().equals(username.getText().toString().trim())){
			if (userFromSp.get_id() !=null){
				updatePrefsUsername(username.getText().toString().trim());
				snack(getResources().getString(R.string.success_username));
			}else{
				showProgressBar(true);
				User user = new User(username.getText().toString().trim());
				((SpearoApplication)getApplication()).saveUser(user, this);
			}
		}else{
			snack(getResources().getString(R.string.success_username));
		}
		
	}

	void saveNewMetricValue() {
		SharedPreferences app_preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		CheckBox metricImperialCheckBox = (CheckBox) findViewById(R.id.checkbox_metric_imperial);
		Editor editor = app_preferences.edit();
		editor.putBoolean(Constants.IMPERIAL, !metricImperialCheckBox.isChecked());
		editor.commit();
	}
	
	void updatePrefsUsername(String trim) {
		SharedPreferences app_preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        EditText username = (EditText) findViewById(R.id.editTextUsername);
		Editor editor = app_preferences.edit();
		editor.putString(Constants.USERNAME, username.getText().toString());
		editor.commit();
	}

	boolean validateUsername(){
		EditText username = findViewById(R.id.editTextUsername);
		String usr = username.getText().toString().trim();
		
		if (usr.length() > 14 || usr.length() < 3 ){
			Snackbar.make(findViewById(R.id.parent_layout), getResources().getString(R.string.invalid_length), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
			return false;
		}
		return true;
	}

	void setupCheckBox() {
		CheckBox metricImperialCheckBox = (CheckBox) findViewById(R.id.checkbox_metric_imperial);
		SharedPreferences app_preferences = this.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		metricImperialCheckBox.setChecked(isMetric);
        String metricLocale = isMetric ? getResources().getString(R.string.Metric) : getResources().getString(R.string.Imperial);
		metricImperialCheckBox.setText(metricLocale);
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        String metricLocale = isChecked ? getResources().getString(R.string.Metric) : getResources().getString(R.string.Imperial);
				updateCheckboxText(metricLocale);
				saveNewMetricValue();
			}
		};
		metricImperialCheckBox.setOnCheckedChangeListener(listener);
	}

	void updateCheckboxText(String metricLocale) {
		CheckBox metricImperialCheckBox = findViewById(R.id.checkbox_metric_imperial);
		metricImperialCheckBox.setText(metricLocale);
	}

	void setProfilePicSource(Uri selectedImage) {
		ImageView changeProfilePic = findViewById(R.id.imageViewChangePic);
		ProgressBar prg = findViewById(R.id.progressBarProfilePic);
		if (selectedImage == null){
			if (SpearoUtils.customProfPicIsSet(getApplication())){
				new AsyncLoadProfilePic(getApplication(), changeProfilePic, prg).execute();
			}else{
				Uri defaultImageUri = Uri.parse("android.resource://gr.liakos.spearo/"+R.drawable.spear2_edited); // use this default image uri if user didn't saved any image to sharedprefrence .
				changeProfilePic.setImageURI(defaultImageUri);
			}
			return;
		}

		Glide.with(getApplicationContext()).load(selectedImage).into(changeProfilePic);

	  //changeProfilePic.setImageURI(selectedImage);
	}

  void setProfilePicChangeListener() {
	  ImageView changeProfilePicImg = (ImageView) findViewById(R.id.imageViewChangePic);
		OnClickListener selectListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			selectImage();
		}
	};
		changeProfilePicImg.setOnClickListener(selectListener);
	}

	@SuppressLint("NewApi")
	void selectImage() {
		
		if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
			openImageIntent();
        } else {
        	requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_DOCUMENTS },
                    Constants.PERMISSION_STORAGE);
        }
		
	}
	
	private void openImageIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
		
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,
         int[] grantResults) {
     switch (requestCode) {
         case Constants.PERMISSION_STORAGE:
             // If request is cancelled, the result arrays are empty.
             if (grantResults.length > 0 &&
                     grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            	 openImageIntent();
             }  else {
            	 Snackbar.make(this.findViewById(R.id.parent_layout), getResources().getText(R.string.prof_pic_cannot_change), Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show();             }
             return;
         }
     }
	
	void startMain() {
        Intent intent = new Intent(this, ActSpearoStatsMain.class);
        startActivity(intent);
        finish();
    }
	
	void snack(String msg){
		Snackbar.make(this.findViewById(R.id.parent_layout), msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
	}
	
	 @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	super.onActivityResult(requestCode, resultCode, data);
	    	
	        if (requestCode == Constants.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
	            if (data == null) {
	            	snack(getResources().getString(R.string.error));
	                return;
	            }
	           
	            Uri selectedImage = data.getData();
				if (selectedImage == null){
					new SpearoUtils(this).snack(findViewById(R.id.parent_layout), "Wrong path or content type");
					return;
				}

				final int takeFlags = data.getFlags()
						& (Intent.FLAG_GRANT_READ_URI_PERMISSION
						| Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

				getContentResolver().takePersistableUriPermission(selectedImage, takeFlags);
	            
	            setProfilePicSource(selectedImage);
	            ImageView changeProfilePicImg = (ImageView) findViewById(R.id.imageViewChangePic);
	            new AsyncStoreProfilePic(selectedImage, changeProfilePicImg.getWidth(), changeProfilePicImg.getHeight()).execute();
	            
	        }
	    }

	@Override
		public void onAsyncError(String errorMsg) {
			snack( errorMsg);
			showProgressBar(false);
		}
	 
	 
	 @Override
		public void onUserSaved(User user) {
		 showProgressBar(false);
			SharedPreferences app_preferences = getApplication().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    	Editor edit = app_preferences.edit();
			edit.putString(Constants.USERNAME, user.getUsername());
			edit.putString(Constants.MONGO_ID, user.get_id().get().toHexString());
			edit.commit();
			((SpearoApplication)getApplication()).setUser(user);
			//startMain();
			snack(getResources().getString(R.string.success_username));
	 }

	void showProgressBar(boolean doShow){
		ProgressBar pb = (ProgressBar) findViewById(R.id.settingsProgressBar);
		Button saveAndUsername = (Button) findViewById(R.id.button_save_username);
		if (doShow){
			saveAndUsername.setVisibility(View.GONE);
			pb.setVisibility(View.VISIBLE);
		}else{
			saveAndUsername.setVisibility(View.VISIBLE);
			pb.setVisibility(View.GONE);
		}
	}
	
	
	
	class AsyncStoreProfilePic extends AsyncTask<Void, Void, byte[]>{
		
		Uri uri;
		
		int width;
		
		int height;

		String uriPath;

		public AsyncStoreProfilePic(Uri uri, int width, int height) {
			this.uri = uri;
			this.width = width;
			this.height = height;
		}
		
		@Override
		protected byte[] doInBackground(Void... arg0) {
			byte[] imgByteArray = null;

			try {
				uriPath = String.valueOf(uri);
			}catch (Exception e){
				uriPath = null;
				imgByteArray = storeImageByteArray();
			}

			return	imgByteArray;
		}
		
		@Override
		protected void onPostExecute(byte[] result) {
			if (result == null && uriPath == null){
				return;
			}
			
			SharedPreferences app_preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	   	  	Editor editor = app_preferences.edit();

	   	  	if (uriPath != null) {
				editor.putString(Constants.PROFILE_PIC_URI, uriPath);
				editor.putString(Constants.PROFILE_PIC_BYTES, null);
			}else{
				String saveThis = Base64.encodeToString(result, Base64.NO_WRAP);
				editor.putString(Constants.PROFILE_PIC_BYTES, saveThis);
				editor.putString(Constants.PROFILE_PIC_URI, null);
			}

	        editor.apply();
		}

		private byte[] storeImageByteArray() {
	        byte[] data = null;
	        try {
	            ContentResolver cr = getBaseContext().getContentResolver();
	            InputStream inputStream = cr.openInputStream(uri);
	            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
	            data = baos.toByteArray();
	            return data;
	        } catch (FileNotFoundException e) {
	            return null;
	        }
		}
		
	}
	
}
