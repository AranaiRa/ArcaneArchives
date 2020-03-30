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
