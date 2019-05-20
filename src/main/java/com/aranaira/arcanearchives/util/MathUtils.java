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
}
