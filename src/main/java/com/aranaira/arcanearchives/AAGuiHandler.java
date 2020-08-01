package com.aranaira.arcanearchives;

import com.aranaira.arcanearchives.client.gui.CrystalWorkbenchGUI;
import com.aranaira.arcanearchives.client.gui.ManifestGUI;
import com.aranaira.arcanearchives.client.gui.RadiantChestGUI;
import com.aranaira.arcanearchives.containers.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.containers.FakeManifestContainer;
import com.aranaira.arcanearchives.containers.ManifestContainer;
import com.aranaira.arcanearchives.containers.RadiantChestContainer;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.tileentities.RadiantChestTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class AAGuiHandler implements IGuiHandler {
  public enum GuiType {
    CrystalWorkbench,
    Manifest,
    RadiantChest;

    @Nullable
    public static GuiType byOrdinal(int ordinal) {
      for (GuiType t : values()) {
        if (t.ordinal() == ordinal) {
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
        CrystalWorkbenchTile cwt = (CrystalWorkbenchTile) te;
        return new CrystalWorkbenchContainer(cwt.getInventory(), cwt, player);
      case Manifest:
        return new FakeManifestContainer(player);
      case RadiantChest:
        if (te == null) {
          return null;
        }
        RadiantChestTile rt = (RadiantChestTile) te;
        return new RadiantChestContainer(rt.getInventory(), rt, player);
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
      case Manifest:
        return new ManifestGUI(player, new ManifestContainer(player));
      case RadiantChest:
        if (te == null) {
          return null;
        }
        RadiantChestTile rt = (RadiantChestTile) te;
        return new RadiantChestGUI(player, new RadiantChestContainer(rt.getInventory(), rt, player));
      default:
        ArcaneArchives.logger.debug(String.format("Invalid Container id of %d was passed in; null was returned to the client.", id));
        return null;
    }
  }
}
