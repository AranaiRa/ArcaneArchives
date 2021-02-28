package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/*
This class taken from and adapted from RenderTickHandler from the Mekanism project.
As the license of this project is also MIT, this is a license-compatible usage.
Original source: https://github.com/mekanism/Mekanism/blob/1.16.x/src/main/java/mekanism/client/render/RenderTickHandler.java
 */

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ArcaneArchives.MODID)
public class RenderTickHandler {
  private static final BoltRenderer boltRenderer = new BoltRenderer();

  public static void renderBolt(Object renderer, BoltEffect bolt) {
    boltRenderer.update(renderer, bolt, Minecraft.getInstance().getRenderPartialTicks());
  }

  @SubscribeEvent
  public static void renderWorld(RenderWorldLastEvent event) {
    if (boltRenderer.hasBoltsToRender()) {
      //Only do matrix transforms and mess with buffers if we actually have any bolts to render
      MatrixStack matrix = event.getMatrixStack();
      matrix.push();
      // here we translate based on the inverse position of the client viewing camera to get back to 0, 0, 0
      Vector3d camVec = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
      matrix.translate(-camVec.x, -camVec.y, -camVec.z);
      //TODO: FIXME, this doesn't work on fabulous, I think it needs something like
      // https://github.com/MinecraftForge/MinecraftForge/pull/7225
      IRenderTypeBuffer.Impl renderer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
      boltRenderer.render(event.getPartialTicks(), matrix, renderer);
      renderer.finish(ArcArcRenderTypes.MEK_LIGHTNING);
      matrix.pop();
    }
  }
}
