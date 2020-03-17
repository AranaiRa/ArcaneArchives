package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.tileentity.TileEntity;

public class TestTile extends TileEntity {
  public TestTile() {
    super();
  }

  @Override
  public void invalidate() {
    ArcaneArchives.logger.info((world == null ? "[NULL]" : world.isRemote ? "[CLIENT]" : "[SERVER]") + " invalidated, was already invalid: " + (isInvalid() ? "true" : "false")); // remoteTileEntity -> setBlockState -> removedByPlayer
    super.invalidate();
  }

  @Override
  public void onChunkUnload() {
    ArcaneArchives.logger.info((world == null ? "[NULL]" : world.isRemote ? "[CLIENT]" : "[SERVER]") + " chunk unloaded"); // updateEntities -> updateTimeAndLight -> tick
    super.onChunkUnload();
  }

  @Override
  public void onLoad() {
    ArcaneArchives.logger.info((world == null ? "[NULL]" : world.isRemote ? "[CLIENT]" : "[SERVER]") + " on load"); // addTileEntity -> setblockState -> placeBlockAt
    super.onLoad();
  }
}
