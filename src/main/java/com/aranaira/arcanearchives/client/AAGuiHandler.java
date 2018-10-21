package com.aranaira.arcanearchives.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AAGuiHandler implements IGuiHandler 
{
	public static final int TOME_OF_REQUISITION = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
		case TOME_OF_REQUISITION:
			return new NetworkContainer(player);
			//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
		case TOME_OF_REQUISITION:
			//Create and return a new GUI.
			return new GUIBookContainer(new NetworkContainer());
			//return new GUIBookContainer(new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0)));
		default:
			return null;
		}
	}

}
