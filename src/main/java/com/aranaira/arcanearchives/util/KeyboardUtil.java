package com.aranaira.arcanearchives.util;

import org.lwjgl.input.Keyboard;

@OnlyIn(Dist.CLIENT)
public class KeyboardUtil {
  public static boolean isShiftDown() {
    return Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
  }
}
