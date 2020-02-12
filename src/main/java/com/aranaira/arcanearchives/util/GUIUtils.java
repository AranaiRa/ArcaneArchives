package com.aranaira.arcanearchives.util;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

/**
 * Used for java debugging watchers to debug display issues
 */
@SuppressWarnings("unused")
public class GUIUtils {
  /**
   * @return GL11.GL_MODELVIEW_MATRIX
   */
  public static String getCurrentModelViewMatrix() {
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, fb);
    fb.rewind();

    float[] printBuff = new float[16];
    fb.get(printBuff, 0, 16);

    return "GL_MODELVIEW_MATRIX: " + ArrayUtils.toString(printBuff);
  }

  /**
   * @return result of various glIsEnabled calls
   */
  public static String modeFlags() {
    boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
    boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
    boolean alpha = GL11.glIsEnabled(GL11.GL_ALPHA);

    return String.format("depth: %b blend: %b alpha: %b", depth, blend, alpha);
  }

  public static String currentColor() {
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);

    GL11.glGetFloat(GL11.GL_CURRENT_COLOR, fb);
    fb.rewind();

    float[] printBuff = new float[4];
    fb.get(printBuff, 0, 4);

    return String.format("GL_CURRENT_COLOR R(%f) G(%f) B(%f) A(%f)", printBuff[0], printBuff[1], printBuff[2], printBuff[3]);
  }
}
