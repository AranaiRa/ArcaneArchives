/*package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.config.ServerSideConfig;
import com.aranaira.arcanearchives.inventories.ExtendedItemStackHandler;
import com.aranaira.arcanearchives.inventories.ITrackingHandler;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class RadiantChestTileEntity extends TileEntity {
  private final TrackingExtendedItemStackHandler inventory = new TrackingExtendedItemStackHandler(54);
  private ItemStack displayStack = ItemStack.EMPTY;
  private EnumFacing displayFacing = EnumFacing.NORTH;
  public String chestName = "";

  public RadiantChestTileEntity() {
  }

*//*	public void firstJoinedNetwork (ServerNetwork network) {
		super.firstJoinedNetwork(network);

		// Disregard previous to-do notice as this is only called when they
		// have no tile ID.
		if (network.getNoNewDefault()) {
			this.routingType = BrazierRoutingType.NO_NEW_STACKS;
			this.markDirty();
			defaultServerSideUpdate();
		}
	}*//*

  public Int2IntOpenHashMap getOrCalculateReference(boolean force) {
    if (force) {
      inventory.manualRecount();
    }
    return inventory.getItemReference();
  }

  public Int2IntOpenHashMap getOrCalculateReference() {
    return getOrCalculateReference(false);
  }

*//*	@Override
	public BrazierRoutingType getRoutingType () {
		return routingType;
	}*//*

  public int totalEmptySlots() {
    return inventory.getEmptyCount();
  }

  public ItemStack acceptStack(ItemStack stack, boolean simulate) {
    ItemStack result = ItemHandlerHelper.insertItemStacked(this.inventory, stack, simulate);
    this.markDirty();
    return result;
  }

  public void toggleRoutingType() {
*//*		if (routingType == BrazierRoutingType.ANY) {
			this.routingType = BrazierRoutingType.NO_NEW_STACKS;
		} else {
			this.routingType = BrazierRoutingType.ANY;
		}
		markDirty();*//*
  }

  public TrackingExtendedItemStackHandler getInventory() {
    return inventory;
  }

  @Override
  @Nonnull
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound compound = writeToNBT(new NBTTagCompound());

    return new SPacketUpdateTileEntity(pos, 0, compound);
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  @Nonnull
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setTag("Inventory", inventory.serializeNBT());
    if (chestName != null && !chestName.isEmpty()) {
      compound.setString(Tags.CHEST_NAME, chestName);
    }
    compound.setInteger(Tags.DISPLAY_FACING, displayFacing.getIndex());
    if (!displayStack.isEmpty()) {
      compound.setTag(Tags.DISPLAY_STACK, displayStack.serializeNBT());
    }
    *//*		compound.setInteger(Tags.ROUTING_TYPE, routingType.ordinal());*//*

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (!compound.hasKey("Inventory")) {
      ArcaneArchives.logger.info(String.format("Radiant Chest tile entity at %d/%d/%d is missing its inventory.", pos.getX(), pos.getY(), pos.getZ()));
    }
    inventory.deserializeNBT(compound.getCompoundTag("Inventory"));

    if (compound.hasKey(Tags.CHEST_NAME)) {
      setChestName(compound.getString(Tags.CHEST_NAME));
    }
    displayFacing = EnumFacing.byIndex(compound.getInteger(Tags.DISPLAY_FACING));
    if (compound.hasKey(Tags.DISPLAY_STACK)) {
      setDisplayStack(new ItemStack(compound.getCompoundTag(Tags.DISPLAY_STACK)));
    }
    *//*		routingType = BrazierRoutingType.fromInt(compound.getInteger(Tags.ROUTING_TYPE));*//*
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }

  @Override
  public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
    }
    return super.getCapability(capability, facing);
  }

  public int countEmptySlots() {
    int empty = 0;
    for (int i = 0; i < inventory.getSlots(); i++) {
      if (inventory.getStackInSlot(i).isEmpty()) {
        empty++;
      }
    }
    return empty;
  }
}*/
