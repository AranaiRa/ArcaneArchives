package com.aranaira.arcanearchives.client;

import com.aranaira.arcanearchives.common.ContainerRadiantChest;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AAGuiHandler implements IGuiHandler 
{
	public static final int TOME_OF_REQUISITION = 0;
	public static final int RADIANT_CHEST = 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID)
		{
		case TOME_OF_REQUISITION:
			return new NetworkContainer(player);
			//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
		case RADIANT_CHEST:
			return new ContainerRadiantChest((RadiantChestTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
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
			return new GUIBookContainer(new NetworkContainer(player));
			//return new GUIBookContainer(new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0)));
		case RADIANT_CHEST:
			//return new GUIRadiantChest(new ContainerChest(player.inventory, (IInventory) player.openContainer, player));
			return new GUIRadiantChest(new ContainerRadiantChest((RadiantChestTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory));
		default:
			return null;
		}
	}

}
