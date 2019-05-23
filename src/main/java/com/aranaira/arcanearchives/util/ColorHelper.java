package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.util.math.MathHelper;

public class ColorHelper {
    public static RenderHelper.Color
        COLORSTEP_0 = new RenderHelper.Color(1.00f, 0.50f, 0.50f, 1.0f), //Red
        COLORSTEP_1 = new RenderHelper.Color(1.00f, 0.75f, 0.50f, 1.0f), //Orange
        COLORSTEP_2 = new RenderHelper.Color(1.00f, 1.00f, 0.50f, 1.0f), //Yellow
        COLORSTEP_3 = new RenderHelper.Color(0.50f, 1.00f, 0.60f, 1.0f), //Green
        COLORSTEP_4 = new RenderHelper.Color(0.50f, 1.00f, 1.00f, 1.0f), //Cyan
        COLORSTEP_5 = new RenderHelper.Color(0.50f, 0.65f, 1.00f, 1.0f), //Blue
        COLORSTEP_6 = new RenderHelper.Color(0.80f, 0.50f, 1.00f, 1.0f), //Purple
        COLORSTEP_7 = new RenderHelper.Color(1.00f, 0.55f, 1.00f, 1.0f); //Pink

    private static int colorIncrement = 20;

    public static RenderHelper.Color getColorFromTime(long worldTime) {
        int clippedTime = (int)(worldTime % (colorIncrement * 8));
        int band = clippedTime / colorIncrement;
        float prog = (float)(clippedTime % colorIncrement) / (float)colorIncrement;

        RenderHelper.Color first, second;
        switch(band){
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
                first = new RenderHelper.Color(1,1,1,1);
                second = new RenderHelper.Color(0,0,0,1);
                break;
        }

        RenderHelper.Color color = RenderHelper.Color.Lerp(first, second, prog);
        return color;
    }
}
