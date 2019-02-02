package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.aranaira.arcanearchives.common.ManifestItemHandler.ManifestEntry;

public class ContainerManifest extends Container
{

	private ArcaneArchivesClientNetwork clientNetwork = null;
	private ManifestItemHandler handler;
	private boolean serverSide;
	private EntityPlayer player;

	public ContainerManifest(EntityPlayer playerIn, boolean ServerSide)
	{
		this.serverSide = ServerSide;
		this.player = playerIn;

		if(ServerSide) return; // all of this is done on the client

		clientNetwork = NetworkHelper.getArcaneArchivesClientNetwork(this.player);
		handler = clientNetwork.getManifestHandler();

		//clientNetwork.registerCallback(PacketSynchronise.SynchroniseType.MANIFEST, this, this::updateHandlerAndSlots);

		/*updateHandlerAndSlots();
	}

	public void updateHandlerAndSlots () {*/
		int i = 0;
		for(int y = 0; y < 9; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new SlotItemHandler(handler, i, x * 18 + 12, y * 18 + 30));
				i++;
			}
		}
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(this.serverSide) return ItemStack.EMPTY;

		ManifestEntry entry = handler.getManifestEntryInSlot(slotId);

		if(entry == null) return ItemStack.EMPTY;

		AATickHandler.GetInstance().mBlockPositions.addAll(entry.getVecPositions());

		return ItemStack.EMPTY;
	}

	public void SetSearchString(String SearchText)
	{
		handler.setSearchText(SearchText);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}
}
