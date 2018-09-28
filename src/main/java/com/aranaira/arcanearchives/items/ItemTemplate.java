package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemLibrary;
import com.aranaira.arcanearchives.util.IHasModel;

import net.minecraft.item.Item;

public class ItemTemplate extends Item implements IHasModel {

	public ItemTemplate(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(ArcaneArchives.TAB_AA);
		
		ItemLibrary.ITEMS.add(this);
	}

	@Override
	public void registerModels() {
		ArcaneArchives.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
