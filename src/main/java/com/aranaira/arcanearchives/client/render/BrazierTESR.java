package com.aranaira.arcanearchives.client.render;

import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.model.animation.FastTESR;

public class BrazierTESR extends FastTESR<BrazierTileEntity> {

	@Override
	public void renderTileEntityFast (BrazierTileEntity brazierTileEntity, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buff) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = mc.getTextureMapBlocks().getTextureExtry("minecraft:blocks/fire_layer_0");

		lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.25)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.75)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.75)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.25, y + 0.75, z + 0.25)).tex(sprite.getMinU(), sprite.getMaxV()));

		lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.25)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.50, y + 1.25, z + 0.75)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.75)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.75, y + 0.75, z + 0.25)).tex(sprite.getMinU(), sprite.getMaxV()));

		lightEnd(color(buff.pos(x + 0.7165, y + 1.25, z + 0.3750)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.2835, y + 1.25, z + 0.6250)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.1585, y + 0.75, z + 0.4085)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.5915, y + 0.75, z + 0.1585)).tex(sprite.getMinU(), sprite.getMaxV()));

		lightEnd(color(buff.pos(x + 0.7165, y + 1.25, z + 0.3750)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.2835, y + 1.25, z + 0.6250)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.4085, y + 0.75, z + 0.8415)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.8418, y + 0.75, z + 0.5915)).tex(sprite.getMinU(), sprite.getMaxV()));

		lightEnd(color(buff.pos(x + 0.2835, y + 1.25, z + 0.3750)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.7165, y + 1.25, z + 0.6250)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.5915, y + 0.75, z + 0.8415)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.1585, y + 0.75, z + 0.5915)).tex(sprite.getMinU(), sprite.getMaxV()));

		lightEnd(color(buff.pos(x + 0.2835, y + 1.25, z + 0.3750)).tex(sprite.getMinU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.7165, y + 1.25, z + 0.6250)).tex(sprite.getMaxU(), sprite.getMinV()));
		lightEnd(color(buff.pos(x + 0.8415, y + 0.75, z + 0.4085)).tex(sprite.getMaxU(), sprite.getMaxV()));
		lightEnd(color(buff.pos(x + 0.4085, y + 0.75, z + 0.1585)).tex(sprite.getMinU(), sprite.getMaxV()));


	}

	// Helper method for duplicate code
	private BufferBuilder color (BufferBuilder buff) {
		return buff.color(1f, 1f, 1f, 1f);
	}

	// Helper method for duplicate code
	private void lightEnd (BufferBuilder buff) {
		// 200/200 are just values that I use, at 255 it is transparent
		buff.lightmap(200, 200).endVertex();
	}

}
