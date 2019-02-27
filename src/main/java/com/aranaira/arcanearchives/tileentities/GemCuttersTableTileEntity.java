package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipe;
import com.aranaira.arcanearchives.registry.crafting.GemCuttersTableRecipeList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GemCuttersTableTileEntity extends AATileEntity
{

	public static final int RECIPE_PAGE_LIMIT = 7;
	private final GemCuttersTableItemHandler mInventory = new GemCuttersTableItemHandler(18);
	private GemCuttersTableRecipe recipe = null;
	private int curPage = 0;

	public GemCuttersTableTileEntity()
	{
		super();
		setName("gemcutterstable");
	}

	public GemCuttersTableItemHandler getInventory()
	{
		return mInventory;
	}

	@Nullable
	public GemCuttersTableRecipe getRecipe()
	{
		return recipe;
	}

	public void setRecipe(int index)
	{
		this.recipe = GemCuttersTableRecipeList.getRecipeByIndex(index);

		if (this.world.isRemote) {
			clientSideUpdate();
		}
	}

	public void consume(UUID playerId)
	{
		EntityPlayer player = world.getPlayerEntityByUUID(playerId);
		if(player == null) return;

		GemCuttersTableRecipe recipe = getRecipe();
		if(recipe == null) return;

		InvWrapper plyInv = new InvWrapper(player.inventory);

		if(!recipe.matchesRecipe(this.mInventory, plyInv)) return;

		recipe.consume(this.mInventory, plyInv);
	}

	public int getPage()
	{
		return curPage;
	}

	public void setPage(int curPage)
	{
		this.curPage = curPage;
	}

	public void nextPage()
	{
		if(GemCuttersTableRecipeList.getSize() > (getPage() + 1) * RECIPE_PAGE_LIMIT)
			curPage++;
	}

	public void previousPage()
	{
		if(getPage() > 0)
			curPage--;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		// I'm making this all worse
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mInventory);
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
		mInventory.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		setRecipe(compound.getInteger(Tags.RECIPE)); // is this server-side or client-side?
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, mInventory.serializeNBT());
		int index = GemCuttersTableRecipeList.indexOf(getRecipe());
		compound.setInteger(Tags.RECIPE, index);

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

		// TODO
		int index = (recipe == null) ? -1 : recipe.getIndex();
		PacketGemCutters.ChangeRecipe packet = new PacketGemCutters.ChangeRecipe(index, getPos(), world.provider.getDimension());
		NetworkHandler.CHANNEL.sendToServer(packet);
	}

	public static class Tags
	{
		public static final String RECIPE = "recipe";
	}

	public static class GemCuttersTableItemHandler extends ItemStackHandler
	{
		private List<Runnable> hooks = new ArrayList<>();

		public GemCuttersTableItemHandler(int size)
		{
			super(size);
		}

		public void addHook(Runnable runnable)
		{
			this.hooks.add(runnable);
		}

		public void deleteHook(Runnable runnable)
		{
			this.hooks.remove(runnable);
		}

		@Override
		protected void onContentsChanged(int slot)
		{
			super.onContentsChanged(slot);

			this.hooks.stream().filter(Objects::nonNull).forEach(Runnable::run);
		}
	}
}
