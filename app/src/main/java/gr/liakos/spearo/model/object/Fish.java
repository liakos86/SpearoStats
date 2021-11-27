package gr.liakos.spearo.model.object;

import gr.liakos.spearo.enums.Concern;
import gr.liakos.spearo.enums.FishFamily;
import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.List;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Represents a fish species that a user can record in his {@link FishingSession}
 * as a {@link FishCatch}. A fish is persisted in the database and will be retrieved upon initialization.
 * {@link #recordCatchWeight} is initially inserted with a script, but is checked every time after mongo updates,
 * in order to be updated via {{@link Database#updateRecordsFromCommunityData(List)}}
 */
public class Fish{

	/**
	 * Unique identifier.
	 * Same id exists for fish that are tracked in mongo.
	 */
	Integer fishId;

	/**
	 * The preservation concern of the species.
	 */
	Concern concern;

	/**
	 * The scientific name of the fish, e.g. 'diplodus sargus'
	 */
	String latinName;

	/**
	 * The common name as loaded via locale strings.
	 */
	String commonName;

	/**
	 * To support greek users with english language, the secondary common name is the greek common name.
	 */
	String secondaryCommonNameForSearch = Constants.EMPTY;

	/**
	 * If the fishId exists in mongo, it will also have a record.
	 * A user cannot store a fish with larger weight than this.
	 */
	Double recordCatchWeight;

	/**
	 * The general fish family that this fish belongs to.
	 */
	FishFamily fishFamily;

	/**
	 * If there is no official record, we need to set a limit.
	 */
	Double maxAllowedCatchWeight;

	/**
	 * Creates a new fish object.
	 *
	 * @param fishId
	 * @param latinName
	 * @param recordWeight
	 * @param fishFamily
	 * @param concern
	 */
	public Fish(Integer fishId, String latinName, Double recordWeight, 
			int fishFamily, int concern) {
		this.fishId = fishId;
		this.latinName = latinName;
		this.recordCatchWeight = recordWeight;
		this.fishFamily = FishFamily.fromPosition(fishFamily);
		this.concern = Concern.fromWeight(concern);
	}

	/**
	 * set invalid id to trigger auto increment sequence.
	 */
	public Fish() {
		this.fishId = Database.INVALID_ID;
	}

	public Integer getFishId() {
		return fishId;
	}

	public void setFishId(Integer fishId) {
		this.fishId = fishId;
	}

	public String getImageName() {
		return latinName.toLowerCase(Locale.US).replace(Constants.SPACE, Constants.UNDERSCORE);
	}

	public String getLatinName() {
		return latinName;
	}

	public Double getRecordCatchWeight() {
		return recordCatchWeight;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public FishFamily getFishFamily() {
		return fishFamily;
	}
	
	public Concern getConcern() {
		return concern;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Fish)){
			return false;
		}
		
		Fish fish = (Fish) o;
		if (!fish.getFishId().equals(this.getFishId())){
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (getLatinName().hashCode()+ getFishId().hashCode()) * 31;
	}
	
	@Override
	public String toString() {
		return this.getCommonName();
	}
	
	public static Fish getFromId(Context context, long id) {
        //Log.v(TAG, String.format("Requesting item [%d]", id));
		synchronized (context) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(Uri.withAppendedPath(ContentDescriptor.Fish.CONTENT_URI,
                                String.valueOf(id)), null, null, null, null);
                cursor.moveToFirst();
                 Fish fromCursor = createFromCursor(cursor);
                 if (fromCursor == null){//id not in db. maybe came from mongo
                 	return null;
				 }

                 fromCursor.setCommonName(SpearoUtils.getStringResourceByName(context, Fish.commonNamePattern(fromCursor)));
                 return fromCursor;
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    public static String commonNamePattern(Fish fish) {
		return fish.getLatinName().replace(Constants.SPACE, Constants.UNDERSCORE);
	}

	public static ContentValues asContentValues(Fish item) {
        if (item == null)
            return null;
        synchronized (item) {
            ContentValues toRet = new ContentValues();
            toRet.put(ContentDescriptor.Fish.Cols.FISHID, item.fishId);
            toRet.put(ContentDescriptor.Fish.Cols.LATINNAME, item.latinName);
            toRet.put(ContentDescriptor.Fish.Cols.RECORDWEIGHT, item.recordCatchWeight);
            toRet.put(ContentDescriptor.Fish.Cols.FISHFAMILY, item.fishFamily.getPosition());
            toRet.put(ContentDescriptor.Fish.Cols.MAXALLOWEDCATCHWEIGHT, item.maxAllowedCatchWeight);
            toRet.put(ContentDescriptor.Fish.Cols.CONCERN, item.concern.getWeight());
            return toRet;
        }
    }

    public static Fish createFromCursor(Cursor cursor) {
        synchronized (cursor) {
            if (cursor.isClosed() || cursor.isAfterLast() || cursor.isBeforeFirst()) {
                //Log.v(TAG, String.format("Requesting entity but no valid cursor"));
                return null;
            }
            Fish toRet = new Fish();
            toRet.fishId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.FISHID));
            toRet.latinName = cursor.getString(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.LATINNAME));
            toRet.recordCatchWeight = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.RECORDWEIGHT));
            toRet.fishFamily = FishFamily.fromPosition(cursor.getInt(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.FISHFAMILY)));
            toRet.maxAllowedCatchWeight = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.MAXALLOWEDCATCHWEIGHT));
            toRet.concern = Concern.fromWeight(cursor.getInt(cursor.getColumnIndex(ContentDescriptor.Fish.Cols.CONCERN)));
            return toRet;
        }
    }

//    /**
//     * let the id decide if we have an insert or an update
//     *
//     * @param resolver
//     * @param item
//     */
//    public static void save(ContentResolver resolver, Fish item) {
//        if (item.fishId == Database.INVALID_ID)
//            resolver.insert(ContentDescriptor.Fish.CONTENT_URI, Fish.asContentValues(item));
//        else
//            resolver.update(ContentDescriptor.Fish.CONTENT_URI, Fish.asContentValues(item),
//                    String.format("%s=?", ContentDescriptor.Fish.Cols.FISHID),
//                    new String[]{
//                            String.valueOf(item.fishId)
//                    });
//    }

	public boolean isRecordCatch(double weight) {
		return  this.getRecordCatchWeight() > -1 && weight > this.getRecordCatchWeight();
	}

	public Double getMaxAllowedCatchWeight() {
		return maxAllowedCatchWeight;
	}
	
	public void setMaxAllowedCatchWeight(Double maxAllowedCatchWeight) {
		this.maxAllowedCatchWeight = maxAllowedCatchWeight;
	}

	public String getSecondaryCommonNameForSearch() {
		return secondaryCommonNameForSearch;
	}

	public void setSecondaryCommonNameForSearch(String secondaryCommonNameForSearch) {
		this.secondaryCommonNameForSearch = secondaryCommonNameForSearch;
	}
	
}
