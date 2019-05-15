package com.aranaira.arcanearchives.util;

public class NumberUtil {


	public static String format (long v) {
		if (v < 1024) {
			return v + "";
		}
		int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
		return String.format("%.1f%s", (double) v / (1L << (z * 10)), " kmgtpe".charAt(z));
	}
}
