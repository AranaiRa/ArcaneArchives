package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.packets.PacketRadiantChestsListResponse;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.util.NetworkHelper;
import com.aranaira.arcanearchives.util.RadiantChestPlaceHolder;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ContainerManifest extends Container
{

	List<RadiantChestTileEntity> networkChests;
	List<ItemStack> ItemList = new ArrayList<>();
	EntityPlayer mPlayer;
	private boolean mServerSide;
	private ArcaneArchivesNetwork mAANetwork;


	public ContainerManifest(EntityPlayer playerIn, boolean ServerSide)
	{
		ArcaneArchives.logger.info(playerIn.getUniqueID());
		mServerSide = ServerSide;

		mPlayer = playerIn;
		mAANetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
		ManifestItemHandler.mInstance.mPlayer = playerIn;
		if(ServerSide)
		{
			mAANetwork.mManifestItemHandler.Clear();
			networkChests = mAANetwork.GetRadiantChests();
			for(int i = 0; i < networkChests.size(); i++)
			{
				List<ItemStack> items = new ArrayList();
				for(int j = 0; j < networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getSlots(); j++)
				{
					if(!networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(j).isEmpty())
					{
						ItemStack s = networkChests.get(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(j);
						items.add(s);
						boolean added = false;

						for(int k = 0; k < ItemList.size(); k++)
						{
							if(ItemComparison.AreItemsEqual(ItemList.get(k), s))
							{
								ItemList.get(k).grow(s.getCount());
								added = true;
							}
						}

						if(!added) ItemList.add(s.copy());
					}
				}
				mAANetwork.mManifestItemHandler.mChests.add(new RadiantChestPlaceHolder(networkChests.get(i).getPos(), items));
			}

			PacketRadiantChestsListResponse message = new PacketRadiantChestsListResponse(playerIn.getUniqueID(), ItemList, mAANetwork.mManifestItemHandler.mChests);

			AAPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) playerIn);
		}

		int i = 0;
		for(int y = 0; y < 9; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new SlotItemHandler(ManifestItemHandler.mInstance, i, x * 18 + 12, y * 18 + 30));
				i++;
			}
		}
	}


	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(mServerSide) return ItemStack.EMPTY;

		AATickHandler.GetInstance().clearChests();

		if(slotId < 0) return ItemStack.EMPTY;
		if(ManifestItemHandler.mInstance.getStackInSlot(slotId).isEmpty()) return ItemStack.EMPTY;


		RadiantChestPlaceHolder RCPH = null;
		for(RadiantChestPlaceHolder rcph : ManifestItemHandler.mInstance.mChests)
		{
			if(rcph.Contains(ManifestItemHandler.mInstance.getStackInSlot(slotId)))
			{
				AATickHandler.GetInstance().mBlockPositions.add(rcph.GetPosition());
			}
		}

		return ItemStack.EMPTY;
	}

	public void SetSearchString(String SearchText)
	{
		ManifestItemHandler.mInstance.setSearchText(SearchText);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}
}
