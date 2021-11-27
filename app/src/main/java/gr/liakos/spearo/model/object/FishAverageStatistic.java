package gr.liakos.spearo.model.object;

import gr.liakos.spearo.enums.Season;
import gr.liakos.spearo.model.ContentDescriptor;
import gr.liakos.spearo.model.Database;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Map;

/**
 * Statistics about a specific {@link Fish}.
 * Can represent either {@link User}'s personal statistics or Mongo community statistics.
 */
public class FishAverageStatistic implements Comparable<FishAverageStatistic>{

	/**
	 * Unique id for db persistence.
	 */
	Integer fishAvgId;

	/**
	 * Corresponding fish.
	 */
	Fish fish;

	/**
	 * Corresponding fish id.
	 */
	Integer fishId;

	/**
	 * All the catches for the fish.
	 */
	int totalCatches;

	/**
	 * Record catch for the fish.
	 */
	double recordWeight;

	/**
	 * Average weight for catches. Accounts for catches with not null weight.
	 */
	double averageWeight;

	/**
	 * Average depth for catches. Accounts for catches with not null depth.
	 */
	double averageDepth;

	/**
	 * The summer hour 0-24 with the most catches.
	 */
	Integer mostCommonSummerHour;

	/**
	 * The winter hour 0-24 with the most catches.
	 */
	Integer mostCommonWinterHour;

	Map<Integer, Integer> catchesPerMonth;

	/**
	 * The number of catches per hour of day (24), per {@link Season}
	 */
	Map<Season, Map<Integer, Integer>> catchesPerHourPerSeason;

	public FishAverageStatistic(){
		this.fishAvgId = Database.INVALID_ID;
	}

	public FishAverageStatistic(int fishAvgId, int fishId, int totalCatches, double avgDepth,
			double avgWeight, int mostCommonSummerHour, int mostCommonWinterHour, double recordWeight) {
		this.fishAvgId = fishAvgId;
		this.fishId = fishId;
		this.totalCatches = totalCatches;
		this.averageDepth = avgDepth;
		this.averageWeight = avgWeight;
		this.mostCommonSummerHour = mostCommonSummerHour;
		this.mostCommonWinterHour = mostCommonWinterHour;
		this.recordWeight = recordWeight;
	}

	public int getTotalCatches() {
		return totalCatches;
	}

	public void setTotalCatches(int totalCatches) {
		this.totalCatches = totalCatches;
	}

	public double getAverageWeight() {
		return averageWeight;
	}

	public void setAverageWeight(double averageWeight) {
		this.averageWeight = averageWeight;
	}

	public double getAverageDepth() {
		return averageDepth;
	}

	public void setAverageDepth(double averageDepth) {
		this.averageDepth = averageDepth;
	}

	public Integer getMostCommonSummerHour() {
		return mostCommonSummerHour;
	}

	public void setMostCommonSummerHour(Integer mostCommonSummerHour) {
		this.mostCommonSummerHour = mostCommonSummerHour;
	}
	
	public Integer getMostCommonWinterHour() {
		return mostCommonWinterHour;
	}

	public void setMostCommonWinterHour(Integer mostCommonWinterHour) {
		this.mostCommonWinterHour = mostCommonWinterHour;
	}

	public Fish getFish() {
		return fish;
	}

	public void setFish(Fish fish) {
		this.fish = fish;
	}

	public double getRecordWeight() {
		return recordWeight;
	}

	public void setRecordWeight(double recordWeight) {
		this.recordWeight = recordWeight;
	}

	public Integer getFishId() {
		return fishId;
	}

	public void setFishId(Integer fishId) {
		this.fishId = fishId;
	}

	public void setCatchesPerMonth(Map<Integer,Integer> catchesPerMonth) {
		this.catchesPerMonth = catchesPerMonth;
	}

	public Map<Integer, Integer> getCatchesPerMonth() {
		return catchesPerMonth;
	}

	public void setCatchesPerHourPerSeason(Map<Season, Map<Integer, Integer>> catchesPerHourPerSeason) {
		this.catchesPerHourPerSeason = catchesPerHourPerSeason;
	}

	public Map<Season, Map<Integer, Integer>> getCatchesPerHourPerSeason() {
		return catchesPerHourPerSeason;
	}

//	public static FishAverageStatistic getFromId(Context context, long id) {
//        //Log.v(TAG, String.format("Requesting item [%d]", id));
//        synchronized (context) {
//            Cursor cursor = null;
//            try {
//                cursor = context.getContentResolver()
//                        .query(Uri.withAppendedPath(ContentDescriptor.FishAverageStatistic.CONTENT_URI,
//                                String.valueOf(id)), null, null, null, null);
//                cursor.moveToFirst();
//                FishAverageStatistic fromCursor = createFromCursor(cursor);
//                 fromCursor.setFish(Fish.getFromId(context, fromCursor.getFishId()));
//                 return fromCursor;
//            } finally {
//                if (cursor != null)
//                    cursor.close();
//            }
//        }
//    }

    public static ContentValues asContentValues(FishAverageStatistic item) {
        if (item == null)
            return null;
        synchronized (item) {
            ContentValues toRet = new ContentValues();
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID, item.fishAvgId);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.FISHID, item.fishId);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.TOTAL_CATCHES, item.totalCatches);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.RECORD_WEIGHT, item.recordWeight);
            
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.AVG_DEPTH, item.averageDepth);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.AVG_WEIGHT, item.averageWeight);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_SUMMER_HOUR, item.mostCommonSummerHour);
            toRet.put(ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_WINTER_HOUR, item.mostCommonWinterHour);
            return toRet;
        }
    }

//    public static FishAverageStatistic createFromCursor(Cursor cursor) {
//        synchronized (cursor) {
//            if (cursor.isClosed() || cursor.isAfterLast() || cursor.isBeforeFirst()) {
//                //Log.v(TAG, String.format("Requesting entity but no valid cursor"));
//                return null;
//            }
//            FishAverageStatistic toRet = new FishAverageStatistic();
//            toRet.fishAvgId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID));
//            toRet.fishId = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.FISHID));
//            toRet.totalCatches = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.TOTAL_CATCHES));
//            toRet.recordWeight = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.RECORD_WEIGHT));
//            toRet.averageDepth = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.AVG_DEPTH));
//            toRet.averageWeight = cursor.getDouble(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.AVG_WEIGHT));
//            toRet.mostCommonSummerHour = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_SUMMER_HOUR));
//            toRet.mostCommonWinterHour = cursor.getInt(cursor.getColumnIndex(ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_WINTER_HOUR));
//
//            return toRet;
//        }
//    }

//    /**
//     * let the id decide if we have an insert or an update
//     *
//     * @param resolver
//     * @param item
//     */
//    public static void save(ContentResolver resolver, FishAverageStatistic item) {
//        if (item.fishAvgId == Database.INVALID_ID)
//            resolver.insert(ContentDescriptor.FishAverageStatistic.CONTENT_URI, FishAverageStatistic.asContentValues(item));
//        else
//            resolver.update(ContentDescriptor.FishAverageStatistic.CONTENT_URI, FishAverageStatistic.asContentValues(item),
//                    String.format("%s=?", ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID),
//                    new String[]{
//                            String.valueOf(item.fishAvgId)
//                    });
//    }

	@Override
	public int compareTo(FishAverageStatistic arg0) {
		return this.totalCatches - arg0.getTotalCatches();
	}

}
