package com.aranaira.arcanearchives.tiles;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public interface IRanged {

  @SideOnly(Side.CLIENT)
  boolean isShowingRange();

  @SideOnly(Side.CLIENT)
  void toggleShowRange();

  @SideOnly(Side.CLIENT)
  void setShowingRange(boolean showingRange);

  @Nonnull
  AxisAlignedBB getBounds();

}
