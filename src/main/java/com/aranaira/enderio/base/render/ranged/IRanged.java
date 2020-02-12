package com.aranaira.enderio.base.render.ranged;

import com.aranaira.enderio.core.client.render.BoundingBox;
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
  BoundingBox getBounds();

}
