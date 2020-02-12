package com.aranaira.arcanearchives.blocks.interfaces;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public interface IFacing {
  PropertyDirection FACING = BlockHorizontal.FACING;

  default PropertyEnum<EnumFacing> getFacingProperty() {
    return FACING;
  }
}
