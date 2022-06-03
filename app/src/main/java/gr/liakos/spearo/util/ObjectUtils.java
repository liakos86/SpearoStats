package gr.liakos.spearo.util;

public class ObjectUtils {
	
	public static boolean isNullOrZero(Object o) {
		if (o == null){
			return true;
		}

		if (o instanceof Double){
			Double double_o = (Double) o;
			return double_o == 0d;
		}
		
		return false;
		
	}

}
