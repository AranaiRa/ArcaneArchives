package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.block.properties.PropertyBool;

public interface IAccessor {
  PropertyBool ACCESSOR = PropertyBool.create("accessor");

  default PropertyBool getAccessorProperty() {
    return ACCESSOR;
  }
}
