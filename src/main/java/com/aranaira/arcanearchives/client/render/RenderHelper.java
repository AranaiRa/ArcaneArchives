package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.util.ColorHelper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Set;

public class RenderHelper {
	@SideOnly(Side.CLIENT)
	public static void drawRays (long worldTime, Vec3d player_pos, Set<Vec3d> target_pos, float width) {
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.translate(-player_pos.x, -player_pos.y, -player_pos.z);

		Color c = ColorHelper.getColorFromTime(worldTime);//new Color(0.601f, 0.164f, 0.734f, 1f);
		GlStateManager.color(c.red, c.green, c.blue, c.alpha);
		GlStateManager.depthMask(false);
		for (Vec3d vec : target_pos) {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
			GlStateManager.glLineWidth((1.0f - getLineWidthFromDistance(player_pos, vec, 10, 70)) * 10.0F);
			bufferBuilder.pos(player_pos.x, player_pos.y + 1, player_pos.z).color(c.red, c.green, c.blue, c.alpha).endVertex();
			bufferBuilder.pos(vec.x + 0.5, vec.y + 0.5, vec.z + 0.5).color(c.red, c.green, c.blue, c.alpha).endVertex();
			tessellator.draw();
		}

		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
		GlStateManager.disableBlend();
		//GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.enableTexture2D();
	}

	@SideOnly(Side.CLIENT)
	public static void drawSegmentedLine (long worldTime, java.awt.Color color, float width, Vec3d player_pos, ArrayList<Vec3d> verts) {
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

	private static float getLineWidthFromDistance (Vec3d first, Vec3d second, float minDistanceClamp, float maxDistanceClamp) {
		float dist = (float) first.distanceTo(second);
		float normalized = MathHelper.clamp((dist - minDistanceClamp) / (maxDistanceClamp - minDistanceClamp), 0.0f, 1.0f);
		float width = normalized * 0.7f + 0.3f;
		return width;
	}

	@SideOnly(Side.CLIENT)
	static class Color {
		public float red;
		public float green;
		public float blue;
		public float alpha;

		public Color () {
			this(1.0F, 1.0F, 1.0F, 1.0F);
		}

		public Color (float redIn, float greenIn, float blueIn, float alphaIn) {
			this.red = redIn;
			this.green = greenIn;
			this.blue = blueIn;
			this.alpha = alphaIn;
		}

		public static Color Lerp (Color one, Color two, float prog) {
			Color lerped = new Color();

			lerped.red = (float) MathHelper.clampedLerp(one.red, two.red, prog);
			lerped.green = (float) MathHelper.clampedLerp(one.green, two.green, prog);
			lerped.blue = (float) MathHelper.clampedLerp(one.blue, two.blue, prog);
			lerped.alpha = (float) MathHelper.clampedLerp(one.alpha, two.alpha, prog);
			return lerped;
		}

		public static String FormatForLogger (Color c, boolean includeAlpha) {
			String str = "";
			str += "<R:" + c.red;
			str += "  G:" + c.green;
			str += "  B:" + c.blue;

			if (includeAlpha) {
				str += "  A:" + c.alpha;
			}
			str += ">";

			return str;
		}
	}
}
