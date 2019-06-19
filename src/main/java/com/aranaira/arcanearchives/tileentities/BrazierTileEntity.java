package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.capabilities.tracking.CapabilityItemTracking;
import com.aranaira.arcanearchives.capabilities.tracking.IItemTracking;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.types.IteRef;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BrazierTileEntity extends ImmanenceTileEntity {
	private UUID lastUUID = null;
	private long lastClick = -1;

	public BrazierTileEntity () {
		super("brazier");
	}

	public void beginInsert (Entity entity) {
		if (entity.world.isRemote) {
			return; // This should never trigger.
		}

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			EnumHand hand = EnumHand.MAIN_HAND;

			// Test for double-click
			boolean doubleClick = false;
			long diff = System.currentTimeMillis() - lastClick;
			if (player.getUniqueID() == lastUUID && diff <= 15) {
				doubleClick = true;
			}
			lastUUID = player.getUniqueID();
			lastClick = System.currentTimeMillis();

			ItemStack item = player.getHeldItemMainhand();
			if (item.isEmpty()) {
				item = player.getHeldItemOffhand();
				hand = EnumHand.OFF_HAND;
			}
			if (!item.isEmpty()) {
				if (!doubleClick) {
					// Boolean result here means "the entire stack was deposited,
					// go ahead and empty out the player's mainhand.".
					// If it was only partially submitted, the reduction in the
					// stack size will already take
					player.setHeldItem(hand, tryInsert(item));
				} else {
					// Collect the references for the inventory
					// this also compensates for the main hand
					List<InventoryRef> references = collectReferences(player, item);
					tryInsert(references, item);
					consumeItems(player, references);
				}
			} else if (!item.isEmpty() && doubleClick) {
				IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				IntOpenHashSet doneSlots = new IntOpenHashSet();
				// TODO: ignore hotbar slots
				for (int i = 9; i < playerInventory.getSlots(); i++) {
					if (doneSlots.contains(i)) {
						continue;
					}
					ItemStack ref = playerInventory.getStackInSlot(i);
					if (ref.isEmpty()) {
						continue;
					}
					List<InventoryRef> references = collectReferences(player, ref);
					for (InventoryRef ref2 : references) {
						doneSlots.add(ref2.slot);
					}
					tryInsert(references, ref);
					consumeItems(player, references);
				}
			}
		} else {
			// It's an entity item
			EntityItem item = (EntityItem) entity;
			ItemStack stack = tryInsert(item.getItem());
			if (!stack.isEmpty()) {
				EntityItem newEntity = new EntityItem(world, item.posX, item.posY, item.posZ, stack);
				world.spawnEntity(newEntity);
			}
		}
	}

	private List<InventoryRef> collectReferences (EntityPlayer player, ItemStack item) {
		List<InventoryRef> references = new ArrayList<>();
		IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		for (int i = 0; i < playerInventory.getSlots(); i++) {
			ItemStack stack = playerInventory.getStackInSlot(i);
			if (ItemUtilities.areStacksEqualIgnoreSize(item, stack)) {
				references.add(new InventoryRef(stack, i, stack.getCount()));
			}
		}
		return references;
	}

	private ItemStack tryInsert (ItemStack stack) {
		List<CapabilityRef> caps = collectCapabilities(stack);
		for (CapabilityRef cap : caps) {
			stack = ItemHandlerHelper.insertItemStacked(cap.handler, stack, false);
			if (stack.isEmpty()) {
				return stack;
			}
		}
		return stack;
	}

	// Returns how many of each item to remove
	private void tryInsert (List<InventoryRef> stacks, ItemStack reference) {
		List<CapabilityRef> caps = collectCapabilities(reference);
		for (CapabilityRef cap : caps) {
			for (InventoryRef ref : stacks) {
				ItemStack result = ItemHandlerHelper.insertItemStacked(cap.handler, ref.stack, false);
				if (!result.isEmpty()) {
					ref.count = result.getCount();
				} else {
					ref.count = 0;
				}
			}
		}
	}

	private void consumeItems (EntityPlayer player, List<InventoryRef> stacks) {
		IItemHandlerModifiable handler = (IItemHandlerModifiable) player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		for (InventoryRef ref : stacks) {
			ItemStack inSlot = handler.getStackInSlot(ref.slot);
			if (inSlot.getCount() != ref.count) {
				if (ref.count == 0) {
					inSlot = ItemStack.EMPTY;
				} else {
					inSlot.setCount(ref.count);
				}
			}
			handler.setStackInSlot(ref.slot, inSlot);
		}
	}

	private List<CapabilityRef> collectCapabilities (ItemStack reference) {
		int ref = RecipeItemHelper.pack(reference);

		List<CapabilityRef> troves = new ArrayList<>();
		List<CapabilityRef> trackings = new ArrayList<>();
		ServerNetwork network = NetworkHelper.getServerNetwork(this.networkId, this.world);
		if (network != null) {
			for (IteRef ref2 : network.getManifestTileEntities()) {
				if (ref2.tile != null && ref2.getTile() != null) {
					IItemTracking cap = ref2.getTile().getCapability(CapabilityItemTracking.ITEM_TRACKING_CAPABILITY, null);
					if (cap != null) {
						IItemHandler cap2 = ref2.getTile().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
						if (ref2.clazz.equals(RadiantTroveTileEntity.class)) {
							troves.add(new CapabilityRef(cap, cap2));
						} else {
							trackings.add(new CapabilityRef(cap, cap2));
						}
					}
				}
			}
		}
		trackings.sort(Comparator.comparingInt(o -> o.tracking.quantity(ref)));
		troves.addAll(trackings);
		return troves;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	public static class Tags {
		private Tags () {
		}
	}

	private static class InventoryRef {
		public ItemStack stack;
		public int slot;
		public int count;

		public InventoryRef (ItemStack stack, int slot, int count) {
			this.stack = stack;
			this.slot = slot;
			this.count = count;
		}
	}

	private static class CapabilityRef {
		public IItemTracking tracking;
		public IItemHandler handler;

		public CapabilityRef (IItemTracking tracking, IItemHandler handler) {
			this.tracking = tracking;
			this.handler = handler;
		}
	}
}
