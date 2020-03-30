package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.client.gui.CrystalWorkbenchGUI;
import com.aranaira.arcanearchives.containers.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class AAGuiHandler implements IGuiHandler {
  public enum GuiType {
    CrystalWorkbench;

    @Nullable
    public static GuiType byOrdinal(int ordinal) {
      int i = 0;
      for (GuiType t : values()) {
        if (i == ordinal) {
          return t;
        }
      }

      return null;
    }
  }

  @Override
  public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity te = world.getTileEntity(pos);

    GuiType type = GuiType.byOrdinal(id);
    if (type == null) {
      return null;
    }

    switch (type) {
      case CrystalWorkbench:
        if (te == null) {
          return null;
        }
        CrystalWorkbenchTile tile = (CrystalWorkbenchTile) te;
        return new CrystalWorkbenchContainer(tile.getInventory(), tile, player);
      default: {
        ArcaneArchives.logger.debug(String.format("Invalid Container id of %d was passed in; null was returned to the server", id));
        return null;
      }
    }
  }

  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity te = world.getTileEntity(pos);

    GuiType type = GuiType.byOrdinal(id);
    if (type == null) {
      return null;
    }

    switch (type) {
      case CrystalWorkbench:
        if (te == null) {
          return null;
        }
        CrystalWorkbenchTile tile = (CrystalWorkbenchTile) te;
        return new CrystalWorkbenchGUI(player, new CrystalWorkbenchContainer(tile.getInventory(), tile, player));
      default:
        ArcaneArchives.logger.debug(String.format("Invalid Container id of %d was passed in; null was returned to the client.", id));
        return null;
    }
  }
}
