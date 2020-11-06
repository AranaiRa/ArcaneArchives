package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import com.aranaira.arcanearchives.inventories.MonitoringWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class MonitoringCrystalTile extends TrackingNetworkedBaseTile<MonitoringWrapper> {
  public static ItemStackHandler EMPTY = new ItemStackHandler(0);
  private final MonitoringWrapper emptyWrapper;
  private BlockPos target = null;

  public MonitoringCrystalTile() {
    this.emptyWrapper = new MonitoringWrapper(this, EMPTY);
  }

  @Override
  public boolean isSingleItemInventory() {
    return false;
  }

  @Override
  public String getDescriptor() {
    return "arcanearchives.tiles.tracking_descriptors.monitoring_crystal";
  }

  @Override
  public MonitoringWrapper getInventory() {
    BlockPos tar = getTarget();
    if (tar == null) {
      return emptyWrapper;
    }

    TileEntity te = world.getTileEntity(tar);
    if (te == null) {
      return emptyWrapper;
    }

    // Monitoring Crystals don't work on ITEs.
    if (te instanceof NetworkedBaseTile) {
      return emptyWrapper;
    }

    IItemHandler handler = null;

    if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
      handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    if (te instanceof IInventory) {
      handler = new InvWrapper((IInventory) te);
    }

    if (handler == null) {
      return emptyWrapper;
    }

    return new MonitoringWrapper(this, handler);
  }

  @Nullable
  public BlockPos getTarget() {
    if (target == null) {
      BlockState state = world.getBlockState(getPos());
      OmniTemplateBlock omni = (OmniTemplateBlock) state.getBlock();
      target = getPos().offset(state.getValue(omni.getFacingProperty()).getOpposite());
    }

    return target;
  }
}
