package gr.liakos.spearo.util;

import gr.liakos.spearo.model.ContentDescriptor;

public class DbColumns {
	
	 public static String[] fromFishCatch(){ 
		 String[] FROM = {
             // ! beware. I mark the position of the fields
             ContentDescriptor.FishCatch.Cols.FISHCATCHID,
             ContentDescriptor.FishCatch.Cols.FISHINGSESSIONID,
             ContentDescriptor.FishCatch.Cols.FISHID,
             ContentDescriptor.FishCatch.Cols.CATCH_TIME_MINUTES,
             ContentDescriptor.FishCatch.Cols.CATCH_HOUR,
             ContentDescriptor.FishCatch.Cols.WEIGHT,
             ContentDescriptor.FishCatch.Cols.DEPTH
     };
	 return FROM;
	 }
	 
	 
	 public static String[] fromFish(){
	 String [] columns = new String[]{
 			ContentDescriptor.Fish.Cols.FISHID,
             ContentDescriptor.Fish.Cols.LATINNAME,
             ContentDescriptor.Fish.Cols.RECORDWEIGHT,
          
             ContentDescriptor.Fish.Cols.FISHFAMILY,
             ContentDescriptor.Fish.Cols.MAXALLOWEDCATCHWEIGHT,
             ContentDescriptor.Fish.Cols.CONCERN
 	};
	 return columns;
	 }
	 
	 public static String[] fromFishAvg(){
		 String [] columns = new String[]{
				 ContentDescriptor.FishAverageStatistic.Cols.FISHAVGID,
	 			 ContentDescriptor.FishAverageStatistic.Cols.FISHID,
	             ContentDescriptor.FishAverageStatistic.Cols.TOTAL_CATCHES,
	          
	             ContentDescriptor.FishAverageStatistic.Cols.AVG_DEPTH,
	             ContentDescriptor.FishAverageStatistic.Cols.AVG_WEIGHT,
	             ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_SUMMER_HOUR,
	             ContentDescriptor.FishAverageStatistic.Cols.MOST_COMMON_WINTER_HOUR,
	             ContentDescriptor.FishAverageStatistic.Cols.RECORD_WEIGHT
	             
	 	};
		 return columns;
		 }

	 public static String[] fromSession(){
		 String[] FROM = {
	                // ! beware. I mark the position of the fields
	                ContentDescriptor.FishingSession.Cols.FISHINGSESSIONID,
	                ContentDescriptor.FishingSession.Cols.FISHINGDATE,
	                ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLAT,
	                ContentDescriptor.FishingSession.Cols.FISHINGSESSIONLON,
	                ContentDescriptor.FishingSession.Cols.UPLOADED,
	                ContentDescriptor.FishingSession.Cols.SESSION_IMAGE,
	               // ContentDescriptor.FishingSession.Cols.SESSION_MOON,
	                ContentDescriptor.FishingSession.Cols.SESSION_WIND,
				 	ContentDescriptor.FishingSession.Cols.SESSION_WIND_VOLUME
	                };
	 
		 return FROM;
	 }
	 
}
