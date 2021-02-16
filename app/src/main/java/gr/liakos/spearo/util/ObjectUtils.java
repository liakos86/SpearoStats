package gr.liakos.spearo.util;

public class ObjectUtils {
	
	public static boolean isNullOrZero(Object o) {
		if (o instanceof Double){
			Double double_o = (Double) o;
			return double_o == null || double_o == 0d;
		}
		
		return false;
		
	}
	
	public static boolean isAnyNullOrEmpty(String... str){
		for (String string : str) {
			if (string == null || Constants.EMPTY.equals(string)){
				return true;
			}
		}
		return false;
	}

}
