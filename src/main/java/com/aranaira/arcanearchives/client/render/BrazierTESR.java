package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import org.lwjgl.opengl.GL11;

public class BrazierTESR extends TileEntityRenderer<BrazierTileEntity> {
	@Override
	public void render (BrazierTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();

		boolean wasLighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();

		GL11.glTranslated(x, y, z);
		Minecraft mc = Minecraft.getMinecraft();
		BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
		BlockModelShapes shapes = dispatcher.getBlockModelShapes();
		IBakedModel thisBlock = shapes.getModelForState(BlockRegistry.BRAZIER_FIRE.getDefaultState());
		mc.renderEngine.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		dispatcher.getBlockModelRenderer().renderModelBrightnessColor(thisBlock, 1f, 1f, 1f, 1f);

		if (wasLighting) {
			GlStateManager.enableLighting();
		}
		GL11.glTranslated(-x, -y, -z);

		GlStateManager.popMatrix();
	}
}
