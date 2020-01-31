/*package com.aranaira.arcanearchives.items.templates;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTemplate extends Item {
	private final String name;

	public ItemTemplate (String name) {
		this.name = name;
		setTranslationKey(name);
		setRegistryName(new ResourceLocation(ArcaneArchives.MODID, name));
		setCreativeTab(ArcaneArchives.TAB);
	}

	@Override
	public void registerModels () {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}*/
