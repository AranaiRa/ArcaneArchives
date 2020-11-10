package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.state.BooleanProperty;

public interface IAccessor {
  BooleanProperty ACCESSOR = BooleanProperty.create("accessor");

  default BooleanProperty getAccessorProperty() {
    return ACCESSOR;
  }
}
