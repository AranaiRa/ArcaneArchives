package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ColorUtils;
import com.aranaira.arcanearchives.util.ColorUtils.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class RenderUtils {
  public static final Vec3d ONE = new Vec3d(1, 1, 1);
  private static TextureAtlasSprite whiteTexture;

  @SideOnly(Side.CLIENT)
  public static void drawSegmentedLine(long worldTime, java.awt.Color color, float width, Vec3d player_pos, ArrayList<Vec3d> verts) {
    //GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
    //ArcaneArchives.logger.info("starting line render");
    GlStateManager.pushMatrix();
    //ArcaneArchives.logger.info("pushed matrix");
    GlStateManager.disableCull();
    GlStateManager.disableLighting();
    GlStateManager.disableTexture2D();

    GlStateManager.enableBlend();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.translate(-player_pos.x, -player_pos.y, -player_pos.z);

    GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    GlStateManager.depthMask(false);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBuffer();
    bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    bufferBuilder.pos(player_pos.x, player_pos.y + 1, player_pos.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    for (Vec3d vert : verts) {
      GlStateManager.glLineWidth(getLineWidthFromDistance(player_pos, vert, 10, 70));
      bufferBuilder.pos(vert.x, vert.y, vert.z).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
      //ArcaneArchives.logger.info("added line vert at "+vert);
    }
    tessellator.draw();

    GlStateManager.depthMask(true);
    GlStateManager.popMatrix();
  }

  public static float getLineWidthFromDistance(Vec3d first, Vec3d second, float minDistanceClamp, float maxDistanceClamp) {
    float dist = (float) first.distanceTo(second);
    float normalized = MathHelper.clamp((dist - minDistanceClamp) / (maxDistanceClamp - minDistanceClamp), 0.0f, 1.0f);
    return normalized * 0.7f + 0.3f;
  }

  // TODO: Not use
  public static void renderFullbrightBlockModel(World world, BlockPos pos, IBlockState state, boolean translateToOrigin) {
    GlStateManager.pushMatrix();
    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
    if (translateToOrigin) {
      buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
    }
    Minecraft mc = Minecraft.getMinecraft();
    BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
    BlockModelShapes shapes = dispatcher.getBlockModelShapes();
    IBakedModel thisBlock = shapes.getModelForState(state);

    mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    long rand = MathHelper.getPositionRandom(pos);

    for (BlockRenderLayer layer : BlockRenderLayer.values()) {
      if (state.getBlock().canRenderInLayer(state, layer)) {
        ForgeHooksClient.setRenderLayer(layer);
        dispatcher.getBlockModelRenderer().renderModel(world, thisBlock, state, pos, buffer, false);
      }
    }

    ForgeHooksClient.setRenderLayer(null);
    if (translateToOrigin) {
      buffer.setTranslation(0, 0, 0);
    }
    int colour = Minecraft.getMinecraft().getBlockColors().colorMultiplier(state, world, pos, 0);
    float[] argb = ColorUtils.getARGB(colour);
    float bright = 2f;
    //buffer.putColorRGB_F4(255, 255, 255);
    //argb[1] * bright, argb[2] * bright, argb[3] * bright);
    //buffer.putBrightness4(0, 255, 255, 0);
    buffer.putBrightness4(255, 255, 255, 255);
    Tessellator.getInstance().draw();
  }

  public static BufferBuilder posVec3d(BufferBuilder bufferBuilder, Vec3d vec3d) {
    return bufferBuilder.pos(vec3d.x, vec3d.y, vec3d.z);
  }

  public static void createColoredVertex(BufferBuilder bufferBuilder, Vec3d pos, Color color) {
    bufferBuilder.pos(pos.x, pos.y, pos.z).color(color.red, color.green, color.blue, color.alpha).endVertex();
  }

  public static Vec3d getPlayerPosAdjusted(EntityPlayer e, float partialTicks) {
    double iPX = e.prevPosX + (e.posX - e.prevPosX) * partialTicks;
    double iPY = e.prevPosY + (e.posY - e.prevPosY) * partialTicks;
    double iPZ = e.prevPosZ + (e.posZ - e.prevPosZ) * partialTicks;

    return new Vec3d(iPX, iPY, iPZ);
  }

  public static void renderBoundingBox(@Nonnull AxisAlignedBB bb, @Nonnull Vec4f color) {
    final float minU = whiteTexture.getMinU();
    final float maxU = whiteTexture.getMaxU();
    final float minV = whiteTexture.getMinV();
    final float maxV = whiteTexture.getMaxV();
    final BufferBuilder tes = Tessellator.getInstance().getBuffer();
    tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
    for (EnumFacing e : EnumFacing.values()) {
      for (Vertex v : getCornersWithUvForFace(bb, e, minU, maxU, minV, maxV)) {
        tes.pos(v.x(), v.y(), v.z()).tex(v.u(), v.v()).color(color.x, color.y, color.z, color.w).endVertex();
      }
    }
    Tessellator.getInstance().draw();
  }

  private static List<Vertex> getCornersWithUvForFace(@Nonnull AxisAlignedBB bb, @Nonnull EnumFacing face, float minU, float maxU, float minV, float maxV) {
    List<Vertex> result = new ArrayList<>();
    switch (face) {
      case NORTH:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(minU, maxV)));
        break;
      case SOUTH:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(maxU, maxV)));
        break;
      case EAST:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        break;
      case WEST:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        break;
      case UP:
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.maxY, bb.minZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.maxY, bb.maxZ), new Vec2f(maxU, minV)));
        break;
      case DOWN:
      default:
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.minZ), new Vec2f(maxU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.minZ), new Vec2f(minU, maxV)));
        result.add(new Vertex(new Vec3d(bb.maxX, bb.minY, bb.maxZ), new Vec2f(minU, minV)));
        result.add(new Vertex(new Vec3d(bb.minX, bb.minY, bb.maxZ), new Vec2f(maxU, minV)));
        break;
    }
    return result;
  }

  @SubscribeEvent
  public static void onIconLoad(TextureStitchEvent.Pre event) {
    final TextureMap map = event.getMap();
    if (map != null) {
      whiteTexture = map.registerSprite(new ResourceLocation(ArcaneArchives.MODID, "white"));
    }
  }

  public static class Vec4f {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4f(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
    }
  }

  public static class Vertex {

    private Vec3d xyz;
    private Vec2f uv;

    public Vertex(Vec3d xyz, Vec2f uv) {
      this.xyz = xyz;
      this.uv = uv;
    }

    public double x() {
      return xyz.x;
    }

    public double y() {
      return xyz.y;
    }

    public double z() {
      return xyz.z;
    }

    public float u() {
      return uv.x;
    }

    public float v() {
      return uv.y;
    }
  }
}
