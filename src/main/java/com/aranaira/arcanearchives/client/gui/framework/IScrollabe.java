package com.aranaira.arcanearchives.client.gui.framework;

/**
 * For use with {@link ScrollEventManager}
 */
public interface IScrollabe {
  /**
   * This drawn element needs to be drawn at a new y offset from where it would otherwise be drawn
   * because we are now scrolled by a different amount
   *
   * @param yOffset new y offset to draw this element at
   */
  void updateY(int yOffset);
}
