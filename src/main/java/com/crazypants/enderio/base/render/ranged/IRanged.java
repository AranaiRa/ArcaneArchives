package com.crazypants.enderio.base.render.ranged;

import javax.annotation.Nonnull;

import com.enderio.core.client.render.BoundingBox;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRanged {

  @SideOnly(Side.CLIENT)
  boolean isShowingRange ();

  @SideOnly(Side.CLIENT)
  void toggleShowRange ();

  @SideOnly(Side.CLIENT)
  void setShowingRange (boolean showingRange);

  @Nonnull
  BoundingBox getBounds ();

}
