package com.aranaira.arcanearchives.client.tracking;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.render.RenderUtils;
import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.MathUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
public class LineHandler {
  public static boolean mIsDrawingLine;

  @SideOnly(Side.CLIENT)
  public static void drawRays(long worldTime, Vec3d adjustedPlayerPos, Set<Vec3d> target_pos) {
    GlStateManager.pushMatrix();
    GlStateManager.disableCull();
    GlStateManager.disableLighting();
    GlStateManager.disableTexture2D();
    GlStateManager.disableDepth();

    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.translate(-adjustedPlayerPos.x, -adjustedPlayerPos.y, -adjustedPlayerPos.z);

    ColorUtils.Color c = ColorUtils.getColorFromTime(worldTime);//new Color(0.601f, 0.164f, 0.734f, 1f);
    GlStateManager.color(c.red, c.green, c.blue, c.alpha);
    GlStateManager.depthMask(false);

    Vec3d scale = RenderUtils.ONE.scale(0.5);

    for (Vec3d vec : target_pos) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
      GlStateManager.glLineWidth((1.0f - RenderUtils.getLineWidthFromDistance(adjustedPlayerPos, vec, 10, 70)) * 10.0F);
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

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void renderOverlay(RenderWorldLastEvent event) {
    Set<Vec3d> positions = ManifestTracking.getPositions(Minecraft.getMinecraft().player.dimension);

    if (!positions.isEmpty()) {
      drawRays(Minecraft.getMinecraft().player.world.getTotalWorldTime(), RenderUtils.getPlayerPosAdjusted(Minecraft.getMinecraft().player, event.getPartialTicks()), ImmutableSet.copyOf(positions));
    }
  }

  public static void addLine(Vec3d line, int dimension) {
    Set<Vec3d> positions = ManifestTracking.getPositions(dimension);
    positions.add(line);
  }

  public static void addLine(BlockPos pos, int dimension) {
    addLine(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), dimension);
  }

  public static void addLines(List<BlockPos> positions, int dimension) {
    positions.forEach(k -> addLine(k, dimension));
  }

  public static void removeLine(BlockPos pos, int dimension) {
    Vec3d bpos = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    removeLine(bpos, dimension);
  }

  public static void removeLine(Vec3d line, int dimension) {
    Set<Vec3d> positions = ManifestTracking.getPositions(dimension);
    positions.remove(line);
  }

  public static void checkClear(int dimension) {
    if (ManifestTracking.getPositions(dimension).isEmpty()) {
      ManifestTracking.clear();
    }
  }

  @SubscribeEvent
  public static void playerLoggedIn(PlayerLoggedInEvent event) {
    mIsDrawingLine = false;
  }

  public static void clearChests(int dimension) {
    Set<Vec3d> positions = ManifestTracking.getPositions(dimension);
    for (Vec3d pos : positions) {
      ManifestTracking.remove(dimension, MathUtils.vec3dToLong(pos));
    }
    positions.clear();
  }

}
