package com.aranaira.arcanearchives.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod.EventHandler;

public class CommonProxy
{
	public void registerItemRenderer(Item item, int meta, String id) {}
	
	public void registerBlockRenderer(Block block, int meta, String id) {}
	
	public void preInit() {	}

	public void init() { }

	public void postInit() { }

}
