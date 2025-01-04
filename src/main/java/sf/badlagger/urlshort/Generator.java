package sf.badlagger.urlshort;

import java.util.Random;

public class Generator {
	
	static private Random rndm = new Random();
	static private final  String ALPHABETH = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static String getUuid(int length) {
		
		String ret = null;
		
		for (int i = 0; i < length; ++i) {
			ret += ALPHABETH.charAt(rndm.nextInt(ALPHABETH.length()));
		}
		
		return ret;
	}
	
	public static long getUuidsNumber(int length) {
		return (long) Math.pow(ALPHABETH.length(), length);
	}
}
