package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.util.InventoryRoutingUtils;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.PlayerUtil;
import com.aranaira.enderio.base.render.ranged.IRanged;
import com.aranaira.enderio.base.render.ranged.RangeParticle;
import com.aranaira.enderio.core.client.render.BoundingBox;
import epicsquid.mysticallib.util.Util;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BrazierTileEntity extends ImmanenceTileEntity implements IRanged {
	public static int STEP = 10;

	private UUID lastUUID = null;
	private long lastClick = -1;
	private Map<EntityPlayer, ItemStack> playerToStackMap = new ConcurrentHashMap<>();
	private int radius = 150;
	private boolean subnetworkOnly;
	private FakeHandler fakeHandler = new FakeHandler();
	private boolean showingRange;

	private Int2ObjectOpenHashMap<ItemCache> itemCache = new Int2ObjectOpenHashMap<>();

	private List<Runnable> clientHooks = new ArrayList<>();

	public BrazierTileEntity () {
		this(false);
	}

	public BrazierTileEntity (boolean fake) {
		super("brazier", fake);
		itemCache.defaultReturnValue(null);
	}

	@SideOnly(Side.CLIENT)
	public void addUpdateHook (Runnable hook) {
		clientHooks.add(hook);
	}

	@SideOnly(Side.CLIENT)
	public void removeUpdateHook (Runnable hook) {
		clientHooks.remove(hook);
	}

	public int getRadius () {
		return radius;
	}

	public int reduceRadius () {
		return radius = Math.max(0, radius - STEP);
	}

	public int increaseRadius () {
		return radius = Math.min(300, radius + STEP);
	}

	public void setRadius (int radius) {
		this.radius = Math.min(300, Math.max(radius, 0));
	}

	public boolean getNetworkMode () {
		return subnetworkOnly;
	}

	public void toggleNetworkMode () {
		subnetworkOnly = !subnetworkOnly;
	}

	public void setNetworkMode (boolean networkMode) {
		this.subnetworkOnly = networkMode;
	}

	private boolean isFavourite (ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return false;
		}

		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			return false;
		}

		return tag.hasKey("Quark:FavoriteItem");
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Nullable
	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(fakeHandler) : null;
	}

	// Handle entities that hit
	public void beginInsert (EntityItem item) {
		if (item.world.isRemote) {
			return;
		}
		if (item.getEntityData().hasKey("rejected")) {
			return;
		}

		List<ItemStack> stack = InventoryRoutingUtils.tryInsertItems(this, item.getItem());
		if (!stack.isEmpty()) {
			rejectItemStacks(stack);
		}
		item.setDead();
	}

	public void rejectItemStacks (List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			rejectItemStack(stack);
		}
	}

	public void rejectItemStack (ItemStack stack) {
		if (world.isRemote) {
			return;
		}

		EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
		item.motionZ = Math.min(0.4f, Math.min((Util.rand.nextFloat() - 0.5f) * 0.6f, 0.2f));
		item.motionX = Math.min(0.4f, Math.min((Util.rand.nextFloat() - 0.5f) * 0.6f, 0.2f));
		item.setPickupDelay(20);
		item.getEntityData().setBoolean("rejected", true);
		world.spawnEntity(item);
	}

	public boolean beginInsert (EntityPlayer player, EnumHand hand, EnumFacing facing) {
		// Test for double-click
		boolean doubleClick = false;
		long diff = System.currentTimeMillis() - lastClick;
		ItemStack lastItem = ItemStack.EMPTY;
		if (player.getUniqueID() == lastUUID && diff <= 300) {
			doubleClick = true;
			lastItem = playerToStackMap.getOrDefault(player, ItemStack.EMPTY);
		} else if (diff > 950) {
			playerToStackMap.clear();
		}
		lastUUID = player.getUniqueID();
		lastClick = System.currentTimeMillis();

		boolean wasHeld = true;

		ItemStack item = player.getHeldItem(hand).copy();
		if (item.isEmpty() && doubleClick) {
			item = lastItem;
			wasHeld = false;
		}

		if (item.isEmpty() || isFavourite(item)) {
			return false;
		}

		if (item.getItem() == ItemRegistry.SCEPTER_MANIPULATION || item.getItem() == ItemRegistry.SCEPTER_MANIPULATION || item.getItem() == ItemRegistry.DEBUG_ORB) {
			return false;
		}

		ServerNetwork network = getServerNetwork();
		if (network == null) {
			return false;
		}

		List<ItemStack> toInsert = new ArrayList<>();

		IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP); // We don't care about the off-hand

		IItemHandler cap = item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		if (wasHeld && cap == null) {
			toInsert.add(playerInventory.extractItem(player.inventory.currentItem, item.getCount(), false));
		}

		boolean doShulkerThing = false;
		NonNullList<ItemStack> itemList = NonNullList.withSize(999, ItemStack.EMPTY);
		List<ItemStack> remainder = new ArrayList<>();
		NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(item);

		if (item.getItem() instanceof ItemShulkerBox && cap == null && tag.hasKey("BlockEntityTag")) {
			ItemStackHelper.loadAllItems(tag.getCompoundTag("BlockEntityTag"), itemList);
			cap = new ShulkerItemHandler(itemList);
			doShulkerThing = true;
		}

		if (cap != null) {
			for (int i = 0; i < cap.getSlots(); i++) {
				ItemStack inSlot = cap.getStackInSlot(i);
				int count = inSlot.getCount();
				ItemStack result = fakeHandler.insertItem(0, inSlot, false);
				if (result.isEmpty()) {
					cap.extractItem(i, count, false);
				} else {
					cap.extractItem(i, count - result.getCount(), false);
				}
			}

			if (doShulkerThing) {
				tag.getCompoundTag("BlockEntityTag").removeTag("Items");
				ItemStackHelper.saveAllItems(tag.getCompoundTag("BlockEntityTag"), ((ShulkerItemHandler) cap).getStacks(), false);
				item.setTagCompound(tag);
				remainder.add(item);
			}
		} else {

			if (!doubleClick) {
				playerToStackMap.put(player, item.copy());
			} else {
				// Collect all the items
				for (int i = 0; i < playerInventory.getSlots(); i++) {
					ItemStack inSlot = playerInventory.getStackInSlot(i);
					if (ItemUtils.areStacksEqualIgnoreSize(inSlot, item)) {
						toInsert.add(playerInventory.extractItem(i, inSlot.getCount(), false));
					}
				}
			}

			if (toInsert.isEmpty()) {
				throw new NullPointerException("wat");
			}

			remainder = InventoryRoutingUtils.tryInsertItems(this, item, toInsert);
		}

		boolean doUpdate = wasHeld;

		if (!remainder.isEmpty()) {
			List<ItemStack> toThrow = new ArrayList<>();
			for (ItemStack stack : remainder) {
				ItemStack result;
				if (wasHeld) {
					result = playerInventory.insertItem(player.inventory.currentItem, stack, false);
					wasHeld = false;
				} else {
					result = ItemHandlerHelper.insertItemStacked(playerInventory, stack, false);
				}
				if (!result.isEmpty()) {
					toThrow.add(result);
				}
			}
			rejectItemStacks(toThrow);
		}

		if (doUpdate || doubleClick) {
			PlayerUtil.Server.syncInventory((EntityPlayerMP) player);
		}

		return true;
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger(Tags.RANGE, radius);
		compound.setBoolean(Tags.SUBNETWORK, subnetworkOnly);
		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey(Tags.RANGE)) {
			radius = compound.getInteger(Tags.RANGE);
		}
		if (compound.hasKey(Tags.SUBNETWORK)) {
			subnetworkOnly = compound.getBoolean(Tags.SUBNETWORK);
		}

		if (world != null && world.isRemote && !clientHooks.isEmpty()) {
			clientHooks.forEach(Runnable::run);
		}
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

	@Override
	public boolean handleManipulationInterface (EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.world.isRemote) {
			return true;
		}
		player.openGui(ArcaneArchives.instance, AAGuiHandler.BRAZIER, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isShowingRange () {
		return showingRange;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void toggleShowRange () {
		setShowingRange(!isShowingRange());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setShowingRange (boolean showingRange) {
		this.showingRange = showingRange;
		if (showingRange) {
			Minecraft.getMinecraft().effectRenderer.addEffect(new RangeParticle<BrazierTileEntity>(this));
		}
	}

	@Nonnull
	@Override
	public BoundingBox getBounds () {
		return new BoundingBox(getPos()).expand(radius, 255, radius);
	}

	@Nullable
	public ItemCache getCachedEntry (ItemStack stack) {
		return getCachedEntry(RecipeItemHelper.pack(stack));
	}

	@Nullable
	public ItemCache getCachedEntry (int packed) {
		return itemCache.get(packed);
	}

	public void cacheInsertion (ItemStack stack, IBrazierRouting route, UUID tileId) {
		int packed = RecipeItemHelper.pack(stack);
		ItemCache existing = itemCache.get(packed);
		if (existing == null || !existing.valid()) {
			itemCache.put(RecipeItemHelper.pack(stack), new ItemCache(route, tileId));
		} else {
			existing.refresh();
		}
	}

	public static class Tags {
		public static final String RANGE = "range";
		public static final String SUBNETWORK = "subnetwork";

		private Tags () {
		}
	}

	public class FakeHandler implements IItemHandlerModifiable {

		@Override
		public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
			List<ItemStack> result = InventoryRoutingUtils.tryInsertItems(BrazierTileEntity.this, stack);
			rejectItemStacks(result);
		}

		@Override
		public int getSlots () {
			return 999;
		}

		@Nonnull
		@Override
		public ItemStack getStackInSlot (int slot) {
			return ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (simulate) {
				return ItemStack.EMPTY;
			}

			List<ItemStack> result = InventoryRoutingUtils.tryInsertItems(BrazierTileEntity.this, stack);
			if (!result.isEmpty()) {
				rejectItemStacks(result);
			}

			return ItemStack.EMPTY;
		}

		@Nonnull
		@Override
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit (int slot) {
			return 999;
		}
	}

	public static class ItemCache {
		public static long LIMIT = 1 * 1000; // The time it takes for a cache to be invalidated
		private long insertedAt;
		private UUID tileId;
		private WeakReference<IBrazierRouting> weakReference = null;

		public ItemCache (@Nullable IBrazierRouting route, UUID tileId) {
			if (route != null) {
				weakReference = new WeakReference<>(route);
			}
			this.tileId = tileId;
			this.insertedAt = System.currentTimeMillis();
		}

		@Nullable
		public IBrazierRouting getRoute () {
			if (weakReference != null) {
				IBrazierRouting result = weakReference.get();
				if (result != null && !result.isTileInvalid()) {
					return result;
				}

				return null;
			}

			return null;
		}

		public UUID getTileId () {
			return tileId;
		}

		public boolean valid () {
			return (System.currentTimeMillis() - insertedAt) < LIMIT;
		}

		public void refresh () {
			this.insertedAt = System.currentTimeMillis();
		}
	}

	public static class ShulkerItemHandler extends ItemStackHandler {
		public ShulkerItemHandler (NonNullList<ItemStack> stacks) {
			super(stacks);
		}

		public NonNullList<ItemStack> getStacks () {
			return this.stacks;
		}
	}
}
