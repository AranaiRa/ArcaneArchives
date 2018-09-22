package com.aranaira.arcanearchives.render;

import com.aranaira.arcanearchives.ArcaneArchives;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

public class RenderTileEntityMatrixCore extends TileEntitySpecialRenderer
{
	ResourceLocation texture;
	ResourceLocation objModelLocation;
	IModel model;
	
	public RenderTileEntityMatrixCore()
	{
		texture = new ResourceLocation(ArcaneArchives.MODID, "models/MatrixCoreTexture.png");
		
		//model = ICustomModelLoader
	}
}
