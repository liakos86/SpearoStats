
package gr.liakos.spearo.model;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.SpearGunBrand;
import gr.liakos.spearo.enums.SpeargunType;
import gr.liakos.spearo.enums.Wind;
import gr.liakos.spearo.enums.WindVolume;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.DbColumns;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "spearo_stats.db";
    private static final int DATABASE_VERSION =  6;
    // this is also considered as invalid id by the server
    public static final Integer INVALID_ID = -1;
    private Context mContext;

    public Database(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContentDescriptor.FishingSession.createTable());
        db.execSQL(ContentDescriptor.FishCatch.createTable());
        db.execSQL(ContentDescriptor.Fish.createTable());
        db.execSQL(ContentDescriptor.FishAverageStatistic.createTable());
        db.execSQL(ContentDescriptor.Speargun.createTable());

        db.execSQL(ContentDescriptor.Fish.insertSpecies());
    }

    /**
     * This only runs when an update is installed.
     * If it is a brand new installation it will not be executed.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		if (newVersion == 2 && oldVersion == 1){
//			db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_IMAGE + " TEXT;");
//			db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_MOON + " INTEGER DEFAULT 0;");
//			db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_WIND + " INTEGER DEFAULT 0;");
//		}
//
//		if (newVersion == 3 && oldVersion == 1){
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_IMAGE + " TEXT;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_MOON + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_WIND + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME + " INTEGER DEFAULT 0;");
//        }
//
//        if (newVersion == 3 && oldVersion == 2){
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME + " INTEGER DEFAULT 0;");
//        }
//
//        if (newVersion == 4 &&  oldVersion == 1 ){
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_IMAGE + " TEXT;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_MOON + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.Cols.SESSION_WIND + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_IMAGE_URI_PATH + " TEXT;");
//            db.execSQL(ContentDescriptor.Fish.insertAdditionalSpecies0());
//        }

//        if (newVersion == 4 &&  oldVersion == 2 ){
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME + " INTEGER DEFAULT 0;");
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_IMAGE_URI_PATH + " TEXT;");
//            db.execSQL(ContentDescriptor.Fish.insertAdditionalSpecies0());
//        }
//
//        if (newVersion == 4 &&  oldVersion == 3 ){
//            db.execSQL("ALTER TABLE " + gr.liakos.spearo.model.ContentDescriptor.FishingSession.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishingSession.Cols.SESSION_IMAGE_URI_PATH + " TEXT;");
//            db.execSQL(ContentDescriptor.Fish.insertAdditionalSpecies0());
//        }

        if (newVersion == 5){
            db.execSQL(ContentDescriptor.Fish.insertAdditionalSpecies_27_11_2021());
            SharedPreferences app_preferences = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putString(Constants.PREFS_NEW_SPECIES, speciesTextFor_27_11_2021());
            editor.apply();
        }

        if (newVersion == 6){
            db.execSQL(ContentDescriptor.Speargun.createTable());
            db.execSQL("ALTER TABLE " + ContentDescriptor.FishCatch.TABLE_NAME + " ADD COLUMN " + ContentDescriptor.FishCatch.Cols.CAUGHT_WITH + " INTEGER;");
        }

	}

    String speciesTextFor_27_11_2021() {
        Resources resources = mContext.getResources();
        StringBuilder builder = new StringBuilder(Constants.EMPTY);
        builder.append(resources.getString(R.string.Caranx_ignobilis) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Caranx_sexfasciatus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Caranx_melampygus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Gnathanodeon_speciosus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Caranx_ignobilis_melampygus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Carangoides_orthogrammus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Pseudocaranx_dentex) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Carangoides_fulvoguttatus) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Acanthocybium_solandri) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Latridopsis_ciliaris) + Constants.COMMA_SEP);
        builder.append(resources.getString(R.string.Sepia));
        return  builder.toString();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
//    	db.execSQL(ContentDescriptor.Fish.insertSpecies());
    }

    /**
     * Adds a fishing session. Return the id in order to use for catches insertion.
     *
     * @param fishingSession the session
     */
    public void addOrUpdateFishingSession(FishingSession fishingSession) {
        ContentResolver resolver = mContext.getContentResolver();
        Integer fishingSessionId = null;
        if (fishingSession.getFishingSessionId().equals(Database.INVALID_ID)) {
            Uri uri = resolver.insert(ContentDescriptor.FishingSession.CONTENT_URI, FishingSession.asContentValues(fishingSession));
            fishingSessionId = Integer.valueOf(uri.getLastPathSegment());
        }else{
            fishingSessionId = fishingSession.getFishingSessionId();
            resolver.delete(ContentDescriptor.FishCatch.CONTENT_URI, ContentDescriptor.FishCatch.Cols.FISHINGSESSIONID +"="+fishingSession.getFishingSessionId(), null);
        }

        saveCatches(fishingSessionId, fishingSession.getFishCatches());
    }

    void saveCatches(Integer id, List<FishCatch> fishCatches){
        for (FishCatch fishCatch : fishCatches) {
            fishCatch.setFishingSessionId(id);
            addFishCatch(fishCatch);
        }
    }

    /**
     * Self explanatory.
     *
     * @param session
     */
    public void deleteSession(FishingSession session) {
        ContentResolver resolver = mContext.getContentResolver();
        String idToDelete = String.valueOf(session.getFishingSessionId());
		resolver.delete(ContentDescriptor.FishingSession.CONTENT_URI, ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID + "=" + idToDelete, null);
        resolver.delete(ContentDescriptor.FishCatch.CONTENT_URI, ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID + "=" + idToDelete, null);
    }

    /**
     * Adds a fish catch.
     *
     * @param fishCatch
     */
	void addFishCatch(FishCatch fishCatch) {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.insert(ContentDescriptor.FishCatch.CONTENT_URI, FishCatch.asContentValues(fishCatch));
    }

    /**
     * Adds a speargun.
     *
     * @param speargun
     */
    public Speargun addSpeargun(Speargun speargun) {
        ContentResolver resolver = mContext.getContentResolver();
        Uri inserted = resolver.insert(ContentDescriptor.Speargun.CONTENT_URI, Speargun.asContentValues(speargun));
        speargun.setGunId(Integer.valueOf(inserted.getLastPathSegment()));
        return speargun;
    }

    public List<FishingSession> fetchFishingSessionsFromDb() {
        String[] FROM = DbColumns.fromSession();
        int sIdPosition = 0;
        int sDatePosition = 1;
        int sLatPosition = 2;
        int sLonPosition = 3;
        int sUploadedPosition = 4;
        int sSessionImgPosition = 5;
        int sSessionWindPosition = 6;
        int sSessionWindVolumePosition = 7;
        int sSessionImgUriPosition = 8;

        String selection = null;
        Cursor c = mContext.getContentResolver().query(ContentDescriptor.FishingSession.CONTENT_URI, FROM, selection,
                null, null);

        List<FishingSession> fishingSessions = new ArrayList<FishingSession>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                FishingSession fishingSession = new FishingSession(c.getInt(sIdPosition),
                        c.getLong(sDatePosition), c.getDouble(sLatPosition), c.getDouble(sLonPosition), c.getInt(sUploadedPosition) == 1);
                
                fishingSession.setSessionImage(c.getString(sSessionImgPosition));
                fishingSession.setSessionWind(Wind.ofPosition(c.getInt(sSessionWindPosition)));
                fishingSession.setSessionWindVolume(WindVolume.ofPosition(c.getInt(sSessionWindVolumePosition)));
                fishingSession.setSessionImageUriPath(c.getString(sSessionImgUriPosition));

                String selectionStringForCatches = ContentDescriptor.FishCatch.Cols.FISHINGSESSIONID + " = " + String.valueOf(fishingSession.getFishingSessionId());
                fishingSession.getFishCatches().addAll(fetchCatchesForSelection(selectionStringForCatches));
                fishingSessions.add(fishingSession);
            }
        }
        c.close();
        c = null;
        
        return fishingSessions;

    }

    public List<Speargun> fetchSpeargunsFromDb() {

        String[] FROM = DbColumns.fromSpeargun();
        int sIdPosition = 0;
        int sBrandPosition = 1;
        int sModelPosition = 2;
        int sTypePosition = 3;
        int sLengthPosition = 4;
        int sNicknamePosition = 5;

        String selection = null;
        Cursor c = mContext.getContentResolver().query(ContentDescriptor.Speargun.CONTENT_URI, FROM, selection,
                null, null);


        List<Speargun> spearguns = new ArrayList<>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Speargun speargun = new Speargun(c.getInt(sIdPosition),
                        SpearGunBrand.ofPosition(c.getInt(sBrandPosition)),
                                c.getString(sModelPosition),
                                SpeargunType.ofPosition(c.getInt(sTypePosition)),
                                c.getInt(sLengthPosition),
                                c.getString(sNicknamePosition));

                String selectionStringForCatches = ContentDescriptor.FishCatch.Cols.CAUGHT_WITH + " = " + speargun.getGunId();
                speargun.setCaughtFish(fetchCatchesForSelection(selectionStringForCatches));
               spearguns.add(speargun);
            }
        }
        c.close();
        c = null;

        Collections.sort(spearguns);
        return spearguns;

    }

    /**
     * The catches for the given session id are fetched.
     *
     * @param selectionString
     * @return
     */
    List<FishCatch> fetchCatchesForSelection(String selectionString) {
        String[] FROM = DbColumns.fromFishCatch();

        int sIdPosition = 0;
        int sSessIdPosition = 1;
        int sFishIdPosition = 2;
        int sTimePosition = 3;
        int sHourPosition = 4;
        int sWeightPosition = 5;
        int sDepthPosition = 6;

        Cursor c = mContext.getContentResolver().query(ContentDescriptor.FishCatch.CONTENT_URI, FROM,
                selectionString,
                null, "weight desc");

        List<FishCatch> catches = new ArrayList<FishCatch>();

        if (c.getCount() > 0) {
        	
            while (c.moveToNext()) {
            	Fish fish = Fish.getFromId(mContext, c.getInt(sFishIdPosition));
                FishCatch fishCatch= new FishCatch(c.getInt(sIdPosition), c.getInt(sSessIdPosition), c.getInt(sFishIdPosition), c.getInt(sTimePosition),
                		c.getInt(sHourPosition), c.getDouble(sWeightPosition), c.getDouble(sDepthPosition));
                fishCatch.setFish(fish);
                catches.add(fishCatch);            }
        }
        c.close();
        c = null;

        return catches;

    }

    /**
     * The list of the db stored fish.
     *
     * @return
     */
    public List<Fish> fetchFishFromDb() {
    	
    	String [] columns = DbColumns.fromFish();
    			
		int positionFISHID = 0;
		int positionLATINNAME = 1;
		int positionRECORDWEIGHT = 2;
		int positionFISHFAMILY = 3;
		int positionMAXALLOWEDCATCHWEIGHT = 4;
		int positionCONCERN = 5;
		
		Cursor c = mContext.getContentResolver().query(ContentDescriptor.Fish.CONTENT_URI, columns, null, null, null);
        List<Fish> fishies = new ArrayList<Fish>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                Fish fish = new Fish(c.getInt(positionFISHID),
                        c.getString(positionLATINNAME),
                        c.getDouble(positionRECORDWEIGHT),
                        c.getInt(positionFISHFAMILY),
                        c.getInt(positionCONCERN)
                );

                fish.setCommonName(SpearoUtils.getStringResourceByName(mContext, Fish.commonNamePattern(fish)));
                fish.setSecondaryCommonNameForSearch( SpearoUtils.getLocalizedName(mContext, fish)  );
                fish.setMaxAllowedCatchWeight(c.getDouble(positionMAXALLOWEDCATCHWEIGHT));
                fishies.add(fish);
            }
        }
        c.close();
        c = null;
        return fishies;
    }

    /**
     * Uploading of catches occurs once.
     *
     * @param fishingSessions2
     */
	public void markSessionsAsUploaded(List<FishingSession> fishingSessions2) {
		if (fishingSessions2.isEmpty()){
			return;
		}
		
	    ContentResolver resolver = mContext.getContentResolver();
	    String updateWhere = "(";
	    for (FishingSession fishingSession : fishingSessions2) {
	    	
	    	if (updateWhere.length() == 1){
	    		updateWhere += fishingSession.getFishingSessionId();
	    	}
	    	
	    	updateWhere += "," + fishingSession.getFishingSessionId();
	    }
	    updateWhere += ")";
	    ContentValues values = new ContentValues();
	    values.put(ContentDescriptor.FishingSession.Cols.UPLOADED, "1");
	    resolver.update(ContentDescriptor.FishingSession.CONTENT_URI, values, ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID+" IN "+ updateWhere, null);
	}

    /**
     * Premium statistics are always saved/updated in db for offline access.
     * Null safe list.
     *
     * @return db mongo data
     */
	public List<FishAverageStatistic> fetchCommunityData() {
		String [] columns = DbColumns.fromFishAvg();
		
		int positionFISHAVGID = 0;
		int positionFISHID = 1;
		int positionTOTALCATCHES = 2;
		int positionAVGDEPTH = 3;
		int positionAVGWEIGHT = 4;
		int positionMOSTCOMMONSUMMERHOUR = 5;
		int positionMOSTCOMMONWINTERHOUR = 6;
		int positionRECORDWEIGHT = 7;

		Cursor c = mContext.getContentResolver().query(ContentDescriptor.FishAverageStatistic.CONTENT_URI, columns, null, null, null);
		List<FishAverageStatistic> avgData = new ArrayList<FishAverageStatistic>();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                FishAverageStatistic avgStat = new FishAverageStatistic(c.getInt(positionFISHAVGID),
                        c.getInt(positionFISHID),
                        c.getInt(positionTOTALCATCHES),
                        c.getDouble(positionAVGDEPTH),
                        c.getDouble(positionAVGWEIGHT),
                        c.getInt(positionMOSTCOMMONSUMMERHOUR),
                        c.getInt(positionMOSTCOMMONWINTERHOUR),
                        c.getDouble(positionRECORDWEIGHT)
                );
                
                avgStat.setFish(Fish.getFromId(mContext, avgStat.getFishId()));
				avgData.add(avgStat);
            }
        }
        c.close();
        c = null;
        return avgData;
	}

    /**
     * Records for fish are initially stored in db.
     * We want to be able to override a record via a mongo update.
     *
     * @param communityStats
     * @return
     */
	public boolean updateRecordsFromCommunityData(
			List<FishStatistic> communityStats) {
		if (communityStats.isEmpty()){
			return false;
		}
		boolean newRecord = false;
	    ContentResolver resolver = mContext.getContentResolver();
	    for (FishStatistic stat : communityStats) {
            Fish dbFish = Fish.getFromId(mContext,  stat.getFishId());
            if (dbFish == null){ // fish in mongo but not in db
                continue;
            }

	    	if (stat.getRecordWeight() <= dbFish.getRecordCatchWeight()){
	    		continue;
	    	}
	    	
	    	newRecord = true;
	    	ContentValues values = new ContentValues();
	    	values.put(ContentDescriptor.Fish.Cols.RECORDWEIGHT, stat.getRecordWeight());
	    	resolver.update(ContentDescriptor.Fish.CONTENT_URI, values, ContentDescriptor.Fish.Cols.FISHID + " = " + stat.getFishId(), null);
	    }
	    return newRecord;
	}

    /**
     * This refers to mongo retrieved stats for a premium user. We want the data to be available offline.
     *
     * @param stats
     */
	public void addNewStats(List<FishAverageStatistic> stats) {
		if (stats.isEmpty()){
			return;
		}
		
      ContentResolver resolver = mContext.getContentResolver();
      resolver.delete(ContentDescriptor.FishAverageStatistic.CONTENT_URI, null, null);
      for (FishAverageStatistic fishStatistic : stats) {
    	  resolver.insert(ContentDescriptor.FishAverageStatistic.CONTENT_URI, FishAverageStatistic.asContentValues(fishStatistic));
      }
	}
	
}
