package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.IGCTRecipe;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.inventory.handlers.ITrackingHandler;
import com.aranaira.arcanearchives.network.Networking;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import com.aranaira.arcanearchives.tileentities.interfaces.IBrazierRouting;
import com.aranaira.arcanearchives.tileentities.interfaces.IManifestTileEntity;
import com.aranaira.arcanearchives.util.ItemUtils;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GemCuttersTableTileEntity extends ImmanenceTileEntity implements IManifestTileEntity, IBrazierRouting {
	private final TrackingGCTHandler inventory = new TrackingGCTHandler(18);
	public static final int RECIPE_PAGE_LIMIT = 7;
	private IGCTRecipe currentRecipe;
	private IGCTRecipe lastRecipe;
	private IGCTRecipe penultimateRecipe;
	private int page;

	public GemCuttersTableTileEntity () {
		super("gemcutterstable");
		currentRecipe = RecipeLibrary.RADIANT_DUST_RECIPE;
	}

	@Override
	public String getDescriptor () {
		return "Gem Cutter's Table";
	}

	@Override
	public String getChestName () {
		return "";
	}

	@Override
	public IItemHandlerModifiable getInventory () {
		return inventory;
	}

	public void setRecipe (ResourceLocation name) {
		currentRecipe = GCTRecipeList.instance.getRecipe(name);
	}

	public void setRecipe (int index) {
		manuallySetRecipe(index);

		if (world != null && world.isRemote) {
			clientSideUpdate();
		} else if (world != null) {
			defaultServerSideUpdate();
		}
	}

	public void manuallySetRecipe (int index) {
		currentRecipe = GCTRecipeList.instance.getRecipeByIndex(index);
	}

	public static final ResourceLocation INVALID = new ResourceLocation(ArcaneArchives.MODID, "invalid_gct_recipe");

	public void clientSideUpdate () {
		if (world == null || !world.isRemote) {
			return;
		}

		ResourceLocation loc = INVALID;

		if (currentRecipe != null) {
			loc = currentRecipe.getName();
		}

		PacketGemCutters.ChangeRecipe packet = new PacketGemCutters.ChangeRecipe(loc, getPos(), world.provider.getDimension());
		Networking.CHANNEL.sendToServer(packet);
	}

	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Nullable
	public IGCTRecipe getCurrentRecipe () {
		return currentRecipe;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	}

	@Deprecated
	public boolean hasCurrentRecipe () {
		return currentRecipe != null;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		manuallySetRecipe(compound.getInteger(Tags.RECIPE)); // is this server-side or client-side?
	}

	public IGCTRecipe getLastRecipe () {
		return lastRecipe;
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, inventory.serializeNBT());
		if (currentRecipe != null) {
			compound.setInteger(Tags.RECIPE, currentRecipe.getIndex());
		}

		return compound;
	}

	public void setLastRecipe (IGCTRecipe lastRecipe) {
		this.lastRecipe = lastRecipe;
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	public IGCTRecipe getPenultimateRecipe () {
		return penultimateRecipe;
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	public void updatePenultimateRecipe () {
		this.penultimateRecipe = this.lastRecipe;
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void previousPage () {
		if (getPage() > 0) {
			setPage(page - 1);
		}
	}

	public int getPage () {
		return page;
	}

	public void setPage (int page) {
		this.page = page;
	}

	public void nextPage () {
		if (GCTRecipeList.instance.getSize() > (page + 1) * RECIPE_PAGE_LIMIT) {
			setPage(page + 1);
		} else {
			setPage(0);
		}
	}

	public Int2IntOpenHashMap getOrCalculateReference (boolean force) {
		if (force) {
			inventory.manualRecount();
		}
		return inventory.getItemReference();
	}

	@Override
	public Int2IntOpenHashMap getOrCalculateReference () {
		return getOrCalculateReference(false);
	}

	@Override
	public BrazierRoutingType getRoutingType () {
		return BrazierRoutingType.GCT;
	}

	@Override
	public int countEmptySlots () {
		int empty = 0;
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) empty++;
		}
		return empty;
	}

	@Override
	public int totalEmptySlots () {
		return inventory.getEmptyCount();
	}

	@Override
	public int totalSlots () {
		return inventory.getSlots();
	}

	@Override
	public int slotMultiplier () {
		return 1;
	}

	@Override
	public ItemStack acceptStack (ItemStack stack) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			ItemStack inSlot = inventory.getStackInSlot(i);
			if (ItemUtils.areStacksEqualIgnoreSize(stack, inSlot)) {
				stack = inventory.insertItem(i, stack, false);
				if (stack.isEmpty()) return stack;
			}
		}
		this.markDirty();
		return stack;
	}

	public static class Tags {
		public static final String RECIPE = "recipe";
	}

	public class TrackingGCTHandler extends ItemStackHandler implements ITrackingHandler {
		private Int2IntOpenHashMap itemReference = new Int2IntOpenHashMap();
		private int emptySlots = 0;

		public TrackingGCTHandler (int size) {
			super(size);
			itemReference.defaultReturnValue(0);
		}

		@Override
		public void deserializeNBT (NBTTagCompound nbt) {
			super.deserializeNBT(nbt);
			if (GemCuttersTableTileEntity.this.world == null || !GemCuttersTableTileEntity.this.world.isRemote) {
				manualRecount();
			}
		}

		public Int2IntOpenHashMap getItemReference () {
			return itemReference;
		}

		@Override
		public int totalSlots () {
			return getSlots();
		}

		@Override
		public int getEmptyCount () {
			return emptySlots;
		}

		@Override
		public void setEmptyCount (int amount) {
			this.emptySlots = amount;
		}

		@Override
		public void incrementEmptyCount () {
			this.emptySlots++;
		}

		@Override
		public void decrementEmptyCount () {
			this.emptySlots--;
		}

		@Override
		public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
			ItemStack inSlot = getStackInSlot(slot);
			if (inSlot.isEmpty()) decrementEmptyCount();
			subtraction(inSlot, -1);
			super.setStackInSlot(slot, stack);
			addition(stack, ItemStack.EMPTY);
			if (stack.isEmpty()) incrementEmptyCount();
		}

		@Nonnull
		@Override
		public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (!simulate) {
				ItemStack inSlot = getStackInSlot(slot);
				if (inSlot.isEmpty()) decrementEmptyCount();
				ItemStack test = super.insertItem(slot, stack, true);
				addition(stack, test);
			}
			return super.insertItem(slot, stack, simulate);
		}

		@Nonnull
		@Override
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			if (!simulate) {
				ItemStack test = getStackInSlot(slot);
				subtraction(test, amount);
			}
			ItemStack result = super.extractItem(slot, amount, simulate);
			if (!simulate) {
				ItemStack inSlot = getStackInSlot(slot);
				if (inSlot.isEmpty()) incrementEmptyCount();
			}
			return result;
		}
	}
}
