package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class RadiantChestTESR extends TileEntitySpecialRenderer<RadiantChestTileEntity> {
	@Override
	public void render (RadiantChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = te.getDisplayFacing();
		ItemStack stack = te.getDisplayStack();
		if (facing == null || stack == null || stack.isEmpty()) return;

		if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) return;

		Vec3d pos = (new Vec3d(x, y, z)).add(getOffset(facing));
		GlStateManager.pushMatrix();
		GlStateManager.translate(pos.x, pos.y + 0.435, pos.z);
		GlStateManager.rotate(getAngle(facing), 0f, 1f, 0f);

		// Render the item
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.scale(0.6f, 0.6f, 0.6f);
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

	private Vec3d getOffset (EnumFacing facing) {
		switch (facing) {
			case NORTH:
				return new Vec3d(0.5, 0.0, 0.03);
			case SOUTH:
				return new Vec3d(0.5, 0.0, 0.97);
			case EAST:
				return new Vec3d(0.97, 0.0, 0.5);
			case WEST:
				return new Vec3d(0.03, 0.0, 0.5);
			default:
				return new Vec3d(0.0, 0.0, 0.0);
		}
	}

	private int getAngle (EnumFacing facing) {
		switch (facing) {
			case NORTH:
				return 0;
			case SOUTH:
				return 180;
			case EAST:
				return 270;
			case WEST:
				return 90;
			default:
				return 0;
		}
	}
}
