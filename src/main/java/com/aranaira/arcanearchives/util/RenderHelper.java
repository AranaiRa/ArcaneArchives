package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Set;

public class RenderHelper {
	/*@SideOnly(Side.CLIENT)
	public static void drawRay(Vec3d player_pos, Vec3d target_pos, float width)
{
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslated(-player_pos.x, -player_pos.y, -player_pos.z);


		Color c = new Color(0.601f, 0.164f, 0.734f, 1f);
		GL11.glColor4d(c.red, c.green, c.blue, c.alpha);
		GL11.glLineWidth(width);
		GL11.glDepthMask(false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);


		bufferBuilder.pos(player_pos.x, player_pos.y + 1, player_pos.z).color(c.red, c.green, c.blue, c.alpha).endVertex();
		bufferBuilder.pos(target_pos.x + 0.5, target_pos.y + 0.5, target_pos.z + 0.5).color(c.red, c.green, c.blue, c.alpha).endVertex();


		tessellator.draw();

		GL11.glDepthMask(true);
		GL11.glPopAttrib();
}*/

	@SideOnly(Side.CLIENT)
	public static void drawRays (long worldTime, Vec3d player_pos, Set<Vec3d> target_pos, float width) {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslated(-player_pos.x, -player_pos.y, -player_pos.z);


		Color c = ColorHelper.getColorFromTime(worldTime);//new Color(0.601f, 0.164f, 0.734f, 1f);
		GL11.glColor4d(c.red, c.green, c.blue, c.alpha);
		GL11.glLineWidth(width);
		GL11.glDepthMask(false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

		for (Vec3d vec : target_pos) {
			bufferBuilder.pos(player_pos.x, player_pos.y + 1, player_pos.z).color(c.red, c.green, c.blue, c.alpha).endVertex();
			bufferBuilder.pos(vec.x + 0.5, vec.y + 0.5, vec.z + 0.5).color(c.red, c.green, c.blue, c.alpha).endVertex();
		}

		tessellator.draw();

		GL11.glDepthMask(true);
		GL11.glPopAttrib();
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

		public static Color Lerp(Color one, Color two, float prog){
			Color lerped = new Color();

			lerped.red = (float)MathHelper.clampedLerp(one.red, two.red, prog);
			lerped.green = (float)MathHelper.clampedLerp(one.green, two.green, prog);
			lerped.blue = (float)MathHelper.clampedLerp(one.blue, two.blue, prog);
			lerped.alpha = (float)MathHelper.clampedLerp(one.alpha, two.alpha, prog);
			return lerped;
		}

		public static String FormatForLogger(Color c, boolean includeAlpha){
			String str = "";
			str += "<R:"+c.red;
			str += "  G:"+c.green;
			str += "  B:"+c.blue;

			if(includeAlpha)
				str += "  A:"+c.alpha;
			str += ">";

			return str;
		}
	}
}
