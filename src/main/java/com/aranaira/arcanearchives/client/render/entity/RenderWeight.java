package com.aranaira.arcanearchives.client.render.entity;

import com.aranaira.arcanearchives.entity.EntityWeight;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderWeight extends Render<EntityWeight> {
  public RenderWeight(RenderManager renderManager) {
    super(renderManager);
  }

  @Nullable
  @Override
  protected ResourceLocation getEntityTexture(EntityWeight entity) {
    return null;
  }
}
