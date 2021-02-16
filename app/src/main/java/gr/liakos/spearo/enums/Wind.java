package gr.liakos.spearo.enums;

import java.util.ArrayList;
import java.util.List;

import gr.liakos.spearo.R;

public enum Wind {
	
	NOT_KNOWN(0, R.drawable.wind_22, R.drawable.wind_40, R.string.not_known, R.string.wind_direction),
	
	NO_WIND(1, R.drawable.wind_no_22, R.drawable.wind_no_40, R.string.no_wind , R.string.no_wind_full),
	
	NORTH(2, R.drawable.wind_n_22, R.drawable.wind_n_40, R.string.wind_north, R.string.wind_north_full),
	
	SOUTH(3, R.drawable.wind_s_22, R.drawable.wind_s_40, R.string.wind_south, R.string.wind_south_full),
	
	EAST(4, R.drawable.wind_e_22, R.drawable.wind_e_40, R.string.wind_east, R.string.wind_east_full),
	
	WEST(5, R.drawable.wind_w_22, R.drawable.wind_w_40, R.string.wind_west, R.string.wind_west_full),
	
	NORTH_EAST(6, R.drawable.wind_ne_22, R.drawable.wind_ne_40, R.string.wind_north_east, R.string.wind_north_east_full),
	
	NORTH_WEST(7, R.drawable.wind_nw_22, R.drawable.wind_nw_40, R.string.wind_north_west, R.string.wind_north_west_full),
	
	SOUTH_EAST(8, R.drawable.wind_se_22, R.drawable.wind_se_40, R.string.wind_south_east, R.string.wind_south_east_full),
	
	SOUTH_WEST(9, R.drawable.wind_sw_22, R.drawable.wind_sw_40, R.string.wind_south_west, R.string.wind_south_west_full);


	private int spinnerDrawable;
	private int shortDesc;
	private int position;
	private int fullDesc;
	private int drawable;


	Wind(int position, int drawable, int spinnerDrawable, int shortDesc, int fullDesc){
		this.drawable = drawable;
		this.shortDesc = shortDesc;
		this.fullDesc = fullDesc;
		this.position = position;
		this.spinnerDrawable = spinnerDrawable;
	}
	
	public int getDrawable() {
		return drawable;
	}

	public int getShortDesc() {
		return shortDesc;
	}
	 
	 public int getPosition() {
		return position;
	}
	 
	public int getFullDesc() {
		return fullDesc;
	}

	public static Wind ofPosition(int position){
		 for (Wind wind : Wind.values()){
			 if (wind.position == position){
				 return wind;
			 }
		 }
		 
		 return null;
	 }

	public int getSpinnerDrawable() {
		return spinnerDrawable;
	}

	public static List<Wind> forSpinner(){
	   	 List<Wind> forSpinner = new ArrayList<Wind>();
	   	 forSpinner.add(NOT_KNOWN);
	   	 forSpinner.add(NO_WIND);
	   	 forSpinner.add(NORTH);
	   	 forSpinner.add(SOUTH);
	   	 forSpinner.add(EAST);
	   	 forSpinner.add(WEST);
	   	 forSpinner.add(NORTH_EAST);
	   	 forSpinner.add(NORTH_WEST);
	   	 forSpinner.add(SOUTH_EAST);
	   	 forSpinner.add(SOUTH_WEST);
	   	 return forSpinner;
    }

}
