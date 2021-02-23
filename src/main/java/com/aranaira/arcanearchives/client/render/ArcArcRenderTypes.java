package com.aranaira.arcanearchives.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

/*
This class taken from and adapted from MekanismRenderType from the Mekanism project.
As the license of this project is also MIT, this is a license-compatible usage.
Original source: https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/client/render/MekanismRenderType.java
 */

public class ArcArcRenderTypes extends RenderType {
  public static final RenderType MEK_LIGHTNING = makeType("mek_lightning", DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
      false, true, RenderType.State.getBuilder()
          .writeMask(COLOR_DEPTH_WRITE)
          .transparency(LIGHTNING_TRANSPARENCY)
          .shadeModel(SHADE_ENABLED)
          .build(false)
  );

  //Ignored
  private ArcArcRenderTypes(String name, VertexFormat format, int drawMode, int bufferSize, boolean useDelegate, boolean needsSorting, Runnable runnablePre, Runnable runnablePost) {
    super(name, format, drawMode, bufferSize, useDelegate, needsSorting, runnablePre, runnablePost);
  }
}
