package com.aranaira.arcanearchives.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class MathUtils {
  public static Random rand = new Random();

  private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
  private static final int NUM_Z_BITS = NUM_X_BITS;
  private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
  private static final int Y_SHIFT = 0 + NUM_Z_BITS;
  private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
  private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
  private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
  private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

  /**
   * The floor of the result of dividing a by b, but using integer math so it is efficient
   *
   * @param a
   * @param b
   * @return (int)Math.floor(a / b)
   */
  public static int intDivisionFloor(int a, int b) {
    return (a / b);
  }

  /**
   * The ceiling of the result of dividing a by b, but using integer math so it is efficient
   *
   * @param a
   * @param b
   * @return (int)Math.floor(a / b)
   */
  public static int intDivisionCeiling(int a, int b) {
    return ((a + (b - 1)) / b);
  }

  public static String format(long v) {
    if (v < 1024) {
      return v + "";
    }
    int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
    return String.format("%.1f%s", (double) v / (1L << (z * 10)), " kmgtpe".charAt(z));
  }

  public static Vec3d vec3dFromLong(long serialized) {
    int i = (int) (serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
    int j = (int) (serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
    int k = (int) (serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
    return new Vec3d(i, j, k);
  }

  public static long vec3dToLong(Vec3d pos) {
    return ((long) pos.x & X_MASK) << X_SHIFT | ((long) pos.y & Y_MASK) << Y_SHIFT | ((long) pos.z & Z_MASK);
  }
}
