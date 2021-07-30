package gr.liakos.spearo.util;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.Season;
import gr.liakos.spearo.model.object.FishingSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;

public class DateUtils {
	
	public static String dateFromMillis(Long millis){
		DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
        Calendar calendar = getCalendarWithTime(millis);
        String dateString = formatter.format(calendar.getTime());
        return dateString;
	}
	
	public static Calendar getCalendarWithTime(Long millis){
		Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
	}
	
	public static String getDayAndMonth(Calendar cal, Context context){
		return getDayName(cal.get(Calendar.DAY_OF_WEEK), context) + Constants.COMMA_SEP + cal.get(Calendar.DATE) + Constants.SPACE + getMonthName(cal.get(Calendar.MONTH), context);// cal.get(Calendar.DATE));//(DateUtils.dateFromMillis(fishingSession.getFishingDate()));
	}
	
	static String getDayName(int pos, Context context){
		Resources resources = context.getResources();
		switch (pos){
			case Calendar.MONDAY:
				return resources.getString(R.string.mon);
			case Calendar.TUESDAY:
				return resources.getString(R.string.tue);
			case Calendar.WEDNESDAY:
				return resources.getString(R.string.wed);
			case Calendar.THURSDAY:
				return resources.getString(R.string.thu);
			case Calendar.FRIDAY:
				return resources.getString(R.string.fri);
			case Calendar.SATURDAY:
				return resources.getString(R.string.sat);
			case Calendar.SUNDAY:
				return resources.getString(R.string.sun);
			default:
				return Constants.EMPTY;
		}
	}
	
	public static String getMonthName(int pos, Context context){
		Resources resources = context.getResources();
		switch (pos){
			case Calendar.JANUARY:
				return resources.getString(R.string.jan);
			case Calendar.FEBRUARY:
				return resources.getString(R.string.feb);
			case Calendar.MARCH:
				return resources.getString(R.string.mar);
			case Calendar.APRIL:
				return resources.getString(R.string.apr);
			case Calendar.MAY:
				return resources.getString(R.string.may);
			case Calendar.JUNE:
				return resources.getString(R.string.jun);
			case Calendar.JULY:
				return resources.getString(R.string.jul);
			case Calendar.AUGUST:
				return resources.getString(R.string.aug);
			case Calendar.SEPTEMBER:
				return resources.getString(R.string.sep);
			case Calendar.OCTOBER:
				return resources.getString(R.string.oct);
			case Calendar.NOVEMBER:
				return resources.getString(R.string.nov);
			case Calendar.DECEMBER:
				return resources.getString(R.string.dec);
			default:
				return resources.getString(R.string.no_date);
				
		}
				
	}
	
	static Season seasonByMonth(int pos){
		switch (pos){
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				return Season.WINTER;
			
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				return Season.SUMMER;
			
			default:
				return Season.SUMMER;
		}
	}

	public static String getMonthAndYear(FishingSession fishingSession, Context context) {
		Calendar cal = getCalendarWithTime(fishingSession.getFishingDate());
		return getMonthName(cal.get(Calendar.MONTH), context) +  Constants.SPACE + String.valueOf(cal.get(Calendar.YEAR));
	}

	public static Season seasonFromSession(FishingSession fishingSession) {
		Calendar cal = getCalendarWithTime(fishingSession.getFishingDate());
		int month = cal.get(Calendar.MONTH);
		return seasonByMonth(month);
	}

}
