package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class RadiantResonatorBlockEntity extends ArcaneArchivesIdentifiedBlockEntity implements ITickableTileEntity {
  // TODO: Config
  public static final int TIMER = 90;
  protected int progress = 0;

  public RadiantResonatorBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  // Should only be called server side
  protected boolean isBlocked() {
    if (getLevel() == null) {
      return true;
    }

    return !getLevel().getBlockState(getBlockPos().above()).isAir();
  }

  @Override
  public void tick() {
    if (isBlocked()) {
      return;
    }

    // TODO: Handle client-side ticking, resonator noise
    // null check is handled in isBlocked()
    //noinspection ConstantConditions
    if (getLevel().isClientSide()) {
      return;
    }

    if (progress % 5 == 0) {
      // TODO: Send an update packet
    }

    if (progress-- <= 0) {
      progress = TIMER;
      // TODO: Create block
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound = super.save(compound);
    compound.putInt(Identifiers.Resonator.progress, progress);
    return compound;
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    super.load(state, compound);
    this.progress = compound.getInt(Identifiers.Resonator.progress);
  }
}
