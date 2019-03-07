package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.data.ClientNetwork;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.data.ServerNetwork;
import com.aranaira.arcanearchives.events.LineHandler;
import com.aranaira.arcanearchives.inventory.handlers.FakeManifestItemHandler;
import com.aranaira.arcanearchives.inventory.handlers.ManifestItemHandler;
import com.aranaira.arcanearchives.util.types.ManifestEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ContainerManifest extends Container
{

	private ClientNetwork clientNetwork = null;
	private ManifestItemHandler handler;
	private boolean serverSide;
	private EntityPlayer player;

	public ContainerManifest(EntityPlayer playerIn, boolean ServerSide)
	{
		this.serverSide = ServerSide;
		this.player = playerIn;

		if(ServerSide)
		{
			handler = new FakeManifestItemHandler();
			int i = 0;
			for(int y = 0; y < 9; y++)
			{
				for(int x = 0; x < 9; x++)
				{
					this.addSlotToContainer(new SlotItemHandler(handler, i, x * 18 + 12, y * 18 + 30));
					i++;
				}
			}
		} else
		{
			clientNetwork = NetworkHelper.getClientNetwork(this.player.getUniqueID());
			handler = clientNetwork.getManifestHandler();

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
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Nullable
	public ManifestEntry getEntry(int slotId)
	{
		return handler.getManifestEntryInSlot(slotId);
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(this.serverSide) return ItemStack.EMPTY;

		ManifestEntry entry = handler.getManifestEntryInSlot(slotId);

		if(entry == null) return ItemStack.EMPTY;

		if(entry.getDimension() != player.dimension) return ItemStack.EMPTY;

		List<Vec3d> visPositions = entry.getVecPositions();
		visPositions.forEach(LineHandler::addLine);

		if(!GuiScreen.isShiftKeyDown())
		{
			Minecraft mc = Minecraft.getMinecraft();
			mc.displayGuiScreen(null);
		}

		return ItemStack.EMPTY;
	}

	public void SetSearchString(String SearchText)
	{
		handler.setSearchText(SearchText);
	}

	public String getSearchString()
	{
		return handler.getSearchText();
	}


}
