package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RadiantTankTEISR extends TileEntityItemStackRenderer {
	private final RadiantTankTESR coreRenderer;
	private IBakedModel bakedTank = null;
	private IBlockState tank = null;

	public RadiantTankTEISR (RadiantTankTESR coreRenderer) {
		this.coreRenderer = coreRenderer;
	}

	@Override
	public void renderByItem (ItemStack stack) {
		if (!stack.isEmpty()) {
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			if (tank == null) {
				tank = BlockRegistry.RADIANT_TANK.getDefaultState();
			}
			if (bakedTank == null) {
				bakedTank = dispatcher.getBlockModelShapes().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "radiant_tank"), "normal"));
			}
			Tessellator tess = Tessellator.getInstance();
			BufferBuilder renderer = tess.getBuffer();
			renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
			for (EnumFacing facing : EnumFacing.values()) {
				for (BakedQuad quad : bakedTank.getQuads(tank, facing, 0)) {
					renderer.addVertexData(quad.getVertexData());
				}
			}
			for (BakedQuad quad : bakedTank.getQuads(tank, null, 0)) {
				renderer.addVertexData(quad.getVertexData());
			}
			tess.draw();
			//dispatcher.getBlockModelRenderer().renderModelBrightnessColor(bakedTank, 1.0f, 1.0f, 1.0f, 1.0f);
			this.coreRenderer.render(stack);
		}
	}
}
