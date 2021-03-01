package gr.liakos.spearo.util;

import java.util.Locale;

import gr.liakos.spearo.R;
import gr.liakos.spearo.application.NumberPicker;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.User;

import org.bson.types.ObjectId;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
//import android.support.design.widget.Snackbar;
import androidx.core.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;


public class SpearoUtils {

	Resources resources;
	String pkgName;
	Context ctx;

	public SpearoUtils(Context context){
		this.ctx = context;
		this.resources = context.getResources();
		this.pkgName = context.getPackageName();
	}

	 public static String getStringResourceByName(Context context, String aString, Object... obj) {
	        String packageName = context.getPackageName();
	        int resId = context.getResources().getIdentifier(aString, "string", packageName);
	        return context.getString(resId, obj);
	 }
	 
	 public static void hideSoftKeyboard(Activity activity) {
		 	if (activity.getCurrentFocus() == null){
		 		return;
		 	}
		 
		    InputMethodManager inputMethodManager = 
		        (InputMethodManager) activity.getSystemService(
		            Activity.INPUT_METHOD_SERVICE);
		    inputMethodManager.hideSoftInputFromWindow(
		        activity.getCurrentFocus().getWindowToken(), 0);
	}
	 
	 public Bitmap createDrawableFromView(Activity activity, View view) {
			DisplayMetrics displayMetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
			view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
			view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
			view.buildDrawingCache();
			Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
	 
			Canvas canvas = new Canvas(bitmap);
			view.draw(canvas);
	 
			return bitmap;
	}

	public Drawable getGridDrawableFromFish(Fish fish){
		try {
			return getDrawableFromString( fish.getImageName() + "_grid");
		}catch (Exception e){
			return getDrawableFromFish(fish);
		}
	}
	 
	 public Drawable getDrawableFromFish(Fish fish){
		 try {
	    	return getDrawableFromString( fish.getImageName());
		 }catch (Exception e){
			 return ResourcesCompat.getDrawable(resources, R.drawable.icon_missing, null);
		 }
	  }
	 
	 public Drawable getDrawableFromString( String img){
	    	final int resourceId = resources.getIdentifier(img, "drawable", 
	    			pkgName);
	    	return ResourcesCompat.getDrawable(resources, resourceId, null);
	  }
	 
	 /**
	     * isOnline - Check if there is a NetworkConnection
	     * @return boolean
	     */
	    @SuppressWarnings("deprecation")
		public static boolean isOnline(Context ctx) {
	        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(ctx.CONNECTIVITY_SERVICE);

	        try {
	            boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
	                    .isConnectedOrConnecting();
	            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
	                    .isConnectedOrConnecting();
	            return isWifi || is3g;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	static String num2string(Integer mostCommonHour) {
		if (mostCommonHour < 10){
			return "0" + mostCommonHour;
		}
		return String.valueOf(mostCommonHour);
	}
	
	public static String catchHourFor(Integer catchHour) {
		String hour = num2string(catchHour);
		String nextHour = num2string(catchHour + 1); 
		return hour + ":00" + Constants.MINUS + nextHour + ":00";
	}
	
	public static User getUserFromSp(Context ctx, SharedPreferences sp){
		String username = sp.getString(Constants.USERNAME, null);
		if (username == null){
			username = ctx.getResources().getString(R.string.defaultUsername);
		}
		User user = new User(username);
		String spMongoId = sp.getString(Constants.MONGO_ID, null);
		ObjectId objectId = spMongoId == null ? null : new ObjectId(spMongoId);
		user.set_id(objectId);
		return user;
	}

	public void setupMetricWeightPicker(NumberPicker pickerWeight) {
		SharedPreferences app_preferences = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		if (isMetric){
			pickerWeight.setStep(0.25f);
			pickerWeight.setDescriptionText(resources.getString(R.string.weightMetric));
			pickerWeight.setMaxValue(200);
		}else{
			pickerWeight.setStep(0.5f);
			pickerWeight.setDescriptionText(resources.getString(R.string.weightImperial)); 
			pickerWeight.setMaxValue(400);
		}
		
	}

	public void setupMetricDepthPicker(NumberPicker pickerDepth) {
		SharedPreferences app_preferences = ctx.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		if (isMetric){
			pickerDepth.setStep(0.5f);
			pickerDepth.setDescriptionText(resources.getString(R.string.depthMetric));
			pickerDepth.setMaxValue(60);
		}else{
			pickerDepth.setStep(1f);
			pickerDepth.setDescriptionText(resources.getString(R.string.depthImperial)); 
			pickerDepth.setMaxValue(200);
		}
		
	}
	
	public void snack(View view, int msg){
		Snackbar.make(view, resources.getString(msg), Snackbar.LENGTH_LONG).setAction("Action", null).show();
	}
	
	public void snack(View view, String msg){
		Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
	}

	public static String getLocalizedName(Context ctx, Fish fish) {
		Resources res = getGreekResources(ctx);
		if (res == null){
			return Constants.EMPTY;
		}
		
		int resId = res.getIdentifier(Fish.commonNamePattern(fish), "string", ctx.getPackageName());
		
		return res.getString(resId);
	}
	
	public static boolean customProfPicIsSet(Application app){
		SharedPreferences app_preferences = app.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		return app_preferences.getString(Constants.PROFILE_PIC_BYTES, null) != null ||
				app_preferences.getString(Constants.PROFILE_PIC_URI, null) != null;
	}
	
	static Resources getGreekResources(Context mContext) {
		String closeLocale = mContext.getResources().getString(R.string.close);
		if (!closeLocale.equals("Close")){
			 return null;
		}
		
		Configuration conf = mContext.getResources().getConfiguration();
	    conf = new Configuration(conf);
	    conf.setLocale(new Locale("el"));
	    Context localizedContext = mContext.createConfigurationContext(conf);
	    Resources resources = localizedContext.getResources();
	    return resources;
	}
	
}
