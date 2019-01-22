package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.init.ItemLibrary;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class CreativeTabAA extends CreativeTabs
{
	CreativeTabAA()
	{
		super(ArcaneArchives.MODID);
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(ItemLibrary.TOME_OF_REQUISITION);
	}
}
