package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import com.aranaira.arcanearchives.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.UUID;

public abstract class ArcaneArchivesIdentifiedTile extends TileEntity implements IArcaneArchivesTile {
  protected UUID uuid = null;

  public ArcaneArchivesIdentifiedTile(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    if (uuid != null) {
      compound.putUniqueId(Identifiers.tileId, uuid);
    }
    return super.write(compound);
  }

  @Override
  public void read(BlockState state, CompoundNBT compound) {
    if (compound.hasUniqueId(Identifiers.tileId)) {
      this.uuid = compound.getUniqueId(Identifiers.tileId);
    } else {
      // TODO: Should we be generating a unique ID here?
      this.uuid = UUID.randomUUID();
    }

    super.read(state, compound);
  }

  @Override
  public UUID getTileId() {
    return uuid;
  }
}
