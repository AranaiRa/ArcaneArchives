package com.aranaira.arcanearchives;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CreativeTabAA extends CreativeTabs {
	public CreativeTabAA () {
		super(ArcaneArchives.MODID);
	}

	@Override
	public ItemStack createIcon () {
		return ItemStack.EMPTY; // ItemStack(ItemRegistry.SHAPED_RADIANT_QUARTZ);
	}
}
