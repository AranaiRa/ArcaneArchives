package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.*;
import com.aranaira.arcanearchives.tileentities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AAGuiHandler implements IGuiHandler
{
	public static final int TOME_OF_REQUISITION = 0;
	public static final int RADIANT_CHEST = 1;
	public static final int MANIFEST = 2;
	public static final int GEMCUTTERS_TABLE = 3;
	public static final int RADIANT_CRAFTING_TABLE = 4;
	public static final int MATRIX_STORAGE = 5;
	public static final int MATRIX_REPOSITORY = 6;
	public static final int MATRIX_RESERVOIR = 6;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		//ArcaneArchives.logger.info("^^^^SERVER GUI ELEMENT");
		//ArcaneArchives.logger.info("id:"+ID+"\nplayer:"+player+"\nworld:"+world+"\nx:"+x+" y:"+y+" z:"+z);
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch(ID)
		{
			case TOME_OF_REQUISITION:
				return new NetworkContainer(player);
			//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
			case MANIFEST:
				return new ContainerManifest(player, true);
		}

		if (!(te instanceof AATileEntity)) return null; // TODO: Handle this error somehow;

		switch(ID)
		{
			case RADIANT_CHEST:
				return new ContainerRadiantChest((RadiantChestTileEntity) te, player.inventory);
			case GEMCUTTERS_TABLE:
				return new ContainerGemcuttersTable((GemcuttersTableTileEntity) te, player.inventory, true);
			case RADIANT_CRAFTING_TABLE:
				return new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player.inventory);
			case MATRIX_STORAGE:
				return new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory);
			case MATRIX_REPOSITORY:
				return new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory);
			default:
			{
				ArcaneArchives.logger.info("^RETURNED NULL");
				return null;
			}
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		//ArcaneArchives.logger.info("^^^^CLIENT GUI ELEMENT");
		//ArcaneArchives.logger.info("^START");
		//ArcaneArchives.logger.info("id:"+ID+"\nplayer:"+player+"\nworld:"+world+"\nx:"+x+" y:"+y+" z:"+z);
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch(ID)
		{
			case TOME_OF_REQUISITION:
				return new GUIBookContainer(new NetworkContainer(player));
			case MANIFEST:
				return new GUIManifest(player, new ContainerManifest(player, false));
		}

		if (!(te instanceof AATileEntity)) return null; // TODO: Handle this error also

		switch(ID)
		{
			case RADIANT_CHEST:
				return new GUIRadiantChest(new ContainerRadiantChest((RadiantChestTileEntity) te, player.inventory), player.getUniqueID());

			case GEMCUTTERS_TABLE:
				return new GUIGemcuttersTable(player, new ContainerGemcuttersTable((GemcuttersTableTileEntity) te, player.inventory, false));
			case RADIANT_CRAFTING_TABLE:
				return new GUIRadiantCraftingTable(player, new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player.inventory));
			case MATRIX_STORAGE:
				return new GUIMatrixStorage(player, new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory));
			case MATRIX_REPOSITORY:
				return new GUIMatrixRepository(player, new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory));
			default:
			{
				ArcaneArchives.logger.info("^RETURNED NULL");
				return null;
			}
		}
	}

}
