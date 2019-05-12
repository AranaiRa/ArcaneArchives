package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.client.gui.*;
import com.aranaira.arcanearchives.inventory.ContainerGemCuttersTable;
import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.inventory.ContainerRadiantChest;
import com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable;
import com.aranaira.arcanearchives.inventory.unused.ContainerMatrixRepository;
import com.aranaira.arcanearchives.inventory.unused.ContainerMatrixStorage;
import com.aranaira.arcanearchives.inventory.unused.NetworkContainer;
import com.aranaira.arcanearchives.tileentities.AATileEntity;
import com.aranaira.arcanearchives.tileentities.GemCuttersTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import com.aranaira.arcanearchives.tileentities.unused.MatrixRepositoryTileEntity;
import com.aranaira.arcanearchives.tileentities.unused.MatrixStorageTileEntity;
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
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {

			case TOME_OF_REQUISITION:
				return new NetworkContainer(player);
			//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
			case MANIFEST:
				return null; // return new ContainerManifest(player, true);
		}

		if(!(te instanceof AATileEntity)) return null; // TODO: Handle this error somehow;

		switch(ID) {
			case RADIANT_CHEST:
				return new ContainerRadiantChest((RadiantChestTileEntity) te, player, true);
			case GEMCUTTERS_TABLE:
				GemCuttersTableTileEntity gct = (GemCuttersTableTileEntity) te;
				return new ContainerGemCuttersTable(gct.getInventory(), gct, player);
			case RADIANT_CRAFTING_TABLE:
				return new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player, player.inventory);
			case MATRIX_STORAGE:
				return new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory);
			case MATRIX_REPOSITORY:
				return new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory);
			default: {
				ArcaneArchives.logger.info(String.format("Invalid Container ID of %d was passed in; null was returned to the server", ID));
				return null;
			}
		}
	}

	@Override
	public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {
			case TOME_OF_REQUISITION:
				return new GUIBookContainer(new NetworkContainer(player));
			case MANIFEST:
				return new GUIManifest(player, new ContainerManifest(player, false));
		}

		if(!(te instanceof AATileEntity)) return null; // TODO: Handle this error also

		switch (ID) {
			case RADIANT_CHEST:
				return new GUIRadiantChest(new ContainerRadiantChest((RadiantChestTileEntity) te, player, false), player);
			case GEMCUTTERS_TABLE:
				GemCuttersTableTileEntity gct = (GemCuttersTableTileEntity) te;
				return new GUIGemCuttersTable(player, new ContainerGemCuttersTable(gct.getInventory(), gct, player));
			case RADIANT_CRAFTING_TABLE:
				return new GUIRadiantCraftingTable(player, new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player, player.inventory));
			case MATRIX_STORAGE:
				return new GUIMatrixStorage(player, new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory));
			case MATRIX_REPOSITORY:
				return new GUIMatrixRepository(player, new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory));
			default:
				ArcaneArchives.logger.info(String.format("Invalid Container ID of %d was passed in; null was returned to the client.", ID));
				return null;
		}
	}
}
