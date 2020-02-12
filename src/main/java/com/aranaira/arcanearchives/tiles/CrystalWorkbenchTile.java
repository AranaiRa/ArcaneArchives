package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.gct.IGCTRecipe;
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

public class CrystalWorkbenchTile extends BaseTile {
  private final ItemStackHandler inventory = new ItemStackHandler(18);
  private final ItemStackHandler outputInventory = new ItemStackHandler(1);
  public static final int RECIPE_PAGE_LIMIT = 7;
  private IGCTRecipe currentRecipe;
  private IGCTRecipe lastRecipe;
  private IGCTRecipe penultimateRecipe;
  private int page;

  public CrystalWorkbenchTile() {
/*    currentRecipe = RecipeLibrary.RADIANT_DUST_RECIPE;*/
  }

  public IItemHandlerModifiable getInventory() {
    return inventory;
  }

  public IItemHandlerModifiable getOutputInventory() {
    return outputInventory;
  }

  public void setRecipe(ResourceLocation name) {
/*    currentRecipe = GCTRecipeList.instance.getRecipe(name);*/
    stateUpdate();
  }

  public void setRecipe(int index) {
    manuallySetRecipe(index);
    markDirty();

    if (world != null && world.isRemote) {
      clientSideUpdate();
    } else if (world != null) {
      stateUpdate();
    }
  }

  public void manuallySetRecipe(int index) {
/*    currentRecipe = GCTRecipeList.instance.getRecipeByIndex(index);*/
  }

  public static final ResourceLocation INVALID = new ResourceLocation(ArcaneArchives.MODID, "invalid_gct_recipe");

  public void clientSideUpdate() {
    if (world == null || !world.isRemote) {
      return;
    }

    ResourceLocation loc = INVALID;

    if (currentRecipe != null) {
      loc = currentRecipe.getName();
    }

/*    PacketGemCutters.ChangeRecipe packet = new PacketGemCutters.ChangeRecipe(loc, getPos(), world.provider.getDimension());
    Networking.CHANNEL.sendToServer(packet);*/
  }

  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
    }
    return super.getCapability(capability, facing);
  }

  @Nullable
  public IGCTRecipe getCurrentRecipe() {
    return currentRecipe;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

  }

  @Deprecated
  public boolean hasCurrentRecipe() {
    return currentRecipe != null;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
    manuallySetRecipe(compound.getInteger(Tags.RECIPE)); // is this server-side or client-side?
  }

  public IGCTRecipe getLastRecipe() {
    return lastRecipe;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setTag("Inventory", inventory.serializeNBT());
    if (currentRecipe != null) {
      compound.setInteger(Tags.RECIPE, currentRecipe.getIndex());
    }

    return compound;
  }

  public void setLastRecipe(IGCTRecipe lastRecipe) {
    this.lastRecipe = lastRecipe;
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  public IGCTRecipe getPenultimateRecipe() {
    return penultimateRecipe;
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  public void updatePenultimateRecipe() {
    this.penultimateRecipe = this.lastRecipe;
  }

  @Override
  @Nonnull
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound compound = writeToNBT(new NBTTagCompound());

    return new SPacketUpdateTileEntity(pos, 0, compound);
  }

/*  public void previousPage() {
    if (getPage() > 0) {
      setPage(page - 1);
    } else {
      int page = GCTRecipeList.instance.size() / RECIPE_PAGE_LIMIT;
      if (GCTRecipeList.instance.size() % RECIPE_PAGE_LIMIT == 0) {
        page = page - 1;
      }
      setPage(page);
    }
  }*/

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

/*  public void nextPage() {
    if (GCTRecipeList.instance.size() > (page + 1) * RECIPE_PAGE_LIMIT) {
      setPage(page + 1);
    } else {
      setPage(0);
    }
  }*/

}
