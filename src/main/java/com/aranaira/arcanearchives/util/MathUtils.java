package com.aranaira.arcanearchives.util;

public class MathUtils {
	/**
	 * The floor of the result of dividing a by b, but using integer math so it is efficient
	 *
	 * @param a
	 * @param b
	 * @return (int)Math.floor(a / b)
	 */
	public static int intDivisionFloor (int a, int b) {
		return (a / b);
	}

	/**
	 * The ceiling of the result of dividing a by b, but using integer math so it is efficient
	 *
	 * @param a
	 * @param b
	 * @return (int)Math.floor(a / b)
	 */
	public static int intDivisionCeiling (int a, int b) {
		return ((a + (b - 1)) / b);
	}

	public static String format (long v) {
		if (v < 1024) {
			return v + "";
		}
		int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
		return String.format("%.1f%s", (double) v / (1L << (z * 10)), " kmgtpe".charAt(z));
	}
}
