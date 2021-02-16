package gr.liakos.spearo.util;

import android.content.SharedPreferences;

public class MetricConverter {
	
	public static double convertDepthFromPickers(SharedPreferences app_preferences, double value){
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		if (isMetric){
			return value;
		}
		
		return value / Constants.METER_TO_FEET;
	}
	

	public static double convertWeightFromPickers(SharedPreferences app_preferences, double value){
		boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
		if (isMetric){
			return value;
		}
		
		return value / Constants.KG_TO_POUNDS;
	}

	public static String convertWeightFromValueStr(boolean isMetric, double value){
		double weight = convertWeightFromValue(isMetric, value);
		if (isMetric){
			return String.format("%.2f", weight) + Constants.KG;
		}
		
		return String.format("%.2f", weight) + Constants.LBS;
	}
	
	static double convertWeightFromValue(boolean isMetric, double value){
		if (isMetric){
			return value;
		}
		
		return value * Constants.KG_TO_POUNDS;
	}
	
	public static String convertDepthFromValueStr(boolean isMetric, double value){
		double depth = convertDepthFromValue(isMetric, value);
		if (isMetric){
			return String.format("%.2f", depth) + Constants.METERS;
		}
		
		return String.format("%.2f", depth) + Constants.FEET;
	}
	
	static double convertDepthFromValue(boolean isMetric, double value){
		if (isMetric){
			return value;
		}
		
		return value * Constants.METER_TO_FEET;
	}
	
	public static String weightSuffix(boolean isMetric){
		if (isMetric){
			return Constants.KG;
		}
		
		return Constants.LBS;
	}
	
	public static String depthSuffix(boolean isMetric){
		if (isMetric){
			return Constants.METERS;
		}
		
		return Constants.FEET;
	}
	
}
