package com.aranaira.arcanearchives.render;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;

public class RenderTileEntityMatrixCore extends TileEntitySpecialRenderer
{
	ResourceLocation texture;
	ResourceLocation objModelLocation;
	IModel model;

	public RenderTileEntityMatrixCore()
	{
		texture = new ResourceLocation(ArcaneArchives.MODID, "models/MatrixCoreTexture.png");

		OBJLoader.INSTANCE.addDomain(ArcaneArchives.MODID);

		//model = ICustomModelLoader
	}


}
