package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class RadiantTroveTESR extends TileEntitySpecialRenderer<RadiantTroveTileEntity> {
	@Override
	public void render (RadiantTroveTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		if (false/*te.isBeingTracked() && ConfigHandler.ItemTrackingConfig.chestsGlow*/) { //TODO: uncomment this when overlays are added
			GlStateManager.pushMatrix();
			GL11.glTranslated(x, y, z);
			boolean wasLighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			// Do stuff here!
			if (wasLighting) {
				GlStateManager.enableLighting();
			}
			GlStateManager.enableDepth();
			GL11.glTranslated(-x, -y, -z);
			GlStateManager.popMatrix();
		}
	}
}
