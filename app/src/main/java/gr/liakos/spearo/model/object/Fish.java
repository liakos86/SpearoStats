package gr.liakos.spearo.model.object;

import gr.liakos.spearo.enums.Concern;
import gr.liakos.spearo.enums.FishFamily;
import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Fish{
	
	Concern concern;

	Integer fishId;
	
	String latinName;
	
	String commonName;
	
	String secondaryCommonNameForSearch = Constants.EMPTY;
	
	Double recordCatchWeight;
	
	FishFamily fishFamily;

	Double maxAllowedCatchWeight;
	
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

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public Double getRecordCatchWeight() {
		return recordCatchWeight;
	}

	public void setRecordCatchWeight(Double recordCatchWeight) {
		this.recordCatchWeight = recordCatchWeight;
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

	public void setFishFamily(FishFamily fishFamily) {
		this.fishFamily = fishFamily;
	}
	
	public Concern getConcern() {
		return concern;
	}

	public void setConcern(Concern concern) {
		this.concern = concern;
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

    /**
     * let the id decide if we have an insert or an update
     *
     * @param resolver
     * @param item
     */
    public static void save(ContentResolver resolver, Fish item) {
        if (item.fishId == Database.INVALID_ID)
            resolver.insert(ContentDescriptor.Fish.CONTENT_URI, Fish.asContentValues(item));
        else
            resolver.update(ContentDescriptor.Fish.CONTENT_URI, Fish.asContentValues(item),
                    String.format("%s=?", ContentDescriptor.Fish.Cols.FISHID),
                    new String[]{
                            String.valueOf(item.fishId)
                    });
    }

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
