package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.GUIBookContainer;
import com.aranaira.arcanearchives.client.GUIGemcuttersTable;
import com.aranaira.arcanearchives.client.GUIManifest;
import com.aranaira.arcanearchives.client.GUIRadiantChest;
import com.aranaira.arcanearchives.tileentities.*;

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
	public static final int MANIFEST = 2;
	public static final int GEMCUTTERS_TABLE = 3;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ArcaneArchives.logger.info("^^^^SERVER GUI ELEMENT");
		ArcaneArchives.logger.info("id:"+ID+"\nplayer:"+player+"\nworld:"+world+"\nx:"+x+" y:"+y+" z:"+z);
		switch (ID)
		{
			case TOME_OF_REQUISITION:
				return new NetworkContainer(player);
				//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
			case RADIANT_CHEST:
				return new ContainerRadiantChest((RadiantChestTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			case MANIFEST:
				return new ContainerManifest(player);
			case GEMCUTTERS_TABLE:
				return new ContainerGemcuttersTable((GemcuttersTableTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			default:
			{
				ArcaneArchives.logger.info("^RETURNED NULL");
				return null;
			}
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		ArcaneArchives.logger.info("^^^^CLIENT GUI ELEMENT");
		ArcaneArchives.logger.info("^START");
		ArcaneArchives.logger.info("id:"+ID+"\nplayer:"+player+"\nworld:"+world+"\nx:"+x+" y:"+y+" z:"+z);
		switch (ID)
		{
			case TOME_OF_REQUISITION:
				return new GUIBookContainer(new NetworkContainer(player));
			case RADIANT_CHEST:
				return new GUIRadiantChest(new ContainerRadiantChest((RadiantChestTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory));
			case MANIFEST:
				return new GUIManifest(player, new ContainerManifest(player));
			case GEMCUTTERS_TABLE:
				return new GUIGemcuttersTable(player, new ContainerGemcuttersTable((GemcuttersTableTileEntity) world.getTileEntity(new BlockPos(x, y, z)), player.inventory));
			default:
			{
				ArcaneArchives.logger.info("^RETURNED NULL");
				return null;
			}
		}
	}

}