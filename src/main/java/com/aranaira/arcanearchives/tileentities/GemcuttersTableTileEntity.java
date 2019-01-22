package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.common.GCTItemHandler;
import com.aranaira.arcanearchives.init.BlockLibrary;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GemcuttersTableTileEntity extends AATileEntity implements ITickable
{
	private String mName = "gemcutterstable";
	private final IItemHandler mInventory = new GCTItemHandler(54);
	public List<BlockPos> mAccessors = new ArrayList<>();

	public GemcuttersTableTileEntity()
	{
		BlockLibrary.TILE_ENTITIES.put(mName, this);
	}

	@Override
	public void update()
	{

	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nonnull EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mInventory);
		return super.getCapability(capability, facing);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nonnull EnumFacing facing)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		// Inventory
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(mInventory, null, compound.getTagList("inventory", NBT.TAG_COMPOUND));


		// TODO: Null checks
		if(compound.hasKey("recipe"))
			((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setRecipe(new ItemStack((NBTTagCompound) compound.getTag("recipe")));
		((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setPage(compound.getInteger("page"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		// Inventory
		compound.setTag("inventory", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(mInventory, null)); // TODO: Null check

		NBTTagCompound tag = super.getUpdateTag();
		if(((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe != null) // TODO here
		{
			NBTTagCompound item = ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe.getOutput().writeToNBT(tag); // TODO here
			tag.setTag("recipe", item);
		}
		tag.setInteger("page", ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getPage()); // TODO here

		return compound;
	}

	public String getName()
	{
		return mName;
	}


	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = super.getUpdateTag();
		if(((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe != null)
		{
			NBTTagCompound item = ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe.getOutput().writeToNBT(tag);
			tag.setTag("recipe", item);
		}
		tag.setInteger("page", ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getPage());
		return tag;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		ArcaneArchives.logger.info(Minecraft.getMinecraft().player.getDisplayNameString());
		if(pkt.getNbtCompound().hasKey("recipe"))
			((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setRecipe(new ItemStack((NBTTagCompound) pkt.getNbtCompound().getTag("recipe")));
		((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).setPage(pkt.getNbtCompound().getInteger("page"));
		super.onDataPacket(net, pkt);
	}


	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = new NBTTagCompound();

		if(((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe != null)
		{
			NBTTagCompound item = ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).mRecipe.getOutput().writeToNBT(compound);
			compound.setTag("recipe", item);
		}

		compound.setInteger("page", ((GCTItemHandler) this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).getPage());

		SPacketUpdateTileEntity spute = new SPacketUpdateTileEntity(pos, 0, compound);
		return spute;
	}
}
