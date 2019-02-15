package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CreativeTabAA extends CreativeTabs
{
	public CreativeTabAA()
	{
		super(ArcaneArchives.MODID);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemRegistry.TOME_OF_REQUISITION);
	}
}
