package com.aranaira.arcanearchives.tileentities;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public interface IRanged {

  @OnlyIn(Dist.CLIENT)
  boolean isShowingRange();

  @OnlyIn(Dist.CLIENT)
  void toggleShowRange();

  @OnlyIn(Dist.CLIENT)
  void setShowingRange(boolean showingRange);

  @Nonnull
  AxisAlignedBB getBounds();

}
