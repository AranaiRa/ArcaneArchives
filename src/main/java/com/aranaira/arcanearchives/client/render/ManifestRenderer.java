package com.aranaira.arcanearchives.client.render;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ManifestRenderer extends TileEntityItemStackRenderer
{
	public ManifestRenderer()
	{

	}

	@Override
	public void renderByItem(ItemStack itemStackIn)
	{
		super.renderByItem(itemStackIn);
	}

}
