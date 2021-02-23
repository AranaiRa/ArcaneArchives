package com.aranaira.arcanearchives.api;

import java.util.Random;

/*
This class taken from and adapted from StatsUtils from the Mekanism project.
As the license of this project is also MIT, this is a license-compatible usage.
Original source: https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/common/util/StatUtils.java
 */

public class StatUtils {

    private StatUtils() {
    }

    public static final Random rand = new Random();

    public static int inversePoisson(double mean) {
        double r = rand.nextDouble() * Math.exp(mean);
        int m = 0;
        double p = 1;
        double stirlingValue = mean * Math.E;
        double stirlingCoeff = 1 / Math.sqrt(2 * Math.PI);
        while ((p < r) && (m < 3 * Math.ceil(mean))) {
            m++;
            p += stirlingCoeff / Math.sqrt(m) * Math.pow(stirlingValue / m, m);
        }
        return m;
    }

    public static double min(double... vals) {
        double min = vals[0];
        for (int i = 1; i < vals.length; i++) {
            min = Math.min(min, vals[i]);
        }
        return min;
    }

    public static double max(double... vals) {
        double max = vals[0];
        for (int i = 1; i < vals.length; i++) {
            max = Math.max(max, vals[i]);
        }
        return max;
    }
}
