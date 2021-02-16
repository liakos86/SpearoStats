package gr.liakos.spearo.async;

import gr.liakos.spearo.R;
import gr.liakos.spearo.util.Constants;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AsyncLoadProfilePic extends AsyncTask<Void, Void, Void> {
    Application application;
    ImageView img;
    String errorMsg;
    ProgressBar prgBar;
    
    byte[] array;

    public AsyncLoadProfilePic(Application application, ImageView img, ProgressBar prgBar) {
        this.application = application;
        this.img = img;
        this.prgBar = prgBar;
    }

    protected void onPreExecute() {
    	if (prgBar != null){
    		prgBar.setVisibility(View.VISIBLE);
    	}
    }

    @Override
    protected Void doInBackground(Void... unused) {
    	SharedPreferences app_preferences = application.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
   	  	String stringArray = app_preferences.getString(Constants.PROFILE_PIC, null);
    	if (stringArray == null){
    		return null;
    	}
   	  	
    	array = Base64.decode(stringArray, Base64.NO_WRAP);
        
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    	if (prgBar != null){
    		prgBar.setVisibility(View.GONE);
    	}
    	
    	
    	if (array == null){
    		Uri defaultImageUri = Uri.parse("android.resource://gr.liakos.spearo/"+R.drawable.spear2_edited); // use this default image uri if user didn't saved any image to sharedprefrence .
   		 	img.setImageURI(defaultImageUri);
    		return;
    	}
    	
    	Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
    	img.setImageBitmap(Bitmap.createScaledBitmap(bmp, img.getWidth(), img.getHeight(), false));

    	
    }
}

