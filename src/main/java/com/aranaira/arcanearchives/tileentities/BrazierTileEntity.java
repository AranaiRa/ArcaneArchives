package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.AAGuiHandler;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.util.InventoryRouting;
import com.aranaira.arcanearchives.util.ItemUtilities;
import com.crazypants.enderio.base.render.ranged.IRanged;
import com.crazypants.enderio.base.render.ranged.RangeParticle;
import com.enderio.core.client.render.BoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

	private List<Runnable> clientHooks = new ArrayList<>();

	public BrazierTileEntity () {
		super("brazier");
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
		List<ItemStack> stack = InventoryRouting.tryInsertItems(this, getServerNetwork(), item.getItem());
		if (!stack.isEmpty()) {
			rejectItemStacks(stack);
		}
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
		item.setPickupDelay(20);
		world.spawnEntity(item);
	}

	public boolean beginInsert (EntityPlayer player, EnumHand hand, EnumFacing facing) {
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

		if (wasHeld) {
			toInsert.add(playerInventory.extractItem(player.inventory.currentItem, item.getCount(), false));
		}

		if (!doubleClick) {
			playerToStackMap.put(player, item.copy());
		} else {
			// Collect all the items
			for (int i = 0; i < playerInventory.getSlots(); i++) {
				ItemStack inSlot = playerInventory.getStackInSlot(i);
				if (ItemUtilities.areStacksEqualIgnoreSize(inSlot, item)) {
					toInsert.add(playerInventory.extractItem(i, inSlot.getCount(), false));
				}
			}
		}

		if (toInsert.isEmpty()) {
			throw new NullPointerException("wat");
		}

		List<ItemStack> remainder = InventoryRouting.tryInsertItems(this, network, item, toInsert);

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
		return new BoundingBox(getPos()).expand(radius);
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
			List<ItemStack> result = InventoryRouting.tryInsertItems(BrazierTileEntity.this, BrazierTileEntity.this.getServerNetwork(), stack);
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

			List<ItemStack> result = InventoryRouting.tryInsertItems(BrazierTileEntity.this, BrazierTileEntity.this.getServerNetwork(), stack);
			if (!result.isEmpty()) {
				if (result.size() == 1) {
					return result.get(0);
				}
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
}
