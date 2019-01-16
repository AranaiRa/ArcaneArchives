package com.aranaira.arcanearchives.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.client.renderer.GlStateManager.*;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import org.lwjgl.opengl.GL11;

public class ManifestRenderer extends TileEntityItemStackRenderer 
{
	public ManifestRenderer() {

	}
	
	@Override
	public void renderByItem(ItemStack itemStackIn) {
		super.renderByItem(itemStackIn);
	}
	
}
