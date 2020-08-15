package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.data.DataHelper;
import com.aranaira.arcanearchives.data.NetworkReferenceData;
import com.aranaira.arcanearchives.init.ModRecipes;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.registry.CrystalWorkbenchRegistry;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkAggregator;
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
import java.util.UUID;

public class CrystalWorkbenchTile extends NetworkedBaseTile {
  // TODO: Store this value somewhere else?
  public static final int RECIPE_PAGE_LIMIT = 7;

  private final ItemStackHandler inventory = new ItemStackHandler(18);
  private final ItemStackHandler outputInventory = new ItemStackHandler(1);
  private CrystalWorkbenchRecipe currentRecipe;
  private CrystalWorkbenchRecipe lastRecipe;
  private int page;

  public CrystalWorkbenchTile() {
    currentRecipe = ModRecipes.RADIANT_DUST.get();
  }

  public static final UUID invalid = UUID.fromString("00000000-0000-0000-0000-000000000000");

  public IItemHandlerModifiable getInventory() {
    return inventory;
  }

  public IItemHandlerModifiable getOutputInventory() {
    return outputInventory;
  }

  public void setRecipe(ResourceLocation name) {
    currentRecipe = CrystalWorkbenchRegistry.getRegistry().get(name);
    // TODO ??
    stateUpdate();
  }

  // TODO ??
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
    currentRecipe = CrystalWorkbenchRegistry.getRegistry().getValueByIndex(index);
    // TODO: Assert nonnull
  }

  public void clientSideUpdate() {
    if (world == null || !world.isRemote) {
      return;
    }

    int index = -1;

    if (currentRecipe != null) {
      index = currentRecipe.getIndex();
    }

    // TODO: Sent packet

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
  public CrystalWorkbenchRecipe getCurrentRecipe() {
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
    inventory.deserializeNBT(compound.getCompoundTag(Tags.CrystalWorkbench.inputInventory));
    outputInventory.deserializeNBT(compound.getCompoundTag(Tags.CrystalWorkbench.outputInventory));
    manuallySetRecipe(compound.getInteger(Tags.recipe)); // is this server-side or client-side?
  }

  public CrystalWorkbenchRecipe getLastRecipe() {
    return lastRecipe;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setTag(Tags.CrystalWorkbench.inputInventory, inventory.serializeNBT());
    compound.setTag(Tags.CrystalWorkbench.outputInventory, outputInventory.serializeNBT());
    compound.setInteger(Tags.recipe, currentRecipe == null ? -1 : currentRecipe.getIndex());

    return compound;
  }

  public void setLastRecipe(CrystalWorkbenchRecipe lastRecipe) {
    this.lastRecipe = lastRecipe;
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public boolean generatesNetworkId() {
    return true;
  }

  @Override
  @Nonnull
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound compound = writeToNBT(new NBTTagCompound());

    return new SPacketUpdateTileEntity(pos, 0, compound);
  }

  public void previousPage() {
    if (getPage() > 0) {
      setPage(page - 1);
    } else {
      int size = CrystalWorkbenchRegistry.getRegistry().size();
      int page = size / RECIPE_PAGE_LIMIT;
      if (size % RECIPE_PAGE_LIMIT == 0) {
        page = page - 1;
      }
      setPage(page);
    }
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public void nextPage() {
    int size = CrystalWorkbenchRegistry.getRegistry().size();
    if (size > (page + 1) * RECIPE_PAGE_LIMIT) {
      setPage(page + 1);
    } else {
      setPage(0);
    }
  }

  @Override
  public void generateNetworkId() {
    if (networkId == null && world != null && !world.isRemote) {
      networkId = NetworkAggregator.generateId();
      markDirty();
      stateUpdate();
      DataHelper.NetworkReference.addNetwork(this.networkId);
    }
  }
}
