package gr.liakos.spearo;

import gr.liakos.spearo.application.MyAcraSender;
import gr.liakos.spearo.async.AsyncLoadCommunityDataFromMongo;
import gr.liakos.spearo.async.AsyncSaveUserMongo;
import gr.liakos.spearo.def.AsyncListener;
import gr.liakos.spearo.def.AsyncSaveUserListener;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.mongo.SyncHelper;
import gr.liakos.spearo.util.ConsistencyChecker;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.FishingHelper;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import com.google.android.gms.ads.MobileAds;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

@ReportsCrashes(formKey = "",
        httpMethod = HttpSender.Method.POST,
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT},
        mode = ReportingInteractionMode.SILENT
        )
public class SpearoApplication extends Application {

    /**
     * The position of the pager
     */
    int position;

    List<Fish> fishies = new ArrayList<Fish>();
    List<FishingSession> fishingSessions = new ArrayList<FishingSession>();
    List<FishCatch> fishCatches = new ArrayList<FishCatch>();
    List<FishAverageStatistic> dbCommunityData = new ArrayList<FishAverageStatistic>();
    
	boolean loaded;
	
	boolean sessionsHaveChanged;
	
	boolean newCommunityDataRecord;
	
	User user;
	
	  @Override
	    protected void attachBaseContext(Context base) {
	        super.attachBaseContext(base);
	        
	        ACRA.init(this);
	        MyAcraSender mySender = new MyAcraSender(this);
	      ACRA.getErrorReporter().addReportSender(mySender);
	      
	    }
	  
	  /**
	   * 1. Initialize ADS sdk.
	   * 2. Initialize ACRA crash reporting.
	   * 3. Load data from db.
	   * 4. Initialize user.
	   * 5. Start async inventory query for premium.
	   * 
	   */
	@Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this);
        new AsyncLoadFishDataFromDb(this).execute();
        initializeUser();
	}
	
    void initializeUser() {
    	SharedPreferences app_preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		User user = SpearoUtils.getUserFromSp(getApplicationContext(), app_preferences);
		this.user = user;
	}

	public List<Fish> getFishies() {
        return fishies;
    }

    public void loadCommunityData(AsyncListener asyncListener){
    	new AsyncLoadCommunityDataFromMongo(this, asyncListener).execute();
    }
    
    public List<FishingSession> getDbFishingSessions(){
    	Database db = new Database(this);
    	List<FishingSession> dbFishingSessions = db.fetchFishingSessionsFromDb();
        return dbFishingSessions;
    }
    
	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public void setPosition(int position) {
        this.position = position;
    }
    
    public List<FishingSession> getFishingSessions() {
		return fishingSessions;
	}

	public List<FishCatch> getFishCatches() {
		return fishCatches;
	}

	public boolean isSessionsHaveChanged() {
		return sessionsHaveChanged;
	}

	public void setSessionsHaveChanged(boolean sessionsChanged) {
		this.sessionsHaveChanged = sessionsChanged;
	}

	/**
	 * Since initializeUser has run, this will never be null.
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<FishAverageStatistic> getDbCommunityData() {
		return dbCommunityData;
	}

	public void setDbCommunityData(List<FishAverageStatistic> dbCommunityData) {
		this.dbCommunityData = dbCommunityData;
	}
	
	public boolean isNewCommunityDataRecord() {
		return newCommunityDataRecord;
	}

	public void setNewCommunityDataRecord(boolean newCommunityDataRecord) {
		this.newCommunityDataRecord = newCommunityDataRecord;
	}

	public void saveUser(User newUser, AsyncSaveUserListener list) {
		new AsyncSaveUserMongo(this, list, newUser).execute();
	}

	/**
     * 1. Fetches all data from db.
     * 2. Uploads the ones that have not been uploaded to mongo.
     * 
     * **If the mongo process fails in the middle we consider them all uploaded.
     * 
     * @author liakos
     *
     */
    private class AsyncLoadFishDataFromDb extends AsyncTask<Void, Void, Void> {
        SpearoApplication application;
        Map<Fish, FishStatistic> toBeUploaded = new HashMap<Fish, FishStatistic>();
        boolean haveBeenUploaded = false;
        
        public AsyncLoadFishDataFromDb(SpearoApplication application) {
            this.application = application;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... unused) {
            Database db = new Database(getApplicationContext());
            fishingSessions = getDbFishingSessions();
            for (FishingSession fishingSession : fishingSessions) {
				fishCatches.addAll(fishingSession.getFishCatches());
			}
            fishies = db.fetchFishFromDb();

            dbCommunityData = db.fetchCommunityData();
            Collections.sort(dbCommunityData);
            Collections.reverse(dbCommunityData);
            
            if (SpearoUtils.isOnline(getApplicationContext())){
            	toBeUploaded = FishingHelper.convertUserSessionsToStats(fishingSessions, true);
				if (!toBeUploaded.isEmpty()) {
					
					if (ConsistencyChecker.isSuspiciousUser(application, fishingSessions.size())){
						db.markSessionsAsUploaded(fishingSessions);
						return null;
					}
					
					SyncHelper syncHelper = new SyncHelper(application);
					syncHelper.uploadAtlasStats(toBeUploaded);
					haveBeenUploaded = true;
				}
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setLoaded(true);
            if (haveBeenUploaded){
            	markAsUploaded(fishingSessions);
            }
            
        }
    }
    
    
    private class AsyncUploadDataToMongo extends AsyncTask<Void, Void, Void> {
        SpearoApplication application;
        Map<Fish, FishStatistic> toBeUploaded = new HashMap<Fish, FishStatistic>();
        boolean haveBeenUploaded = false;
        
        public AsyncUploadDataToMongo(SpearoApplication application) {
            this.application = application;
        }

        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... unused) {
            
            if (SpearoUtils.isOnline(getApplicationContext())){
            	toBeUploaded = FishingHelper.convertUserSessionsToStats(fishingSessions, true);
				if (!toBeUploaded.isEmpty()) {
					
					if (ConsistencyChecker.isSuspiciousUser(application, fishingSessions.size())){
						haveBeenUploaded = true;
						return null;
					}
					
					SyncHelper syncHelper = new SyncHelper(application);
					syncHelper.uploadAtlasStats(toBeUploaded);
					haveBeenUploaded = true;
				}
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (haveBeenUploaded){
            	markAsUploaded(fishingSessions);
            }
        }
    }

	public void markAsUploaded(List<FishingSession> fishingSessions2) {
		Database db = new Database(getApplicationContext());
    	db.markSessionsAsUploaded(fishingSessions2);
		
	}
	
	public void reloadFishWhenNewRecord() {
		List<Fish> fetchFishFromDb = new Database(getApplicationContext()).fetchFishFromDb();
		fishies.clear();
		for (Fish fish : fetchFishFromDb) {
			fishies.add(fish);
		}
	}


	public void checkForMongoUpload() {
		SharedPreferences app_preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		long lastUploadMillis = app_preferences.getLong(Constants.LAST_UPLOAD_MILLIS, -1);
		if ((new Date().getTime() - lastUploadMillis) > (8*60*60*1000)){
			uploadSessions();
			Editor edit = app_preferences.edit();
			edit.putLong(Constants.LAST_UPLOAD_MILLIS, new Date().getTime());
			edit.commit();
		}
	}
	
	public void uploadSessions(){
		new AsyncUploadDataToMongo(this).execute();
	}

	public void refreshCommunityData(List<FishAverageStatistic> stats) {
		if (stats.isEmpty()){
			return;
		}
		
		dbCommunityData.clear();
		dbCommunityData.addAll(stats);
		
	}
	
}