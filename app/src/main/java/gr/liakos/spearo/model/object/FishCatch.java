package gr.liakos.spearo.model.object;

import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class FishCatch {
	
	long fishCatchId;
	
	Integer fishingSessionId;
	
	Integer fishId;
	
	Fish fish;
	
	Double weight;
	
	Double depthMeters;

	Integer caughtWith;
	
	/**
	 * The sum of minutes from 00:00
	 */
	Integer catchTimeInMinutes;
	
	/**
	 * 0-24 hour of day
	 */
	Integer catchHour;
	
	
	public FishCatch(Integer fishCatchId, Integer fishingSessionId, Integer fishId, Integer timeInMinutes, Integer catchHour,  Double weight,
			Double depthMeters) {
		this.fishCatchId = fishCatchId;
		this.fishingSessionId = fishingSessionId;
		this.catchTimeInMinutes = timeInMinutes;
		this.catchHour = catchHour;
		this.fishId = fishId;
		this.weight = weight;
		this.depthMeters = depthMeters;
	}

	public FishCatch() {
		this.fishCatchId = Database.INVALID_ID;
	}
	
	public Fish getFish() {
		return fish;
	}

	public void setFish(Fish fish) {
		this.fish = fish;
	}

	public Integer getFishId() {
		return fishId;
	}

	public void setFishId(Integer fishId) {
		this.fishId = fishId;
	}

	public Integer getCatchHour() {
		return catchHour;
	}

	public void setCatchHour(Integer catchHour) {
		this.catchHour = catchHour;
	}

	public long getFishCatchId() {
		return fishCatchId;
	}

	public void setFishCatchId(long fishCatchId) {
		this.fishCatchId = fishCatchId;
	}

	public Integer getFishingSessionId() {
		return fishingSessionId;
	}

	public void setFishingSessionId(Integer fishingSessionId) {
		this.fishingSessionId = fishingSessionId;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getDepthMeters() {
		return depthMeters;
	}

	public void setDepthMeters(Double depthMeters) {
		this.depthMeters = depthMeters;
	}

	public Integer getCaughtWith() {
		return caughtWith;
	}

	public void setCaughtWith(Integer caughtWith) {
		this.caughtWith = caughtWith;
	}

//	public Integer getTimeOfTheDay() {
//		return catchTimeInMinutes;
//	}
//
//	public void setTimeOfTheDay(Integer timeOfTheDay) {
//		this.catchTimeInMinutes = timeOfTheDay;
//	}

//	public static FishCatch getFromId(Context context, long id) {
//	        //Log.v(TAG, String.format("Requesting item [%d]", id));
//	        synchronized (context) {
//	            Cursor cursor = null;
//	            try {
//	                cursor = context.getContentResolver()
//	                        .query(Uri.withAppendedPath(ContentDescriptor.FishCatch.CONTENT_URI,
//	                                String.valueOf(id)), null, null, null, null);
//	                cursor.moveToFirst();
//	                return createFromCursor(cursor);
//	            } finally {
//	                if (cursor != null)
//	                    cursor.close();
//	            }
//	        }
//	    }

	    public static ContentValues asContentValues(FishCatch item) {
	        if (item == null)
	            return null;
	        synchronized (item) {
	            ContentValues toRet = new ContentValues();
	            toRet.put(ContentDescriptor.FishCatch.Cols.FISHCATCHID, item.fishCatchId);
	            toRet.put(ContentDescriptor.FishCatch.Cols.FISHINGSESSIONID, item.fishingSessionId);
	            toRet.put(ContentDescriptor.FishCatch.Cols.FISHID, item.fishId);
	            toRet.put(ContentDescriptor.FishCatch.Cols.WEIGHT, item.weight);
	            toRet.put(ContentDescriptor.FishCatch.Cols.DEPTH, item.depthMeters);
	            toRet.put(ContentDescriptor.FishCatch.Cols.CATCH_TIME_MINUTES, item.catchTimeInMinutes);
	            toRet.put(ContentDescriptor.FishCatch.Cols.CATCH_HOUR, item.catchHour);
				toRet.put(ContentDescriptor.FishCatch.Cols.CAUGHT_WITH, item.caughtWith);
	            return toRet;
	        }
	    }

//	    public static FishCatch createFromCursor(Cursor cursor) {
//	        synchronized (cursor) {
//	            if (cursor.isClosed() || cursor.isAfterLast() || cursor.isBeforeFirst()) {
//	                //Log.v(TAG, String.format("Requesting entity but no valid cursor"));
//	                return null;
//	            }
//	            FishCatch toRet = new FishCatch();
//	            toRet.fishCatchId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.FISHCATCHID));
//	            toRet.fishingSessionId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.FISHINGSESSIONID));
//	            toRet.fishId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.FISHID));
//	            toRet.weight = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.WEIGHT));
//	            toRet.depthMeters = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.DEPTH));
//	            toRet.catchTimeInMinutes = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.CATCH_TIME_MINUTES));
//	            toRet.catchHour = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.CATCH_HOUR));
//	            toRet.caughtWith = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishCatch.Cols.CAUGHT_WITH));
//
//	            return toRet;
//	        }
//	    }

//	    /**
//	     * let the id decide if we have an insert or an update
//	     *
//	     * @param resolver
//	     * @param item
//	     */
//	    public static void save(ContentResolver resolver, FishCatch item) {
//	        if (item.fishCatchId == Database.INVALID_ID)
//	            resolver.insert(ContentDescriptor.FishCatch.CONTENT_URI, FishCatch.asContentValues(item));
//	        else
//	            resolver.update(ContentDescriptor.FishCatch.CONTENT_URI, FishCatch.asContentValues(item),
//	                    String.format("%s=?", ContentDescriptor.FishCatch.Cols.FISHCATCHID),
//	                    new String[]{
//	                            String.valueOf(item.fishCatchId)
//	                    });
//	    }

}
