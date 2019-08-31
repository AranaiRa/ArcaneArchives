package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.client.gui.*;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.inventory.*;
import com.aranaira.arcanearchives.tileentities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AAGuiHandler implements IGuiHandler {
	public static final int TOME_OF_REQUISITION = 0;
	public static final int RADIANT_CHEST = 1;
	public static final int MANIFEST = 2;
	public static final int GEMCUTTERS_TABLE = 3;
	public static final int RADIANT_CRAFTING_TABLE = 4;
	public static final int MATRIX_STORAGE = 5;
	public static final int MATRIX_REPOSITORY = 6;
	public static final int MATRIX_RESERVOIR = 6;
	public static final int BAUBLE_GEMSOCKET = 7;
	public static final int DEVOURING_CHARM = 8;
	public static final int DEVOURING_CHARM_BACKSIDE = 9;
	public static final int UPGRADES = 10;
	public static final int BRAZIER = 11;

	@Override
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);

		switch (ID) {

			case TOME_OF_REQUISITION:
				return null;
			//return new NetworkCraftingContainer(player.inventory, world, new BlockPos(0, 0, 0));
			case MANIFEST:
				return new ContainerFakeManifest(player);
			case BAUBLE_GEMSOCKET:
				return new ContainerGemSocket(player);
			case DEVOURING_CHARM:
				return new ContainerDevouringCharm(player);
		}

		if (!(te instanceof AATileEntity)) {
			return null; // TODO: Handle this error somehow;
		}

		switch (ID) {
			case RADIANT_CHEST:
				return new ContainerRadiantChest((RadiantChestTileEntity) te, player);
			case GEMCUTTERS_TABLE:
				ServerNetwork network = DataHelper.getServerNetwork(player.getUniqueID());
				network.synchroniseHiveInfo();
				GemCuttersTableTileEntity gct = (GemCuttersTableTileEntity) te;
				return new ContainerGemCuttersTable(gct.getInventory(), gct, player);
			case RADIANT_CRAFTING_TABLE:
				return new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player, player.inventory);
			/*case MATRIX_STORAGE:
				return new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory);
			case MATRIX_REPOSITORY:
				return new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory);*/
			case UPGRADES:
				return new ContainerUpgrades(player, (ImmanenceTileEntity) te);
			case BRAZIER:
				return new ContainerBrazier((BrazierTileEntity) te, player);
			default: {
				ArcaneArchives.logger.debug(String.format("Invalid Container ID of %d was passed in; null was returned to the server", ID));
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
				return new GUIBookContainer(null);
			case MANIFEST:
				return new GUIManifest(player, new ContainerManifest(player));
			case BAUBLE_GEMSOCKET:
				return new GUIGemSocket(new ContainerGemSocket(player));
			case DEVOURING_CHARM:
				return new GUIDevouringCharm(new ContainerDevouringCharm(player));
		}

		if (!(te instanceof AATileEntity)) {
			return null; // TODO: Handle this error also
		}

		switch (ID) {
			case RADIANT_CHEST:
				return new GUIRadiantChest(new com.aranaira.arcanearchives.inventory.ContainerRadiantChest((RadiantChestTileEntity) te, player), player.inventory);
			case GEMCUTTERS_TABLE:
				GemCuttersTableTileEntity gct = (GemCuttersTableTileEntity) te;
				return new GUIGemCuttersTable(player, new ContainerGemCuttersTable(gct.getInventory(), gct, player));
			case RADIANT_CRAFTING_TABLE:
				return new GUIRadiantCraftingTable(player, new ContainerRadiantCraftingTable((RadiantCraftingTableTileEntity) te, player, player.inventory));
			case UPGRADES:
				return new GUIUpgrades(new ContainerUpgrades(player, (ImmanenceTileEntity) te), player, (ImmanenceTileEntity) te);
			case BRAZIER:
				return new GUIBrazier(new ContainerBrazier((BrazierTileEntity) te, player));
			/*case MATRIX_STORAGE:
				return new GUIMatrixStorage(player, new ContainerMatrixStorage((MatrixStorageTileEntity) te, player.inventory));
			case MATRIX_REPOSITORY:
				return new GUIMatrixRepository(player, new ContainerMatrixRepository((MatrixRepositoryTileEntity) te, player.inventory));*/
			default:
				ArcaneArchives.logger.debug(String.format("Invalid Container ID of %d was passed in; null was returned to the client.", ID));
				return null;
		}
	}
}
