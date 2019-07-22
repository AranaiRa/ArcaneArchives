package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class BrazierTESR extends TileEntitySpecialRenderer<BrazierTileEntity> {
	@Override
	public void render (BrazierTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();

		boolean wasLighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();

		GL11.glTranslated(x, y + 0.6, z);
		RenderUtils.renderBlockModel(te.getWorld(), te.getPos(), BlockRegistry.BRAZIER_FIRE.getDefaultState(), true);

		if (wasLighting) {
			GlStateManager.enableLighting();
		}
		GL11.glTranslated(-x, -y + 0.6, -z);

		GlStateManager.popMatrix();
	}
}
