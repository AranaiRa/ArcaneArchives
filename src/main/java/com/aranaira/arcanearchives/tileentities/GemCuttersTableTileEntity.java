package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.RecipeLibrary;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GemCuttersTableTileEntity extends ImmanenceTileEntity implements IManifestTileEntity {
	private final ItemStackHandler inventory = new ItemStackHandler(18);
	public static final int RECIPE_PAGE_LIMIT = 7;
	private GCTRecipe currentRecipe;
	private GCTRecipe lastRecipe;
	private GCTRecipe penultimateRecipe;
	private int page;

	public GemCuttersTableTileEntity () {
		super("gemcutterstable");
		currentRecipe = RecipeLibrary.RADIANT_DUST_RECIPE;
	}

	@Override
	public String getDescriptor () {
		return "Gem Cutter's Table";
	}

	@Override
	public String getChestName () {
		return "";
	}

	@Override
	public IItemHandlerModifiable getInventory () {
		return inventory;
	}

	public void setRecipe (ResourceLocation name) {
		currentRecipe = GCTRecipeList.instance.getRecipe(name);
	}

	public void setRecipe (int index) {
		manuallySetRecipe(index);

		if (world != null && world.isRemote) {
			clientSideUpdate();
		} else if (world != null) {
			defaultServerSideUpdate();
		}
	}

	public void manuallySetRecipe (int index) {
		currentRecipe = GCTRecipeList.instance.getRecipeByIndex(index);
	}

	public static final ResourceLocation INVALID = new ResourceLocation(ArcaneArchives.MODID, "invalid_gct_recipe");

	public void clientSideUpdate () {
		if (world == null || !world.isRemote) {
			return;
		}

		ResourceLocation loc = INVALID;

		if (currentRecipe != null) {
			loc = currentRecipe.getName();
		}

		PacketGemCutters.ChangeRecipe packet = new PacketGemCutters.ChangeRecipe(loc, getPos(), world.provider.getDimension());
		NetworkHandler.CHANNEL.sendToServer(packet);
	}

	@Override
	public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		return super.getCapability(capability, facing);
	}

	@Nullable
	public GCTRecipe getCurrentRecipe () {
		return currentRecipe;
	}

	@Override
	public boolean hasCapability (Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

	}

	@Deprecated
	public boolean hasCurrentRecipe () {
		return currentRecipe != null;
	}

	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag(AATileEntity.Tags.INVENTORY));
		manuallySetRecipe(compound.getInteger(Tags.RECIPE)); // is this server-side or client-side?
	}

	public GCTRecipe getLastRecipe () {
		return lastRecipe;
	}

	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag(AATileEntity.Tags.INVENTORY, inventory.serializeNBT());
		if (currentRecipe != null) {
			compound.setInteger(Tags.RECIPE, currentRecipe.getIndex());
		}

		return compound;
	}

	public void setLastRecipe (GCTRecipe lastRecipe) {
		this.lastRecipe = lastRecipe;
	}

	@Override
	public NBTTagCompound getUpdateTag () {
		return writeToNBT(new NBTTagCompound());
	}

	public GCTRecipe getPenultimateRecipe () {
		return penultimateRecipe;
	}

	@Override
	public void onDataPacket (NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		super.onDataPacket(net, pkt);
	}

	public void updatePenultimateRecipe () {
		this.penultimateRecipe = this.lastRecipe;
	}

	@Override
	@Nonnull
	public SPacketUpdateTileEntity getUpdatePacket () {
		NBTTagCompound compound = writeToNBT(new NBTTagCompound());

		return new SPacketUpdateTileEntity(pos, 0, compound);
	}

	public void previousPage () {
		if (getPage() > 0) {
			setPage(page - 1);
		}
	}

	public int getPage () {
		return page;
	}

	public void setPage (int page) {
		this.page = page;
	}

	public void nextPage () {
		if (GCTRecipeList.instance.getSize() > (page + 1) * RECIPE_PAGE_LIMIT) {
			setPage(page + 1);
		} else {
			setPage(0);
		}
	}

	public static class Tags {
		public static final String RECIPE = "recipe";
	}


}
