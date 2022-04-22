package gr.liakos.spearo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.google.android.gms.ads.MobileAds;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.model.object.User;
import gr.liakos.spearo.mongo.SyncHelper;
import gr.liakos.spearo.util.ConsistencyChecker;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.FishingHelper;
import gr.liakos.spearo.util.SpearoUtils;

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

    List<Speargun> spearGuns = new ArrayList<>();
    List<Fish> fishies = new ArrayList<Fish>();
    List<FishingSession> fishingSessions = new ArrayList<FishingSession>();
    List<FishCatch> fishCatches = new ArrayList<FishCatch>();
    List<FishAverageStatistic> dbCommunityData = new ArrayList<FishAverageStatistic>();
    
	boolean loaded;
	
	boolean sessionsHaveChanged;
	
	boolean newCommunityDataRecord;

	boolean spearGunsUpdated;
	
	User user;

	//Database database;

	  @Override
		protected void attachBaseContext(Context base) {
			super.attachBaseContext(base);

			ACRA.init(this);
			MyAcraSender mySender = new MyAcraSender(this);
			ACRA.getErrorReporter().addReportSender(mySender);
		}
	  
	  /**
	   * 1. Initialize ADS sdk.
	   * 2. Load data from db.
	   * 3. Initialize user.
	   * 4. Start async inventory query for premium.
	   *
	   */
	@Override
    public void onCreate() {
        super.onCreate();

      //  database = new Database(getApplicationContext());

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

	public List<Speargun> getSpearGuns() {
		return spearGuns;
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
	
	public boolean isNewCommunityDataRecord() {
		return newCommunityDataRecord;
	}

	public void setNewCommunityDataRecord(boolean newCommunityDataRecord) {
		this.newCommunityDataRecord = newCommunityDataRecord;
	}

	public boolean isSpearGunsUpdated() {
		return spearGunsUpdated;
	}

	public void setSpearGunsUpdated(boolean spearGunsUpdated) {
		this.spearGunsUpdated = spearGunsUpdated;
	}

	public void saveUser(User newUser, AsyncSaveUserListener list) {
		new AsyncSaveUserMongo(this, list, newUser).execute();
	}

	public void addSpeargun(Speargun newSpeargun) {
		Speargun speargun = new Database(getApplicationContext()).addSpeargun(newSpeargun);
		spearGuns.add(speargun);
		spearGunsUpdated = true;
	}

	void refreshSpearGuns(){
		spearGuns.clear();
		spearGuns.addAll(new Database(getApplicationContext()).fetchSpeargunsFromDb());
		spearGunsUpdated = true;
	}

    public void checkForSpearGunUpdate(List<FishCatch> fishCatches) {
		for (FishCatch fishCatch: fishCatches ) {
			if (fishCatch.getCaughtWith() != null){
				refreshSpearGuns();
				return;
			}
		}
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

		/**
		 * We retrieve {@link FishingSession}s from db.
		 * We retrieve {@link Fish} from db.
		 * We retrieve the community {@link FishAverageStatistic}s that might exist in db.
		 * If user is online we upload any data that have not been sent to mongo.
		 *
		 * @param unused -
		 * @return -
		 */
		@Override
        protected Void doInBackground(Void... unused) {

            spearGuns = new Database(getApplicationContext()).fetchSpeargunsFromDb();
            fishingSessions = getDbFishingSessions();
            for (FishingSession fishingSession : fishingSessions) {
				fishCatches.addAll(fishingSession.getFishCatches());
			}

			Database db = new Database(getApplicationContext());
            fishies = db.fetchFishFromDb();

            dbCommunityData = db.fetchCommunityData();
            Collections.sort(dbCommunityData);
            
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