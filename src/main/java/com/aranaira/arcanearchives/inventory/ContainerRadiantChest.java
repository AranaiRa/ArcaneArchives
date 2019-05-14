package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketNetworks;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

@ChestContainer(isLargeChest = true)
public class ContainerRadiantChest extends Container {
	private RadiantChestTileEntity tile;
	private boolean serverSide;
	private EntityPlayer player;

	public ContainerRadiantChest (RadiantChestTileEntity tile, EntityPlayer player, boolean serverSide) {
		this.tile = tile;
		this.player = player;
		this.serverSide = serverSide;

		IInventory playerInventory = player.inventory;

		IItemHandler handler = tile.getInventory();

		for (int j = 0; j < 6; ++j) {
			for (int k = 0; k < 9; ++k) {
				this.addSlotToContainer(new SlotItemHandler(handler, k + j * 9, 16 + k * 18, 16 + j * 18));
			}
		}

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 16 + j1 * 18, 142 + l * 18));
			}
		}

		for (int i1 = 0; i1 < 9; ++i1) {
			this.addSlotToContainer(new Slot(playerInventory, i1, 16 + i1 * 18, 200));
		}

		if (serverSide) {
			this.addListener(new RadiantChestListener());
		}
	}

	public String getName () {
		return tile.getChestName();
	}

	public void setName (String name) {
		tile.setChestName(name);
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot (EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack slotStack = slot.getStack();
			stack = slotStack.copy();

			//Chest inventory
			if (index < 54) {
				if (!mergeItemStack(slotStack, 54, 90, true)) {
					return ItemStack.EMPTY;
				}
			}
			//Players inventory
			else {
				if (!mergeItemStack(slotStack, 0, 54, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (slotStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}

		return stack;
	}

	@Override
	public boolean canInteractWith (@Nonnull EntityPlayer playerIn) {
		return true;
	}

	public RadiantChestTileEntity getTile () {
		return tile;
	}

	public class RadiantChestListener implements IContainerListener {
		private ServerNetwork network;
		private int lastUpdated = 0;

		public RadiantChestListener () {
			this.network = NetworkHelper.getServerNetwork(player.getUniqueID(), player.world);
			this.lastUpdated = player.ticksExisted;
		}

		@Override
		public void sendAllContents (Container containerToSend, NonNullList<ItemStack> itemsList) {
		}

		@Override
		public void sendSlotContents (Container containerToSend, int slotInd, ItemStack stack) {
			if (slotInd < 54) {
				sendManifestUpdate();
			}
		}

		private void sendManifestUpdate () {
			if (network != null && (player.ticksExisted - lastUpdated > 80)) {
				MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				if (server != null) {
					EntityPlayerMP player = server.getPlayerList().getPlayerByUUID(network.getUUID());
					if (player != null) {
						NBTTagCompound output = network.buildSynchroniseManifest();
						if (output != null) {
							PacketNetworks.Response packet = new PacketNetworks.Response(PacketNetworks.SynchroniseType.DATA, network.getUUID(), output);
							NetworkHandler.CHANNEL.sendTo(packet, player);
						}
					}
				}
				lastUpdated = player.ticksExisted;
			}
		}

		@Override
		public void sendWindowProperty (Container containerIn, int varToUpdate, int newValue) {
		}

		@Override
		public void sendAllWindowProperties (Container containerIn, IInventory inventory) {
		}
	}
}
