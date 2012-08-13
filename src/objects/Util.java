package objects;

import java.util.Random;

public class Util {
	public static Random random = new Random();
	
	public static double castDouble(Object number) {
		if (number instanceof Number) {
			return ((Number) number).doubleValue();
		}
		if (number instanceof String) {
			try {
				return Double.valueOf((String) number);
			} catch (NumberFormatException e) {
				
			}
		}
		return 0;
	}
	
	public static float castFloat(Object number) {
		if (number instanceof Number) {
			return ((Number) number).floatValue();
		}
		if (number instanceof String) {
			try {
				return Float.valueOf((String) number);
			} catch (NumberFormatException e) {
				
			}
		}
		return 0;
	}
}
