package com.aranaira.arcanearchives.client.gui;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class GUIUtils {
	public static String getCurrentModelViewMatrix() {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, fb);
		fb.rewind();

		float[] printBuff = new float[16];
		fb.get(printBuff, 0, 16);

		return ArrayUtils.toString(printBuff);
	}
}
