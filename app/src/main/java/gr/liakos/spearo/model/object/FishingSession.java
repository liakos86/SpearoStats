package gr.liakos.spearo.model.object;

import gr.liakos.spearo.enums.MoonPhase;
import gr.liakos.spearo.enums.Wind;
import gr.liakos.spearo.enums.WindVolume;
import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FishingSession implements Comparable<FishingSession>{
	
	Integer fishingSessionId;
	
	Long fishingDate;
	
	Double latitude;
	
	Double longitude;
	
	List<FishCatch> fishCatches;
	
	boolean uploadedToMongo;
	
	String sessionImage;
	
	//MoonPhase sessionMoon;
	
	Wind sessionWind;

	WindVolume sessionWindVolume;
	
	
	public FishingSession(Integer fishingSessionId, Long fishingDate, Double latitude, Double longitude, boolean uploaded) {
		this.fishingSessionId = fishingSessionId;
		this.fishingDate = fishingDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fishCatches = new ArrayList<FishCatch>();
		this.uploadedToMongo = uploaded;
	}

	public FishingSession() {
		this.fishCatches = new ArrayList<FishCatch>();
		this.fishingSessionId = Database.INVALID_ID;
	}

	public List<FishCatch> getFishCatches(){
		return fishCatches;
	}
	
	public Integer getFishingSessionId() {
		return fishingSessionId;
	}

	public void setFishingSessionId(Integer fishingSessionId) {
		this.fishingSessionId = fishingSessionId;
	}

	public boolean isUploadedToMongo() {
		return uploadedToMongo;
	}

	public void setUploadedToMongo(boolean uploadedToMongo) {
		this.uploadedToMongo = uploadedToMongo;
	}

	public Long getFishingDate() {
		return fishingDate;
	}

	public void setFishingDate(Long fishingDate) {
		this.fishingDate = fishingDate;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getSessionImage() {
		return sessionImage;
	}

	public void setSessionImage(String sessionImage) {
		this.sessionImage = sessionImage;
	}
	
//	public MoonPhase getSessionMoon() {
//		return sessionMoon;
//	}

	//public void setSessionMoon(MoonPhase sessionMoon) {
//		this.sessionMoon = sessionMoon;
//	}

	public Wind getSessionWind() {
		return sessionWind;
	}

	public void setSessionWind(Wind wind) {
		this.sessionWind = wind;
	}

	public WindVolume getSessionWindVolume() {
		return sessionWindVolume;
	}

	public void setSessionWindVolume(WindVolume sessionWindVolume) {
		this.sessionWindVolume = sessionWindVolume;
	}

	public Double getRecordCatchWeight(){
		Double record = 0d;
		for (FishCatch fCatch : fishCatches){
			Double weight = fCatch.getWeight();
			if  (weight != null && weight > record){
				record = weight;
			}
		}
		
		if (record > 0){
			return record;
		}
		
		return null;
	}

	public static FishingSession getFromId(Context context, long id) {
        //Log.v(TAG, String.format("Requesting item [%d]", id));
        synchronized (context) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(Uri.withAppendedPath(ContentDescriptor.FishingSession.CONTENT_URI,
                                String.valueOf(id)), null, null, null, null);
                cursor.moveToFirst();
                return createFromCursor(cursor);
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    public static ContentValues asContentValues(FishingSession item) {
        if (item == null)
            return null;
        synchronized (item) {
            ContentValues toRet = new ContentValues();
            toRet.put(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID, item.fishingSessionId);
            toRet.put(ContentDescriptor.FishingSession.Cols.FISHINGDATE, item.fishingDate);
            toRet.put(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLAT, item.latitude);
            toRet.put(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLON, item.longitude);
            toRet.put(ContentDescriptor.FishingSession.Cols.UPLOADED, item.uploadedToMongo);
            toRet.put(ContentDescriptor.FishingSession.Cols.SESSION_IMAGE, item.sessionImage);
            //toRet.put(ContentDescriptor.FishingSession.Cols.SESSION_MOON, item.sessionMoon.getPosition());
            toRet.put(ContentDescriptor.FishingSession.Cols.SESSION_WIND, item.sessionWind.getPosition());
			toRet.put(ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME, item.sessionWindVolume.getPosition());
            
            return toRet;
        }
    }

    public static FishingSession createFromCursor(Cursor cursor) {
        synchronized (cursor) {
            if (cursor.isClosed() || cursor.isAfterLast() || cursor.isBeforeFirst()) {
                //Log.v(TAG, String.format("Requesting entity but no valid cursor"));
                return null;
            }
            Integer fishingSessionId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID));
            Long fishingDate = cursor.getLong(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.FISHINGDATE));
            Double latitude = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLAT));
            Double longitude = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLON));
            boolean uploaded = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.UPLOADED))  == 1; 
            FishingSession fishingSession = new FishingSession(fishingSessionId, fishingDate, latitude, longitude, uploaded);
            String sessionImg = cursor.getString(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.SESSION_IMAGE));
            fishingSession.setSessionImage(sessionImg);
//            MoonPhase phase = MoonPhase.ofPosition(cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.SESSION_MOON)));
//            fishingSession.setSessionMoon(phase);
            Wind wind = Wind.ofPosition(cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.SESSION_WIND)));
            fishingSession.setSessionWind(wind);
			WindVolume windVolume = WindVolume.ofPosition(cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME)));
			fishingSession.setSessionWindVolume(windVolume);
			return fishingSession;
        }
    }

//    /**
//     * let the id decide if we have an insert or an update
//     *
//     * @param resolver
//     * @param item
//     */
//    public static void save(ContentResolver resolver, FishingSession item) {
//        if (item.fishingSessionId == Database.INVALID_ID)
//            resolver.insert(ContentDescriptor.FishingSession.CONTENT_URI, FishingSession.asContentValues(item));
//        else
//            resolver.update(ContentDescriptor.FishingSession.CONTENT_URI, FishingSession.asContentValues(item),
//                    String.format("%s=?", ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID),
//                    new String[]{
//                            String.valueOf(item.fishingSessionId)
//                    });
//    }

	@Override
	public int compareTo(FishingSession arg0) {
		
		if (this.fishingDate == null || this.fishingDate == 0){
			return -1;
		}
		
		if (arg0.getFishingDate() == null || arg0.getFishingDate() == 0){
			return 1;
		}
		
		return  this.fishingDate > arg0.getFishingDate() ? 1 : -1;
	}
	
}
