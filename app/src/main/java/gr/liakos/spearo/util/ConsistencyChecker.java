package gr.liakos.spearo.util;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.FishingSession;

public class ConsistencyChecker {
	
	public static boolean isSuspiciousSession(FishingSession session){
		List<FishCatch> fishCatches = session.getFishCatches();
		return over50perCentOfOfficialRecordExists(fishCatches);
	}


	private static boolean over50perCentOfOfficialRecordExists(
			List<FishCatch> fishCatches) {
		for (FishCatch fishCatch : fishCatches) {
			Double catchWeight = fishCatch.getWeight();
			if (catchWeight == null || catchWeight == 0){
				continue;
			}

			Double recordCatchWeight = fishCatch.getFish().getRecordCatchWeight();
			if (recordCatchWeight < 0){
				recordCatchWeight = fishCatch.getFish().getMaxAllowedCatchWeight();
			}
			
			
			if (recordCatchWeight / catchWeight < 2){
				return true;
			}
		}
		return false;
	}


	public static boolean isSuspiciousUser(SpearoApplication application, int sessionsSize) {
		if (sessionsSize <= 5){
			return false;
		}
		
		SharedPreferences app_preferences = application.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		int suspiciousCount = app_preferences.getInt(Constants.SUSPICIOUS_COUNT, 0);
		int suspiciousStreak = app_preferences.getInt(Constants.SUSPICIOUS_STREAK, 0);
		
		if (suspiciousStreak >= 5){
			return true;
		}
		
		if (suspiciousCount > 0 && sessionsSize / suspiciousCount <= 3){
			return true;
		}

		if (suspiciousCount >= 30){
			return true;
		}
		
		
		return false;
	}

}
