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

    String imagePathUri;

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

    	imagePathUri = app_preferences.getString(Constants.PROFILE_PIC_URI, null);
    	if (imagePathUri != null){
    		return null;
    	}

    	String imgByteArray = app_preferences.getString(Constants.PROFILE_PIC_BYTES, null);
    	if (imgByteArray != null) {
            array = Base64.decode(imgByteArray, Base64.NO_WRAP);
        }
        
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    	if (prgBar != null){
    		prgBar.setVisibility(View.GONE);
    	}

    	//uri from sp
    	if (imagePathUri != null){
            Uri imageUri = Uri.parse(imagePathUri);
            img.setImageURI(imageUri);

            if (img.getDrawable() == null){//image of uri is deleted.
                Uri defaultImageUri = Uri.parse("android.resource://gr.liakos.spearo/"+R.drawable.spear2_edited); // use this default image uri if user didn't saved any image to sharedprefrence .
                img.setImageURI(defaultImageUri);
            }

            return;
        }

    	//no uri, no byte array
    	if (array == null ){
    		Uri defaultImageUri = Uri.parse("android.resource://gr.liakos.spearo/"+R.drawable.spear2_edited); // use this default image uri if user didn't saved any image to sharedprefrence .
   		 	img.setImageURI(defaultImageUri);
    		return;
    	}

    	//byte array set
    	Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
    	img.setImageBitmap(Bitmap.createScaledBitmap(bmp, img.getWidth(), img.getHeight(), false));

    }
}

