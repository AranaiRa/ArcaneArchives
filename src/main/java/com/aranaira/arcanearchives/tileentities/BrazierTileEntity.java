package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.inventory.handlers.TroveItemHandler;
import com.aranaira.arcanearchives.tileentities.IBrazierRouting.BrazierRoutingType;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.aranaira.arcanearchives.util.types.IteRef;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
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

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BrazierTileEntity extends ImmanenceTileEntity {
	public static int STEP = 10;

	private UUID lastUUID = null;
	private long lastClick = -1;
	private Map<EntityPlayer, ItemStack> playerToStackMap = new ConcurrentHashMap<>();
	private int radius = 150;
	private boolean subnetworkOnly = false;

	public BrazierTileEntity () {
		super("brazier");
	}

	public int getRadius () {
		return radius;
	}

	public int reduceRadius () {
		return radius = Math.max(0, radius - STEP);
	}

	public int increaseRadius () {
		return radius = Math.max(300, radius + STEP);
	}

	public boolean getNetworkMode () {
		return subnetworkOnly;
	}

	public void toggleNetworkMode () {
		subnetworkOnly = !subnetworkOnly;
	}


	private boolean isFavourite (ItemStack stack) {
		if (!stack.hasTagCompound()) return false;

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) return false;

		return tag.hasKey("Quark:FavoriteItem");
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
			ItemStack lastItem = ItemStack.EMPTY;
			if (player.getUniqueID() == lastUUID && diff <= 2000) {
				doubleClick = true;
				lastItem = playerToStackMap.getOrDefault(player, ItemStack.EMPTY);
			} else if (diff > 20000) {
				playerToStackMap.clear();
			}
			lastUUID = player.getUniqueID();
			lastClick = System.currentTimeMillis();


			ItemStack item = player.getHeldItemMainhand();
			if (item.isEmpty()) {
				item = player.getHeldItemOffhand();
				hand = EnumHand.OFF_HAND;
			}
			if (!item.isEmpty() && item.hasTagCompound() && isFavourite(item)) {
				return;
			}
			if (!item.isEmpty() && !doubleClick) {
				playerToStackMap.put(player, item.copy());
			}
			if (item.isEmpty() && !doubleClick) {
				playerToStackMap.put(player, ItemStack.EMPTY);
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
			} else if ((!item.isEmpty() && doubleClick) || (!lastItem.isEmpty() && doubleClick)) {
				ItemStack toCompare = item.isEmpty() ? lastItem : item;
				IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				IntOpenHashSet doneSlots = new IntOpenHashSet();
				for (int i = 0; i < playerInventory.getSlots(); i++) {
					if (doneSlots.contains(i)) {
						continue;
					}
					ItemStack ref = playerInventory.getStackInSlot(i);
					if (ref.isEmpty() || !ItemUtilities.areStacksEqualIgnoreSize(toCompare, ref) || isFavourite(ref)) {
						continue;
					}
					List<InventoryRef> references = collectReferences(player, ref);
					for (InventoryRef ref2 : references) {
						doneSlots.add(ref2.slot);
					}
					tryInsert(references, ref);
					consumeItems(player, references);
				}
			} else if (item.isEmpty() && lastItem.isEmpty() && doubleClick && player.isSneaking()) {
				IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
				IntOpenHashSet doneSlots = new IntOpenHashSet();
				for (int i = 9; i < playerInventory.getSlots(); i++) {
					if (doneSlots.contains(i)) {
						continue;
					}
					ItemStack ref = playerInventory.getStackInSlot(i);
					if (ref.isEmpty() || isFavourite(ref)) {
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
		if (isFavourite(stack)) return stack;

		ServerNetwork network = NetworkHelper.getServerNetwork(this.networkId, this.world);
		List<CapabilityRef> caps = collectCapabilities(network, stack);
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
		ServerNetwork network = NetworkHelper.getServerNetwork(this.networkId, this.world);
		List<CapabilityRef> caps = collectCapabilities(network, reference);
		for (CapabilityRef cap : caps) {
			for (InventoryRef ref : stacks) {
				if (isFavourite(ref.stack)) continue;

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

	public static List<CapabilityRef> collectCapabilities (ServerNetwork network, ItemStack reference) {
		int ref = RecipeItemHelper.pack(reference);

		List<CapabilityRef> troves = new ArrayList<>();
		List<CapabilityRef> chests = new ArrayList<>();
		List<CapabilityRef> gcts = new ArrayList<>();
		if (network != null) {
			for (IteRef ref2 : network.getManifestTileEntities()) {
				if (ref2.tile != null && ref2.getTile() != null) {

					ImmanenceTileEntity ite = ref2.getTile();
					if (!(ite instanceof IBrazierRouting)) continue;

					IBrazierRouting routing = (IBrazierRouting) ite;
					Int2IntOpenHashMap referenceMap = routing.getOrCalculateReference();
					IItemHandler inventory = ite.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					if (routing.getRoutingType() != BrazierRoutingType.ANY && referenceMap.get(ref) <= 0) continue;
					if (routing.isVoidingTrove(reference)) {
						return Collections.singletonList(new CapabilityRef(referenceMap, inventory));
					}
					if (ite instanceof RadiantTroveTileEntity) {
						TroveItemHandler handler = ((RadiantTroveTileEntity) ite).getInventory();
						if (handler.getPacked() == ref) {
							troves.add(new CapabilityRef(referenceMap, handler));
						}
					} else if (ite instanceof RadiantChestTileEntity) {
						if (routing.getRoutingType() == BrazierRoutingType.ANY || (referenceMap.get(ref) < reference.getMaxStackSize() && referenceMap.get(ref) > 0)) {
							chests.add(new CapabilityRef(referenceMap, inventory));
						}
					} else if (ite instanceof GemCuttersTableTileEntity) {
						if (referenceMap.get(ref) > 0) {
							gcts.add(new CapabilityRef(referenceMap, inventory));
						}
					}
				}
			}
		}
		chests.sort((o1, o2) -> Integer.compare(o2.map.getOrDefault(ref, 0), o1.map.getOrDefault(ref, 0)));
		gcts.sort((o1, o2) -> Integer.compare(o2.map.getOrDefault(ref, 0), o1.map.getOrDefault(ref, 0)));
		gcts.addAll(troves);
		gcts.addAll(chests);
		return gcts;
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

	public static class CapabilityRef {
		public Map<Integer, Integer> map;
		public IItemHandler handler;

		public CapabilityRef (Map<Integer, Integer> map, IItemHandler handler) {
			this.map = map;
			this.handler = handler;
		}
	}
}
