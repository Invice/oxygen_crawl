package org.oceanoxygen.tnr.util;

import java.util.Random;

public class MyRandom {

	private static Random random = new Random();
	
	public static int rndBetween(int lower, int upper) {
		return(random.nextInt(upper-lower) + lower);
	}
	
	public static String rndLang() {
		int i = rndBetween(0, 4);
		String lang = "en";
		switch (i) {
		case 0:
			lang = "de";
			break;
		case 1:
			lang = "it";
			break;
		case 2:
			lang = "ja";
			break;
		default:
			break;
		}
		return lang;
	}
}
