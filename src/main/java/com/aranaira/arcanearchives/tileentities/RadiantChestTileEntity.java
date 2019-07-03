package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.inventory.handlers.ExtendedItemStackHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketRadiantChest;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class RadiantChestTileEntity extends ImmanenceTileEntity implements IManifestTileEntity {
	private final RadiantChestHandler inventory = new RadiantChestHandler(54);
	private ItemStack displayStack = ItemStack.EMPTY;
	private EnumFacing displayFacing = EnumFacing.NORTH;
	public String chestName = "";

	public RadiantChestTileEntity () {
		super("radiantchest");
	}

	public Int2IntOpenHashMap getOrCalculateReference (boolean force) {
		if (force) {
			inventory.manualRecount();
		}
		return inventory.getItemReference();
	}

	public Int2IntOpenHashMap getOrCalculateReference () {
		return getOrCalculateReference(false);
	}

	public ItemStack getDisplayStack () {
		return displayStack;
	}

	public void setDisplay (ItemStack displayStack, EnumFacing facing) {
		this.displayStack = displayStack;
		this.displayFacing = facing;
		defaultServerSideUpdate();
	}

	public EnumFacing getDisplayFacing () {
		return displayFacing;
	}

	@Override
	public String getDescriptor () {
		return "chest";
	}

	@Override
	public String getChestName () {
		return chestName;
	}

	public void setChestName (String newName) {
		this.chestName = (newName == null) ? "" : newName;
		this.updateChestName();
	}

	private void updateChestName () {
		if (world == null) {
			return;
		}

		if (this.world.isRemote) {
			PacketRadiantChest.SetName packet = new PacketRadiantChest.SetName(getPos(), getChestName(), world.provider.getDimension());
			NetworkHandler.CHANNEL.sendToServer(packet);
		}
	}

	@Override
	public RadiantChestHandler getInventory () {
		return inventory;
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
	@Nonnull
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, inventory.serializeNBT());
		compound.setString(Tags.CHEST_NAME, chestName);
		compound.setInteger(Tags.DISPLAY_FACING, displayFacing.getIndex());
		compound.setTag(Tags.DISPLAY_STACK, displayStack.serializeNBT());

		return compound;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (!compound.hasKey(AATileEntity.Tags.INVENTORY)) {
			ArcaneArchives.logger.info(String.format("Radiant Chest tile entity at %d/%d/%d is missing its inventory.", pos.getX(), pos.getY(), pos.getZ()));
		}
		inventory.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		chestName = compound.getString(Tags.CHEST_NAME);
		displayFacing = EnumFacing.byIndex(compound.getInteger(Tags.DISPLAY_FACING));
		displayStack = new ItemStack(compound.getCompoundTag(Tags.DISPLAY_STACK));
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	public boolean hasCapability (@Nonnull Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability (@Nonnull Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	public int countEmptySlots () {
		int empty = 0;
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				empty++;
			}
		}
		return empty;
	}

	public static class Tags {
		public static final String CHEST_NAME = "chestName";
		public static final String DISPLAY_STACK = "displayStack";
		public static final String DISPLAY_FACING = "displayFacing";

		private Tags () {
		}
	}

	public class RadiantChestHandler extends ExtendedItemStackHandler {
		private Int2IntOpenHashMap itemReference = new Int2IntOpenHashMap();

		public RadiantChestHandler (int size) {
			super(size);
			itemReference.defaultReturnValue(0);
		}

		public Int2IntOpenHashMap getItemReference () {
			return itemReference;
		}

		@Override
		public void deserializeNBT (NBTTagCompound nbt) {
			super.deserializeNBT(nbt);
			if (RadiantChestTileEntity.this.world == null || !RadiantChestTileEntity.this.world.isRemote) {
				manualRecount();
			}
		}

		private void manualRecount () {
			for (int i = 0; i < getSlots(); i++) {
				ItemStack stack = getStackInSlot(i);
				if (!stack.isEmpty()) {
					int packed = RecipeItemHelper.pack(stack);
					itemReference.put(packed, itemReference.get(packed) + stack.getCount());
				}
			}
		}

		@Override
		public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
			ItemStack inSlot = getStackInSlot(slot);
			subtraction(inSlot, -1);
			super.setStackInSlot(slot, stack);
			addition(stack, ItemStack.EMPTY);
			world.updateComparatorOutputLevel(pos, BlockRegistry.RADIANT_CHEST);
		}

		@Nonnull
		@Override
		public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (!simulate) {
				ItemStack test = super.insertItem(slot, stack, true);
				addition(stack, test);
			}
			world.updateComparatorOutputLevel(pos, BlockRegistry.RADIANT_CHEST);
			return super.insertItem(slot, stack, simulate);
		}

		@Nonnull
		@Override
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			if (!simulate) {
				ItemStack test = getStackInSlot(slot);
				subtraction(test, amount);
			}
			world.updateComparatorOutputLevel(pos, BlockRegistry.RADIANT_CHEST);
			return super.extractItem(slot, amount, simulate);
		}

		private void addition (ItemStack stack, ItemStack spare) {
			if (stack.isEmpty()) return;

			int ref = RecipeItemHelper.pack(stack);
			int count = stack.getCount();

			if (!spare.isEmpty()) {
				count -= spare.getCount();
			}

			int curCount = itemReference.get(ref);
			itemReference.put(ref, Math.max(-1, count + curCount));
		}

		private void subtraction (ItemStack stack, int count) {
			if (stack.isEmpty()) return;
			if (count == -1) count = stack.getCount();

			int ref = RecipeItemHelper.pack(stack);
			int curCount = itemReference.get(ref);
			if (curCount > 0) {
				itemReference.put(ref, Math.max(curCount - count, 0));
			}
		}
	}
}
