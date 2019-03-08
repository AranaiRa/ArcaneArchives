package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.inventory.handlers.SharedGCTData;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GemCuttersTableTileEntity extends AATileEntity
{
	private final IItemHandlerModifiable inventory = new ItemStackHandler(18);
	private SharedGCTData sharedData = new SharedGCTData();

	public GemCuttersTableTileEntity()
	{
		super();
		setName("gemcutterstable");
	}

	public IItemHandlerModifiable getInventory()
	{
		return inventory;
	}

	public void manuallySetRecipe(int index)
	{
		sharedData.setCurrentRecipe(GCTRecipeList.getRecipeByIndex(index));
	}

	public void setRecipe(int index)
	{
		manuallySetRecipe(index);

		if(world != null && world.isRemote)
		{
			clientSideUpdate();
		} else if(world != null && !world.isRemote)
		{
			defaultServerSideUpdate();
		}
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		manuallySetRecipe(compound.getInteger(Tags.RECIPE)); // is this server-side or client-side?
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
		if(sharedData.hasCurrentRecipe())
		{
			int index = GCTRecipeList.indexOf(sharedData.getCurrentRecipe());
			compound.setInteger(Tags.RECIPE, index);
		}

		return compound;
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void clientSideUpdate()
	{
		if(world == null || !world.isRemote) return;

		int index = sharedData.hasCurrentRecipe() ? sharedData.getCurrentRecipe().getIndex() : -1;
		PacketGemCutters.ChangeRecipe packet = new PacketGemCutters.ChangeRecipe(index, getPos(), world.provider.getDimension());
		NetworkHandler.CHANNEL.sendToServer(packet);
	}

	public SharedGCTData getSharedData()
	{
		return sharedData;
	}

	public static class Tags
	{
		public static final String RECIPE = "recipe";
	}
}
