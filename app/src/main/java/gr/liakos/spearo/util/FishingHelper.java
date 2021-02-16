package gr.liakos.spearo.util;

import gr.liakos.spearo.bean.ListPair;
import gr.liakos.spearo.enums.Season;
import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class FishingHelper {
	
	/**
	 * A session has many fish.
	 * A user has many sessions.
	 * We need to produce a map of a {@link FishStatistic} per fish.
	 * 
	 * @param sessionsToPrepareForUpload
	 * @param isForMongoUpload
	 * @return
	 */
	public static Map<Fish, FishStatistic> convertUserSessionsToStats(List<FishingSession> sessionsToPrepareForUpload, boolean isForMongoUpload){
		Map<Fish, FishStatistic>  toBeReturned = new HashMap<Fish, FishStatistic>();
		for (FishingSession fishingSession : sessionsToPrepareForUpload) {
			if (isForMongoUpload){
				if (fishingSession.isUploadedToMongo()){
					continue;
				}
			}
			Map<Fish, FishStatistic> converted = session2stat(fishingSession);
			updateEntries(toBeReturned, converted);
		}
		return toBeReturned;
	}
	
	/**
	 * We have statistics for species. We need to convert them to average stats.
	 * 
	 * @param ctx
	 * @param stats
	 * @param mode
	 * @return
	 */
	public static List<FishAverageStatistic> getAverageStatsFrom(Context ctx, List<FishStatistic> stats, StatMode mode){
		List<FishAverageStatistic> averageStats = new ArrayList<FishAverageStatistic>();
		for (FishStatistic stat : stats){
			Fish fish = Fish.getFromId(ctx,  stat.getFishId());
			if (fish == null){//fish in mongo but not in db
				continue;
			}
			
        	FishAverageStatistic avgStat = new  FishAverageStatistic();
			avgStat.setFish(fish);
        	avgStat.setFishId(stat.getFishId());
        	avgStat.setTotalCatches(stat.getTotalCatches());
        	Integer mostCommonSummerHourFromStat = getMostCommonHourFromStat(stat, Season.SUMMER);
			avgStat.setMostCommonSummerHour(mostCommonSummerHourFromStat);
			
			Integer mostCommonWinterHourFromStat = getMostCommonHourFromStat(stat, Season.WINTER);
			avgStat.setMostCommonWinterHour(mostCommonWinterHourFromStat);
        	
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
        	averageStats.add(avgStat);
        }
		
		return averageStats;
	}
	
	/**
	 * The first map holds the sum stats of the process until now.
	 * The second map is the new incoming converted session.
	 * 
	 * We increase the values of the first with the values in the second,
	 * or insert any missing ones.
	 * 
	 * @param existingStats
	 * @param newStats
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
			existingStats.put(fish, existingStat);
		}
		
	}

	/**
	 * A session has many catches. Some are of the same species.
	 * We separate same species in a map.
	 * 
	 * 1. Used when converting FOR mongo upload.
	 * 
	 * @param fishingSession
	 * @return
	 */
	static Map<Fish, FishStatistic> session2stat(FishingSession fishingSession) {
		Map<Fish, FishStatistic> session2stat = new HashMap<Fish, FishStatistic>();
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
		if (Season.SUMMER.equals(season)){
		
			Integer max = -1;
			
			
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
			return max;
		
		}else{
			Integer max = -1;
			
			
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
			return max;
		}
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
	 * @param newSeasonCatchesPerHourDay
	 * @param oldSeasonCatchesPerHourDay
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
	
	
	public static ListPair divideIntoMonths(List<FishingSession> allSessions, Context ctx){
		Map<String, List<FishingSession>> sessionsPerMonth = new HashMap<String, List<FishingSession>>();
		for (FishingSession fishingSession : allSessions) {
			String monthAndYear = DateUtils.getMonthAndYear(fishingSession, ctx);
			List<FishingSession> existing = sessionsPerMonth.get(monthAndYear);
			if (existing == null){
				existing = new ArrayList<FishingSession>();
				sessionsPerMonth.put(monthAndYear, existing);
			}
			existing.add(fishingSession);
		}
		return sessionListsPerMonth(sessionsPerMonth);
	}
	
	static ListPair sessionListsPerMonth(Map<String, List<FishingSession>> sessionsMap){
		ListPair lp = new ListPair();
		for (Map.Entry<String, List<FishingSession>> entry : sessionsMap.entrySet() ){
			lp.getParents().add(entry.getKey());
			List<FishingSession> fishingSessions = entry.getValue();
			Collections.sort(fishingSessions);
			Collections.reverse(fishingSessions);
			lp.getChildren().add(fishingSessions);
		}
		return lp;
	}

}
