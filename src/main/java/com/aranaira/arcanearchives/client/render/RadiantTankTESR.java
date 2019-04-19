package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.ColourUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RadiantTankTESR extends TileEntitySpecialRenderer<RadiantTankTileEntity>
{
	private static Vec3d orig = new Vec3d(0.06d, 0.12d, 0.06d);
	private static Vec3d other = new Vec3d (0.88d, 0d, 0.88d);

	@Override
	public void render(RadiantTankTileEntity te, double _x, double _y, double _z, float partialTicks, int destroyStage, float alpha)
	{
		if(te != null)
		{
			FluidTank tank = te.getInventory();

			if(tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0)
			{
				GlStateManager.pushMatrix();
				GlStateManager.enableBlend();

				double x = orig.x;
				double y = orig.y;
				double z = orig.z;

				FluidStack fluidStack = tank.getFluid();
				double y2 = (double) tank.getFluidAmount() / (double) tank.getCapacity() * 0.8d;

				BlockPos pos = te.getPos();

				Minecraft mc = Minecraft.getMinecraft();
				TextureAtlasSprite top = mc.getTextureMapBlocks().getTextureExtry(fluidStack.getFluid().getStill(fluidStack).toString());
				TextureAtlasSprite side = mc.getTextureMapBlocks().getTextureExtry(fluidStack.getFluid().getFlowing(fluidStack).toString());

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				int light = mc.world.getCombinedLight(pos, fluidStack.getFluid().getLuminosity());
				int color = fluidStack.getFluid().getColor(fluidStack);

				float sx = (float) (pos.getX() - TileEntityRendererDispatcher.staticPlayerX);
				float sy = (float) (pos.getY() - TileEntityRendererDispatcher.staticPlayerY);
				float sz = (float) (pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ);

				GlStateManager.translate(sx, sy, sz);

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

				GlStateManager.pushMatrix();
				RenderHelper.disableStandardItemLighting();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				if(Minecraft.isAmbientOcclusionEnabled())
				{
					GL11.glShadeModel(GL11.GL_SMOOTH);
				} else
				{
					GL11.glShadeModel(GL11.GL_FLAT);
				}

				GlStateManager.translate(orig.x, orig.y, orig.z);

				ColourUtil.ColourLight colour = ColourUtil.parseColour(color, light);

				double minU;
				double maxU;
				double minV;
				double maxV;

				double size = 16f;

				double width = other.x - orig.x;
				double height = y2 - orig.y;
				double length = other.x - orig.z;

				double x2 = other.x;
				// y2
				double z2 = other.z;

				double u = orig.x % 1d;
				double u1 = u + width;

				while(u1 > 1f)
				{
					u1 -= 1f;
				}

				double vy = orig.y % 1d;
				double vy1 = vy + height;
				while(vy1 > 1f)
				{
					vy1 -= 1f;
				}

				final double vz = orig.z % 1d;
				double vz1 = vz + length;

				while(vz1 > 1f)
				{
					vz1 -= 1f;
				}

				// down
				minU = top.getInterpolatedU(u * size);
				maxU = top.getInterpolatedU(u1 * size);
				minV = top.getInterpolatedV(vz * size);
				maxV = top.getInterpolatedV(vz1 * size);

				// down
				buffer.pos(x, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();

				// north
				minU = side.getInterpolatedU(u1 * size);
				maxU = side.getInterpolatedU(u * size);
				minV = side.getInterpolatedV(vy * size);
				maxV = side.getInterpolatedV(vy1 * size);

				// north
				buffer.pos(x, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();

				// east
				minU = side.getInterpolatedU(vz1 * size);
				maxU = side.getInterpolatedU(vz * size);
				minV = side.getInterpolatedV(vy * size);
				maxV = side.getInterpolatedV(vy1 * size);

				// east
				buffer.pos(x, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, minV).lightmap(colour.light1, colour.light2).endVertex();

				// south
				minU = side.getInterpolatedU(u1 * size);
				maxU = side.getInterpolatedU(u * size);
				minV = side.getInterpolatedV(vy * size);
				maxV = side.getInterpolatedV(vy1 * size);

				// south
				buffer.pos(x, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, minV).lightmap(colour.light1, colour.light2).endVertex();

				// west
				minU = side.getInterpolatedU(vz1 * size);
				maxU = side.getInterpolatedU(vz * size);
				minV = side.getInterpolatedV(vy * size);
				maxV = side.getInterpolatedV(vy1 * size);

				// west
				buffer.pos(x2, y, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();

				// up
				minU = top.getInterpolatedU(u * size);
				maxU = top.getInterpolatedU(u1 * size);
				minV = top.getInterpolatedV(vz * size);
				maxV = top.getInterpolatedV(vz1 * size);

				// up
				buffer.pos(x, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, minV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(minU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z2).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();
				buffer.pos(x2, y2, z).color(colour.red, colour.green, colour.blue, colour.alpha).tex(maxU, maxV).lightmap(colour.light1, colour.light2).endVertex();

				tessellator.draw();

				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
				RenderHelper.enableStandardItemLighting();

				GlStateManager.disableBlend();
				GlStateManager.popMatrix();
			}
		}
	}
}
