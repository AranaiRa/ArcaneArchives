//package com.aranaira.arcanearchives.client.render;
//
//import com.aranaira.arcanearchives.util.MathUtils;
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.player.ClientPlayerEntity;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.renderer.*;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderItemExtended {
//  private static ItemRenderer itemRender = Minecraft.getInstance().getRenderItem();
//
//  // TODO: Are these really needed any more?
//  @Deprecated
//  public static void setZLevel(float z) {
//    itemRender.zLevel = z;
//  }
//
//  @Deprecated
//  public static float getZLevel() {
//    return itemRender.zLevel;
//  }
//
//  @Deprecated
//  public static void modifyZLevel(float amount) {
//    itemRender.zLevel += amount;
//  }
//
//  public static void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
//    if (!stack.isEmpty()) {
//
//      if (stack.getItem().showDurabilityBar(stack)) {
//        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
//        GlStateManager.disableTexture2D();
//        GlStateManager.disableAlpha();
//        GlStateManager.disableBlend();
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder vertexbuffer = tessellator.getBuffer();
//        double health = stack.getItem().getDurabilityForDisplay(stack);
//        int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
//        int i = Math.round(13.0F - (float) health * 13.0F);
//        int j = rgbfordisplay;
//        draw(vertexbuffer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
//        draw(vertexbuffer, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
//        GlStateManager.enableBlend();
//        GlStateManager.enableAlpha();
//        GlStateManager.enableTexture2D();
//        GlStateManager.enableLighting();
//        GlStateManager.enableDepth();
//      }
//
//      if (stack.getCount() != 1 || text != null) {
//        String s = text;
//        if (s == null) {
//          int count = stack.getCount();
//          if (count > 1) {
//            s = MathUtils.format(count);
//          } else {
//            s = "";
//          }
//        } /*else {
//          s = String.valueOf(stack.getCount());
//        }*/
//        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
//        GlStateManager.disableBlend();
//        GlStateManager.pushMatrix();
//        float scale = 0.75f;
//        GlStateManager.scale(scale, scale, 1.0F);
//        fr.drawStringWithShadow(s, (xPosition + 19 - 2 - (fr.getStringWidth(s) * scale)) / scale, (yPosition + 6 + 3 + (1 / (scale * scale) - 1)) / scale, 16777215);
//        GlStateManager.popMatrix();
//        GlStateManager.enableLighting();
//        GlStateManager.enableDepth();
//        // Fixes opaque cooldown overlay a bit lower
//        // TODO: check if enabled blending still screws things up down
//        // the line.
//        GlStateManager.enableBlend();
//      }
//
//      ClientPlayerEntity entityplayersp = Minecraft.getInstance().player;
//      float f3 = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
//
//      if (f3 > 0.0F) {
//        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
//        GlStateManager.disableTexture2D();
//        Tessellator tessellator1 = Tessellator.getInstance();
//        BufferBuilder vertexbuffer1 = tessellator1.getBuffer();
//        draw(vertexbuffer1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
//        GlStateManager.enableTexture2D();
//        GlStateManager.enableLighting();
//        GlStateManager.enableDepth();
//      }
//    }
//  }
//
//  private static void draw(
//      BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
//    renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
//    renderer.pos((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
//    renderer.pos((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
//    renderer.pos((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
//    renderer.pos((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
//    Tessellator.getInstance().draw();
//  }
//
//}