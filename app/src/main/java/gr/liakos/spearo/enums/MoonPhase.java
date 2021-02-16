package gr.liakos.spearo.enums;

import java.util.ArrayList;
import java.util.List;

import gr.liakos.spearo.R;

public enum MoonPhase {
	
	NOT_KNOWN(0,  R.string.minus, R.string.minus),
	
	NEW_MOON(1,  R.string.new_moon, R.string.grid_new_moon),
	
	WAXING_CRESCENT(2,  R.string.waxing_crescent, R.string.grid_waxing_crescent),
	
	FIRST_QUARTER(3,  R.string.first_quarter, R.string.grid_first_quarter),
	
	WAXING_GIBBOUS(4,  R.string.waxing_gibbous, R.string.grid_waxing_gibbous),
	
	FULL_MOON(5, R.string.full_moon, R.string.grid_full_moon),
	
	WANING_GIBBOUS(6, R.string.waning_gibbous, R.string.grid_waning_gibbous),
	
	LAST_QUARTER(7,  R.string.last_quarter, R.string.grid_last_quarter),
	
	WANING_CRESCENT(8, R.string.waning_crescent, R.string.grid_waning_crescent);

	int position;

	int gridViewText;

	int alertText;
	
     MoonPhase(int position, int alertText, int gridViewText){
    	 this.position = position;
    	 this.gridViewText = gridViewText;
    	 this.alertText = alertText;
     }
     
     public static MoonPhase ofPosition(int position){
    	 for (MoonPhase phase : MoonPhase.values()){
    		 if (phase.position == position){
    			 return phase;
    		 }
    	 }
    	 
    	 return null;
     }

	public int getPosition() {
		return position;
	}

	public int getGridViewText() {
		return gridViewText;
	}

	public int getAlertText() {
		return alertText;
	}
}
