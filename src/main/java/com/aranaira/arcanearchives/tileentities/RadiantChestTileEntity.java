package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RadiantChestTileEntity extends ManifestTileEntity {
	private final RadiantChestHandler inventory = new RadiantChestHandler(54);
	private ItemStack displayStack = ItemStack.EMPTY;
	private EnumFacing displayFacing = EnumFacing.NORTH;
	public String chestName = "";

	public RadiantChestTileEntity () {
		super("radiantchest");
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

	public ItemStackHandler getInventory () {
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

	@Deprecated
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

	private class RadiantChestHandler extends ItemStackHandler {
		public int emptySlots = 0;
		public Int2IntOpenHashMap itemReference = new Int2IntOpenHashMap();

		public RadiantChestHandler (int size) {
			super(size);
			itemReference.defaultReturnValue(-1);
		}

		@Override
		public void deserializeNBT (NBTTagCompound nbt) {
			super.deserializeNBT(nbt);
			if (RadiantChestTileEntity.this.world == null || !RadiantChestTileEntity.this.world.isRemote) {
				manualRecount();
			}
		}

		private void manualRecount () {
			itemReference.clear();
			itemReference.defaultReturnValue(0);
			emptySlots = 0;
			for (int i = 0; i < getSlots(); i++) {
				ItemStack stack = getStackInSlot(i);
				if (stack.isEmpty()) {
					emptySlots++;
				} else {
					int packed = RecipeItemHelper.pack(stack);
					itemReference.put(packed, itemReference.get(packed) + stack.getCount());
				}
			}
			itemReference.defaultReturnValue(-1);
		}

		@Override
		public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
			ItemStack curStack = getStackInSlot(slot);
			int current = RecipeItemHelper.pack(getStackInSlot(slot));
			int count = itemReference.get(current);
			if (count != -1) {
				count -= curStack.getCount();
				itemReference.put(current, count);
			}
			if (stack.isEmpty()) {
				emptySlots++;
			}
			super.setStackInSlot(slot, stack);
		}

		@Nonnull
		@Override
		public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (!simulate) {
				ItemStack inSlot = getStackInSlot(slot);
				if (inSlot.isEmpty()) {
					emptySlots--;
				}

				ItemStack test = super.insertItem(slot, stack, true);
				int current = RecipeItemHelper.pack(stack);
				int count = stack.getCount();
				if (!test.isEmpty()) {
					count -= test.getCount();
				}
				int curCount = itemReference.get(current);
				if (curCount != -1) {
					count += curCount;
				}
				itemReference.put(current, count);
			}
			return super.insertItem(slot, stack, simulate);
		}

		@Nonnull
		@Override
		public ItemStack extractItem (int slot, int amount, boolean simulate) {
			if (!simulate) {
				ItemStack test = super.extractItem(slot, amount, true);
				int current = RecipeItemHelper.pack(test);
				int curCount = itemReference.get(current);
				if (curCount != -1) {
					itemReference.put(current, curCount - test.getCount());
				}
			}
			ItemStack result = super.extractItem(slot, amount, simulate);
			if (!simulate) {
				if (getStackInSlot(slot).isEmpty()) {
					emptySlots++;
				}
			}
			return result;
		}
	}
}
