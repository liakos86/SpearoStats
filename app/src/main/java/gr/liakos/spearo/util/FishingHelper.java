package gr.liakos.spearo.util;

import gr.liakos.spearo.enums.Season;
import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class FishingHelper {
	
	/**
	 * A session has many fish.
	 * A user has many sessions.
	 * We need to produce a map of a {@link FishStatistic} per fish that the user
	 * has caught among ALL his {@link FishingSession}s.
	 * 
	 * @param sessionsToPrepareForUpload all the user's {@link FishingSession}s
	 * @param isForMongoUpload flag for mongo upload of the stats.
	 *
	 * @return stats per fish
	 */
	public static Map<Fish, FishStatistic> convertUserSessionsToStats(List<FishingSession> sessionsToPrepareForUpload, boolean isForMongoUpload){
		Map<Fish, FishStatistic>  toBeReturned = new HashMap<>();
		for (FishingSession fishingSession : sessionsToPrepareForUpload) {
			if (isForMongoUpload){
				if (fishingSession.isUploadedToMongo()){
					continue;
				}
			}
			Map<Fish, FishStatistic> converted = fishingSessionToFishStatistics(fishingSession);
			updateEntries(toBeReturned, converted);
		}
		return toBeReturned;
	}
	
	/**
	 * We have {@link FishStatistic}s for species. Those stats contain sums of catches, or kilograms for every fish.
	 * We need to convert them to {@link FishAverageStatistic}s, by utilising the input stats.
	 * When we are in {@link StatMode#PERSONAL} the record weight is the user's record weight.
	 * Else in {@link StatMode#GLOBAL} we prefer the community record from mongo.
	 * 
	 * @param ctx context
	 * @param stats statistics
	 * @param mode the mode
	 * @return average stats per fish
	 */
	public static List<FishAverageStatistic> getAverageStatsFrom(Context ctx, List<FishStatistic> stats, StatMode mode){
		List<FishAverageStatistic> averageStats = new ArrayList<>();
		for (FishStatistic stat : stats){
			Fish fish = Fish.getFromId(ctx,  stat.getFishId());
			if (fish == null){/* fish in mongo but not in db */
				continue;
			}

			FishAverageStatistic avgStat = convertStatToAverageStat(stat, mode, ctx);
        	averageStats.add(avgStat);
        }
		
		return averageStats;
	}

	/**
	 * Converts a {@link FishStatistic to a FishAverageStatistic}.
	 *
	 * @param stat statistic
	 * @param mode mode
	 * @param ctx context
	 *
	 * @return average stat from simple stat
	 */
	static FishAverageStatistic convertStatToAverageStat(FishStatistic stat, StatMode mode, Context ctx) {
		Fish fish = Fish.getFromId(ctx,  stat.getFishId());
		FishAverageStatistic avgStat = new  FishAverageStatistic();
		avgStat.setFish(fish);
		avgStat.setFishId(stat.getFishId());
		avgStat.setTotalCatches(stat.getTotalCatches());
		Integer mostCommonSummerHourFromStat = getMostCommonHourFromStat(stat, Season.SUMMER);
		avgStat.setMostCommonSummerHour(mostCommonSummerHourFromStat);

		Integer mostCommonWinterHourFromStat = getMostCommonHourFromStat(stat, Season.WINTER);
		avgStat.setMostCommonWinterHour(mostCommonWinterHourFromStat);
		avgStat.setCatchesPerHourPerSeason(stat.getSeasonCatchesPerHourDay());

		if (mode.equals(StatMode.PERSONAL)){
			avgStat.setRecordWeight(stat.getRecordWeight());
		}else{
			double recWeight = (stat.getRecordWeight() > avgStat.getFish().getRecordCatchWeight()) ? stat.getRecordWeight() : avgStat.getFish().getRecordCatchWeight();
			avgStat.setRecordWeight(recWeight);
		}

		if (stat.getDepthedCatches() > 0 ){
			avgStat.setAverageDepth(stat.getTotalDepth() / stat.getDepthedCatches());
		}

		if (stat.getWeightedCatches() > 0){
			avgStat.setAverageWeight(stat.getTotalWeight() / stat.getWeightedCatches());
		}
		return avgStat;
	}

	/**
	 * We divide the fish catches to catches per month, for every fish.
	 * Used for statistics diagrams.
	 *
	 * @param sessions all user's {@link FishingSession}s
	 * @return fish per month
	 */
	public static Map<Fish, Map<Integer, Integer>> convertUserSessionsToMonthlyCatches(List<FishingSession> sessions){
		Map<Fish, Map<Integer, Integer>>  toBeReturned = new HashMap<>();
		for (FishingSession fishingSession : sessions) {
			Calendar calendarWithTime = DateUtils.getCalendarWithTime(fishingSession.getFishingDate());
			Integer month = calendarWithTime.get(Calendar.MONTH);
			for (FishCatch fishCatch : fishingSession.getFishCatches()){
				Map<Integer, Integer> fishCatchesPerMonth = toBeReturned.get(fishCatch.getFish());
				if (fishCatchesPerMonth == null){
					fishCatchesPerMonth = initializeMonthsMap();
					toBeReturned.put(fishCatch.getFish(), fishCatchesPerMonth);
				}

				Integer increasedCatches = fishCatchesPerMonth.get(month) + 1;
				fishCatchesPerMonth.put(month, increasedCatches);
			}
		}
		return toBeReturned;
	}

	/**
	 * The map is initialized with zero fish per month for all species.
	 *
	 * @return map of catches per month
	 */
	static Map<Integer, Integer> initializeMonthsMap(){
		Map<Integer, Integer> catchesPerMonthMap = new HashMap<>();
		catchesPerMonthMap.put(Calendar.JANUARY, 0);
		catchesPerMonthMap.put(Calendar.FEBRUARY, 0);
		catchesPerMonthMap.put(Calendar.MARCH, 0);
		catchesPerMonthMap.put(Calendar.APRIL, 0);
		catchesPerMonthMap.put(Calendar.MAY, 0);
		catchesPerMonthMap.put(Calendar.JUNE, 0);
		catchesPerMonthMap.put(Calendar.JULY, 0);
		catchesPerMonthMap.put(Calendar.AUGUST, 0);
		catchesPerMonthMap.put(Calendar.SEPTEMBER, 0);
		catchesPerMonthMap.put(Calendar.OCTOBER, 0);
		catchesPerMonthMap.put(Calendar.NOVEMBER, 0);
		catchesPerMonthMap.put(Calendar.DECEMBER, 0);
		return catchesPerMonthMap;
	}
	
	/**
	 * The first map holds the sum stats of the process until now.
	 * The second map is the new incoming converted session's {@link FishStatistic}s.
	 * We increase the values of the first with the values in the second,
	 * or insert any missing ones.
	 * 
	 * @param existingStats the existing stats of all the {@link FishingSession}s so far.
	 * @param newStats the stats of a {@link FishingSession}
	 */
	static void updateEntries(Map<Fish, FishStatistic> existingStats,
			Map<Fish, FishStatistic> newStats) {
		for (Map.Entry<Fish, FishStatistic> session2statEntry : newStats.entrySet()){
			Fish fish = session2statEntry.getKey();
			FishStatistic existingStat = existingStats.get(fish);
			FishStatistic incomingStat = session2statEntry.getValue();
			if (existingStat == null){
				existingStats.put(fish, incomingStat);
				continue;
			}
			
			if (incomingStat.getRecordWeight() > existingStat.getRecordWeight()){
				existingStat.setRecordWeight(incomingStat.getRecordWeight());
			}
			
			existingStat.setTotalCatches(existingStat.getTotalCatches() + incomingStat.getTotalCatches());
			existingStat.setDepthedCatches(existingStat.getDepthedCatches() + incomingStat.getDepthedCatches());
			existingStat.setWeightedCatches(existingStat.getWeightedCatches() + incomingStat.getWeightedCatches());
			existingStat.setTotalDepth(existingStat.getTotalDepth() + incomingStat.getTotalDepth());
			existingStat.setTotalWeight(existingStat.getTotalWeight() + incomingStat.getTotalWeight());
			increaseCatchHours(incomingStat, existingStat);
			existingStats.put(fish, existingStat);//this is unnecessary
		}
		
	}

	/**
	 * A session has many catches. Some are of the same species.
	 * We separate same species in a map.
	 * i.e if user has caught 2 seriola and 2 sargo, the map will have two entries,
	 * one for every {@link Fish} with the relevant data.
 	 *
	 * 1. Used when converting FOR mongo upload.
	 * 
	 * @param fishingSession user session
	 * @return statistics of a single session.
	 */
	static Map<Fish, FishStatistic> fishingSessionToFishStatistics(FishingSession fishingSession) {
		Map<Fish, FishStatistic> session2stat = new HashMap<>();
		Season sessionSeason = DateUtils.seasonFromSession(fishingSession);
		for (FishCatch fishCatch : fishingSession.getFishCatches()) {
			FishStatistic fishStatistic = session2stat.get(fishCatch.getFish());
			if (fishStatistic == null){
				fishStatistic = new FishStatistic();
				fishStatistic.setFishId(fishCatch.getFishId());
			}
			fishStatistic.setTotalCatches(fishStatistic.getTotalCatches() + 1);
			increaseCatchHour(sessionSeason, fishCatch.getCatchHour(), fishStatistic);
			increaseDepthMeters(fishCatch.getDepthMeters(), fishStatistic);
			increaseCatchWeight(fishCatch.getWeight(), fishStatistic);
			session2stat.put(fishCatch.getFish(), fishStatistic);
		}
		
		return session2stat;
	}
	
	static Integer getMostCommonHourFromStat(FishStatistic stat, Season season){
		Integer max = -1;
		if (Season.SUMMER.equals(season)){
			max = max(stat.getHourSummer0(), max, 0);
			max = max(stat.getHourSummer1(), max, 1);
			max = max(stat.getHourSummer2(), max, 2);
			max = max(stat.getHourSummer3(), max, 3);
			max = max(stat.getHourSummer4(), max, 4);
			max = max(stat.getHourSummer5(), max, 5);
			max = max(stat.getHourSummer6(), max, 6);
			max = max(stat.getHourSummer7(), max, 7);
			max = max(stat.getHourSummer8(), max, 8);
			max = max(stat.getHourSummer9(), max, 9);
			max = max(stat.getHourSummer10(), max, 10);
			max = max(stat.getHourSummer11(), max, 11);
			max = max(stat.getHourSummer12(), max, 12);
			max = max(stat.getHourSummer13(), max, 13);
			max = max(stat.getHourSummer14(), max, 14);
			max = max(stat.getHourSummer15(), max, 15);
			max = max(stat.getHourSummer16(), max, 16);
			max = max(stat.getHourSummer17(), max, 17);
			max = max(stat.getHourSummer18(), max, 18);
			max = max(stat.getHourSummer19(), max, 19);
			max = max(stat.getHourSummer20(), max, 20);
			max = max(stat.getHourSummer21(), max, 21);
			max = max(stat.getHourSummer22(), max, 22);
			max = max(stat.getHourSummer23(), max, 23);
		}else{
			max = max(stat.getHourWinter0(), max, 0);
			max = max(stat.getHourWinter1(), max, 1);
			max = max(stat.getHourWinter2(), max, 2);
			max = max(stat.getHourWinter3(), max, 3);
			max = max(stat.getHourWinter4(), max, 4);
			max = max(stat.getHourWinter5(), max, 5);
			max = max(stat.getHourWinter6(), max, 6);
			max = max(stat.getHourWinter7(), max, 7);
			max = max(stat.getHourWinter8(), max, 8);
			max = max(stat.getHourWinter9(), max, 9);
			max = max(stat.getHourWinter10(), max, 10);
			max = max(stat.getHourWinter11(), max, 11);
			max = max(stat.getHourWinter12(), max, 12);
			max = max(stat.getHourWinter13(), max, 13);
			max = max(stat.getHourWinter14(), max, 14);
			max = max(stat.getHourWinter15(), max, 15);
			max = max(stat.getHourWinter16(), max, 16);
			max = max(stat.getHourWinter17(), max, 17);
			max = max(stat.getHourWinter18(), max, 18);
			max = max(stat.getHourWinter19(), max, 19);
			max = max(stat.getHourWinter20(), max, 20);
			max = max(stat.getHourWinter21(), max, 21);
			max = max(stat.getHourWinter22(), max, 22);
			max = max(stat.getHourWinter23(), max, 23);
		}

		return max;
	}
	
	/**
	 * Two stats for the same fish species.
	 * The first is coming from a new session.
	 * The second is the aggregation of the sessions so far.
	 * 
	 * @param newStat
	 * @param existingStat
	 */
	static void increaseCatchHours(FishStatistic newStat,
			FishStatistic existingStat) {
		mergeHourMaps(newStat.getSeasonCatchesPerHourDay(), existingStat.getSeasonCatchesPerHourDay());
	}

	/**
	 * Replace the old entry with the sum of the two entry values.
	 * 
	 * @param newSeasonCatchesPerHourDay the incoming values
	 * @param oldSeasonCatchesPerHourDay the existing values
	 */
	static void mergeHourMaps(Map<Season, Map<Integer, Integer>> newSeasonCatchesPerHourDay,
			Map<Season, Map<Integer, Integer>> oldSeasonCatchesPerHourDay) {
		for (Integer i = 0; i < 24; i++){
			for (Season season : Season.values()){
				Map<Integer, Integer> oldMap = oldSeasonCatchesPerHourDay.get(season);
				Map<Integer, Integer> newMap = newSeasonCatchesPerHourDay.get(season);
				oldMap.put(i, oldMap.get(i) + newMap.get(i));
			}
		}
	}
	
	static void increaseCatchWeight(double weight, FishStatistic fishStat) {
		if (weight > 0){
			fishStat.setWeightedCatches(fishStat.getWeightedCatches() + 1);
			fishStat.setTotalWeight(fishStat.getTotalWeight() + weight);
			
			if (weight > fishStat.getRecordWeight()){
				fishStat.setRecordWeight(weight);
			}
		}
	}

	static void increaseDepthMeters(double depthMeters, FishStatistic fishStat) {
		if (depthMeters > 0){
			fishStat.setDepthedCatches(fishStat.getDepthedCatches() + 1);
			fishStat.setTotalDepth(fishStat.getTotalDepth() + depthMeters);
		}
	}
	
	static void increaseCatchHour(Season season, Integer fishCatchHour, FishStatistic fishStat) {
		if (fishCatchHour != null){
			Map<Integer, Integer> seasonHours = fishStat.getSeasonCatchesPerHourDay().get(season);
			Integer hourCatches = seasonHours.get(fishCatchHour);
			hourCatches = hourCatches + 1;
			seasonHours.put(fishCatchHour, hourCatches);
		}
	}
	
	static Integer max(Integer num1, Integer max, Integer candidate){
		if (num1 > max){
			return candidate;
		}
		return max;
	}

	/**
	 * {@link FishAverageStatistic#getCatchesPerHourPerSeason()} hold the number of catches
	 * per hour of day, per {@link Season}. The {@link FishStatistic#getHourSummer0()} etc. hold
	 * the catches of every hour. We collect those in a map per fish.
	 *
	 * @param communityAvgStats avg stats from community stats
	 * @param communityStats stats from mongo
	 */
	public static void assignCatchesPerHourPerSeasonGlobal(List<FishAverageStatistic> communityAvgStats, List<FishStatistic> communityStats) {
		for (FishAverageStatistic avgStat: communityAvgStats) {
			Fish fish = avgStat.getFish();
			for (FishStatistic stat : communityStats){
				if (Integer.valueOf(stat.getFishId() ).equals(fish.getFishId())){
					Map<Season, Map<Integer, Integer>> catchesPerHourPerSeason = new HashMap<>();
					catchesPerHourPerSeason.put(Season.SUMMER, getSummerHoursGlobal(stat));
					catchesPerHourPerSeason.put(Season.WINTER, getWinterHoursGlobal(stat));
					avgStat.setCatchesPerHourPerSeason(catchesPerHourPerSeason);
				}
			}
		}
	}

	static Map<Integer, Integer> getWinterHoursGlobal(FishStatistic stat) {
		Map<Integer, Integer> winterHoursGlobal = new HashMap<>();
		winterHoursGlobal.put(0, stat.getHourWinter0());
		winterHoursGlobal.put(1, stat.getHourWinter1());
		winterHoursGlobal.put(2, stat.getHourWinter2());
		winterHoursGlobal.put(3, stat.getHourWinter3());
		winterHoursGlobal.put(4, stat.getHourWinter4());
		winterHoursGlobal.put(5, stat.getHourWinter5());
		winterHoursGlobal.put(6, stat.getHourWinter6());
		winterHoursGlobal.put(7, stat.getHourWinter7());
		winterHoursGlobal.put(8, stat.getHourWinter8());
		winterHoursGlobal.put(9, stat.getHourWinter9());
		winterHoursGlobal.put(10, stat.getHourWinter10());
		winterHoursGlobal.put(11, stat.getHourWinter11());
		winterHoursGlobal.put(12, stat.getHourWinter12());
		winterHoursGlobal.put(13, stat.getHourWinter13());
		winterHoursGlobal.put(14, stat.getHourWinter14());
		winterHoursGlobal.put(15, stat.getHourWinter15());
		winterHoursGlobal.put(16, stat.getHourWinter16());
		winterHoursGlobal.put(17, stat.getHourWinter17());
		winterHoursGlobal.put(18, stat.getHourWinter18());
		winterHoursGlobal.put(19, stat.getHourWinter19());
		winterHoursGlobal.put(20, stat.getHourWinter20());
		winterHoursGlobal.put(21, stat.getHourWinter21());
		winterHoursGlobal.put(22, stat.getHourWinter22());
		winterHoursGlobal.put(23, stat.getHourWinter23());
		return winterHoursGlobal;
	}

	static Map<Integer, Integer> getSummerHoursGlobal(FishStatistic stat) {
		Map<Integer, Integer> summerHoursGlobal = new HashMap<>();
		summerHoursGlobal.put(0, stat.getHourSummer0());
		summerHoursGlobal.put(1, stat.getHourSummer1());
		summerHoursGlobal.put(2, stat.getHourSummer2());
		summerHoursGlobal.put(3, stat.getHourSummer3());
		summerHoursGlobal.put(4, stat.getHourSummer4());
		summerHoursGlobal.put(5, stat.getHourSummer5());
		summerHoursGlobal.put(6, stat.getHourSummer6());
		summerHoursGlobal.put(7, stat.getHourSummer7());
		summerHoursGlobal.put(8, stat.getHourSummer8());
		summerHoursGlobal.put(9, stat.getHourSummer9());
		summerHoursGlobal.put(10, stat.getHourSummer10());
		summerHoursGlobal.put(11, stat.getHourSummer11());
		summerHoursGlobal.put(12, stat.getHourSummer12());
		summerHoursGlobal.put(13, stat.getHourSummer13());
		summerHoursGlobal.put(14, stat.getHourSummer14());
		summerHoursGlobal.put(15, stat.getHourSummer15());
		summerHoursGlobal.put(16, stat.getHourSummer16());
		summerHoursGlobal.put(17, stat.getHourSummer17());
		summerHoursGlobal.put(18, stat.getHourSummer18());
		summerHoursGlobal.put(19, stat.getHourSummer19());
		summerHoursGlobal.put(20, stat.getHourSummer20());
		summerHoursGlobal.put(21, stat.getHourSummer21());
		summerHoursGlobal.put(22, stat.getHourSummer22());
		summerHoursGlobal.put(23, stat.getHourSummer23());
		return summerHoursGlobal;
	}

}
