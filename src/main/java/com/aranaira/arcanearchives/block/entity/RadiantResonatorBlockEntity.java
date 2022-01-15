package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.RadiantCrystalBlock;
import com.aranaira.arcanearchives.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class RadiantResonatorBlockEntity extends NetworkIdentifiedBlockEntity implements ITickableTileEntity {
  // TODO: Config
  public static final int TIMER = 90;
  protected int progress = -1;

  public RadiantResonatorBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  // Should only be called server side
  protected boolean isBlocked() {
    if (getLevel() == null) {
      return true;
    }

    if (getNetworkId() == null) {
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

    if (progress == -1) {
      progress = TIMER;
      return;
    }

    if (progress % 5 == 0) {
      // TODO: Send an update packet
    }

    if (progress-- <= 0) {
      progress = TIMER;
      // TODO: Create block
      getLevel().setBlock(getBlockPos().above(), ModBlocks.RADIANT_CRYSTAL.get().defaultBlockState().setValue(RadiantCrystalBlock.FACING, Direction.UP), 3);
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound = super.save(compound);
    compound.putInt(Identifiers.Resonator.countdown, progress);
    return compound;
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    super.load(state, compound);
    this.progress = compound.getInt(Identifiers.Resonator.countdown);
  }
}
