package com.aranaira.arcanearchives.util;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ColorUtils {
	public static Color COLORSTEP_0 = new Color(1.00f, 0.50f, 0.50f, 1.0f);    //Red
	public static Color COLORSTEP_1 = new Color(1.00f, 0.75f, 0.50f, 1.0f); //Orange
	public static Color COLORSTEP_2 = new Color(1.00f, 1.00f, 0.50f, 1.0f); //Yellow
	public static Color COLORSTEP_3 = new Color(0.50f, 1.00f, 0.60f, 1.0f); //Green
	public static Color COLORSTEP_4 = new Color(0.50f, 1.00f, 1.00f, 1.0f); //Cyan
	public static Color COLORSTEP_5 = new Color(0.50f, 0.65f, 1.00f, 1.0f); //Blue
	public static Color COLORSTEP_6 = new Color(0.80f, 0.50f, 1.00f, 1.0f); //Purple
	public static Color COLORSTEP_7 = new Color(1.00f, 0.55f, 1.00f, 1.0f); //Pink
	private static int colorIncrement = 12;

	public static ColourLight parseColour (int colour, int light) {
		return new ColourLight(colour, light);
	}

	public static Color getColorFromTime (long worldTime) {
		int clippedTime = (int) (worldTime % (colorIncrement * 8));
		int band = clippedTime / colorIncrement;
		float prog = (float) (clippedTime % colorIncrement) / (float) colorIncrement;

		Color first, second;
		switch (band) {
			case 0:
				first = COLORSTEP_0;
				second = COLORSTEP_1;
				break;
			case 1:
				first = COLORSTEP_1;
				second = COLORSTEP_2;
				break;
			case 2:
				first = COLORSTEP_2;
				second = COLORSTEP_3;
				break;
			case 3:
				first = COLORSTEP_3;
				second = COLORSTEP_4;
				break;
			case 4:
				first = COLORSTEP_4;
				second = COLORSTEP_5;
				break;
			case 5:
				first = COLORSTEP_5;
				second = COLORSTEP_6;
				break;
			case 6:
				first = COLORSTEP_6;
				second = COLORSTEP_7;
				break;
			case 7:
				first = COLORSTEP_7;
				second = COLORSTEP_0;
				break;
			default:
				first = new Color(1, 1, 1, 1);
				second = new Color(0, 0, 0, 1);
				break;
		}

		Color color = Color.Lerp(first, second, prog);
		return color;
	}

	public static class ColourLight {
		public final int light1;
		public final int light2;
		public final int alpha;
		public final int red;
		public final int green;
		public final int blue;

		public ColourLight (int colour, int light) {
			light1 = light >> 0x10 & 0xFFFF;
			light2 = light & 0xFFFF;
			alpha = colour >> 24 & 0xFF;
			red = colour >> 16 & 0xFF;
			green = colour >> 8 & 0xFF;
			blue = colour & 0xFF;
		}
	}

	public static float[] getARGB (int colour) {
		return new float[]{colour >> 24 & 0xFF, colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF};
	}

	@SideOnly(Side.CLIENT)
	public static class Color {
		public float red;
		public float green;
		public float blue;
		public float alpha;

		public Color () {
			this(1.0F, 1.0F, 1.0F, 1.0F);
		}

		public Color (float redIn, float greenIn, float blueIn, float alphaIn) {
			this.red = redIn;
			this.green = greenIn;
			this.blue = blueIn;
			this.alpha = alphaIn;
		}

		public static Color Lerp (Color one, Color two, float prog) {
			Color lerped = new Color();

			lerped.red = (float) MathHelper.clampedLerp(one.red, two.red, prog);
			lerped.green = (float) MathHelper.clampedLerp(one.green, two.green, prog);
			lerped.blue = (float) MathHelper.clampedLerp(one.blue, two.blue, prog);
			lerped.alpha = (float) MathHelper.clampedLerp(one.alpha, two.alpha, prog);
			return lerped;
		}

		public static String FormatForLogger (Color c, boolean includeAlpha) {
			String str = "";
			str += "<R:" + c.red;
			str += "  G:" + c.green;
			str += "  B:" + c.blue;

			if (includeAlpha) {
				str += "  A:" + c.alpha;
			}
			str += ">";

			return str;
		}

		public int toInteger () {
			int result = (int) (alpha * 255) << 8;
			result = (result + (int) (red * 255)) << 8;
			result = (result + (int) (green * 255)) << 8;
			result = result + (int) (blue * 255);
			return result;
		}
	}
}
