package de.primeapi.primeplugins.bungeeapi.util;

/**
 * @author Lukas S. PrimeAPI
 * created on 02.06.2021
 * crated for PrimePlugins
 */
public class PrimeUtils {

	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	public static String formatInteger(Integer input) {
		String number = input.toString();
		String s = "";
		int count = 0;
		for (int i = number.length() - 1; i >= 0; i--) {
			if (count >= 2 && i != 0) {
				s = "." + number.charAt(i) + s;
				count = 0;
			} else {
				s = number.charAt(i) + s;
				count++;
			}
		}
		return s;
	}

}
