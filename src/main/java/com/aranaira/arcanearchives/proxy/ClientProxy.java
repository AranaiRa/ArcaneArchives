package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.util.handlers.RenderHandler;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Material;
import net.minecraftforge.fml.common.FMLLog;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerItemRenderer(Item item, int meta, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	
	@Override
	public void preInit()
	{
		super.preInit();
		RenderHandler.registerEntityRenderers();
	}
	
	public static ImmutableMap<String, TextureAtlasSprite> getTextures(OBJModel model) {
		ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
		builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
		TextureAtlasSprite missing = ModelLoader.defaultTextureGetter().apply(new ResourceLocation("missingno"));
		for(String materialName : model.getMatLib().getMaterialNames())
		{
			Material material = model.getMatLib().getMaterial(materialName);
			if(material.getTexture().getTextureLocation().getResourcePath().startsWith("#"))
			{
				FMLLog.bigWarning("OBJLoaderAA: Unresolved texture '%s' for OBJ model '%s'", material.getTexture().getTextureLocation().getResourcePath(), model.toString());
			}
			else
				builder.put(materialName, ModelLoader.defaultTextureGetter().apply(material.getTexture().getTextureLocation()));
		}
		
		return builder.build();
	}
}
