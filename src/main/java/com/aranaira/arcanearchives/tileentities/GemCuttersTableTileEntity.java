package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.packets.PacketGCTChangePage;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipeList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GemCuttersTableTileEntity extends AATileEntity implements ITickable
{
	public final static int RECIPE_PAGE_LIMIT = 7;

	private final ItemStackHandler mInventory = new ItemStackHandler(19);
	private GemCuttersTableRecipe mRecipe = null;
	private int curPage = 0;

	public GemCuttersTableTileEntity()
	{
		super();
		setName("gemcutterstable");
	}

	public ItemStackHandler getInventory()
	{
		return mInventory;
	}

	@Nullable
	public GemCuttersTableRecipe getRecipe()
	{
		return mRecipe;
	}

	public void setRecipe(ItemStack itemStack)
	{
		mRecipe = GemCuttersTableRecipeList.GetRecipe(itemStack);
		// "update output"
	}

	public void setRecipe(GemCuttersTableRecipe recipe)
	{
		mRecipe = recipe;
	}

	public int getPage()
	{
		return curPage;
	}

	public void setPage(int curPage)
	{
		this.curPage = curPage;
	}

	public boolean nextPage()
	{
		if(GemCuttersTableRecipeList.getSize() > (getPage() + 1) * RECIPE_PAGE_LIMIT)
		{
			curPage++;
			updateOutput();
			return true;
		} else
		{
			return false;
		}
	}

	public boolean previousPage()
	{
		if(getPage() == 0)
		{
			return false;
		} else
		{
			curPage--;
			updateOutput();
			return true;
		}
	}

	@Override
	public void update()
	{

	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		// I'm making this all worse
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mInventory);
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
		mInventory.deserializeNBT(compound.getCompoundTag("inventory"));

		ItemStack recipe = ItemStack.EMPTY;

		if(compound.hasKey("recipe"))
		{
			recipe = new ItemStack(compound.getCompoundTag("recipe"));
		}

		setPage(compound.getInteger("page"));
		setRecipe(recipe);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag("inventory", mInventory.serializeNBT());
		if(getRecipe() != null)
		{
			compound.setTag("recipe", getRecipe().getOutput().writeToNBT(new NBTTagCompound()));
		}
		compound.setInteger("page", getPage());

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
		ArcaneArchives.logger.info(Minecraft.getMinecraft().player.getDisplayNameString());
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}


	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void updateOutput()
	{
		SPacketUpdateTileEntity update = getUpdatePacket();
		if(world != null && !world.isRemote)
		{
			MinecraftServer server = world.getMinecraftServer();
			if(server != null)
				server.getPlayerList().sendToAllNearExcept(null, pos.getX(), pos.getY(), pos.getZ(), 128, world.provider.getDimension(), update);
		} else if(world != null)
		{
			PacketGCTChangePage packet = new PacketGCTChangePage(getPos(), getPage(), world.provider.getDimension());
			AAPacketHandler.CHANNEL.sendToServer(packet);
		}
	}
}
