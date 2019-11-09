package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.ColorUtils.Color;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Set;

public class RenderUtils {
  public static final Vec3d ONE = new Vec3d(1, 1, 1);

  @OnlyIn(Dist.CLIENT)
  public static void drawRays(long worldTime, Vec3d adjustedPlayerPos, Set<Vec3d> target_pos) {
    GlStateManager.pushMatrix();
    GlStateManager.disableCull();
    GlStateManager.disableLighting();
    GlStateManager.disableTexture2D();
    GlStateManager.disableDepth();

    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.translated(-adjustedPlayerPos.x, -adjustedPlayerPos.y, -adjustedPlayerPos.z);

    Color c = ColorUtils.getColorFromTime(worldTime);//new Color(0.601f, 0.164f, 0.734f, 1f);
    GlStateManager.color4f(c.red, c.green, c.blue, c.alpha);
    GlStateManager.depthMask(false);

    Vec3d scale = ONE.scale(0.5);

    for (Vec3d vec : target_pos) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GlStateManager.lineWidth((1.0f - getLineWidthFromDistance(adjustedPlayerPos, vec, 10, 70)) * 10.0F);
      vec = vec.add(scale);

      bufferBuilder.pos(vec.x, vec.y, vec.z).color(c.red, c.green, c.blue, c.alpha).endVertex();
      bufferBuilder.pos(adjustedPlayerPos.x, adjustedPlayerPos.y + 1, adjustedPlayerPos.z).color(c.red, c.green, c.blue, c.alpha).endVertex();
      tessellator.draw();
    }

    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
    GlStateManager.disableBlend();
    GlStateManager.enableDepth();
    //GlStateManager.enableLighting();
    GlStateManager.enableCull();
    GlStateManager.enableTexture2D();
  }

  private static float getLineWidthFromDistance(Vec3d first, Vec3d second, float minDistanceClamp, float maxDistanceClamp) {
    float dist = (float) first.distanceTo(second);
    float normalized = MathHelper.clamp((dist - minDistanceClamp) / (maxDistanceClamp - minDistanceClamp), 0.0f, 1.0f);
    return normalized * 0.7f + 0.3f;
  }


  public static Vec3d getPlayerPosAdjusted(PlayerEntity e, float partialTicks) {
    double iPX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
    double iPY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
    double iPZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;

    return new Vec3d(iPX, iPY, iPZ);
  }
}
