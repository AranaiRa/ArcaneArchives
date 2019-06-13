package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Vector2d;
import java.util.Vector;

public class RadiantChestTESR extends TileEntitySpecialRenderer<RadiantChestTileEntity> {
	@Override
	public void render (RadiantChestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		EnumFacing facing = EnumFacing.SOUTH;
		int angle = 0;
		switch (facing) {
			case EAST:
				angle = 270;
				break;
			case WEST:
				angle = 90;
				break;
			case SOUTH:
				angle = 0;
				break;
			case NORTH:
				angle = 180;
				break;
		}

		ItemStack stack = new ItemStack(Blocks.FURNACE);

		GlStateManager.pushMatrix();

		double xOffset = facing.getXOffset() == 0 ? 0.5 : 0;
		GlStateManager.translate(x + xOffset, y + 0.45, z);

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

	private Vector2d getOffset(EnumFacing facing) {
		switch(facing) {
			case NORTH:
				return new Vector2d(0.5, 0.0);
			case SOUTH:
				return new Vector2d(0.5, 1.0);
			case EAST:
				return new Vector2d(1.0, 0.5);
			case WEST:
				return new Vector2d(0.0, 0.5);
			default:
				return new Vector2d(0.0, 0.0);
		}
	}
}
