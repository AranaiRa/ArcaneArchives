package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RadiantTankTEISR extends ItemStackTileEntityRenderer {
	private final RadiantTankTESR coreRenderer;
	private IBakedModel bakedTank = null;

	public RadiantTankTEISR (RadiantTankTESR coreRenderer) {
		this.coreRenderer = coreRenderer;
	}

	@Override
	public void renderByItem (ItemStack stack) {
		if (!stack.isEmpty()) {
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
			if (bakedTank == null) {
				bakedTank = dispatcher.getBlockModelShapes().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "radiant_tank"), "normal"));
			}
			dispatcher.getBlockModelRenderer().renderModelBrightnessColor(bakedTank, 1.0f, 1.0f, 1.0f, 1.0f);
			this.coreRenderer.render(stack);
		}
	}
}
