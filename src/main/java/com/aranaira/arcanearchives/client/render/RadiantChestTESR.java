package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.sun.javafx.geom.Vec4d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector2d;
import java.util.Vector;

public class RadiantChestTESR extends TileEntitySpecialRenderer<RadiantChestTileEntity> {
	@Override
	public void render (RadiantChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = EnumFacing.SOUTH;
		Vec3d pos = (new Vec3d(x, y, z)).add(getOffset(facing));

		ItemStack stack = new ItemStack(Blocks.FURNACE);

		GlStateManager.pushMatrix();

		GlStateManager.translate(pos.x, pos.y + 0.435, pos.z);

		// Render the item
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.5f, 0.5f, 0.5f);
		GlStateManager.pushAttrib();
		RenderHelper.enableStandardItemLighting();
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, TransformType.FIXED);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popAttrib();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		// Finish rendering the item
		GlStateManager.popMatrix();
	}

	private Vec3d getOffset(EnumFacing facing) {
		switch(facing) {
			case NORTH:
				return new Vec3d(0.5, 0.0, 0.0);
			case SOUTH:
				return new Vec3d(0.5, 0.0, 1.0);
			case EAST:
				return new Vec3d(1.0, 0.0, 0.5);
			case WEST:
				return new Vec3d(0.0, 0.0, 0.5);
			default:
				return new Vec3d(0.0, 0.0, 0.0);
		}
	}
}
