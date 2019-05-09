package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.init.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CreativeTabAA extends CreativeTabs
{
	public CreativeTabAA () {
		super(ArcaneArchives.MODID);
	}

	@Override
	public ItemStack createIcon () {
		return new ItemStack(ItemRegistry.CUT_RADIANT_QUARTZ);
	}
}
