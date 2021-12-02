package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.inventory.slot.IRecipeSlot;
import com.aranaira.arcanearchives.api.reference.Constants;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.init.ModContainers;
import com.aranaira.arcanearchives.core.init.ResolvingRecipes;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.inventory.slot.ClientCrystalWorkbenchRecipeRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchSlot;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.aranaira.arcanearchives.api.reference.Constants.CrystalWorkbench.DataArray;

public class CrystalWorkbenchContainer extends AbstractLargeContainer<CrystalWorkbenchInventory, CrystalWorkbenchBlockEntity> {
  private final List<Slot> ingredientSlots = new ArrayList<>();
  private CrystalWorkbenchCrafting workbench = null;

  private int attuneProgress = 0;
  private int deattuneProgress = 0;
  private int selectedSlot = -1;
  private int slotOffset = 0;

  private final IIntArray data = new IIntArray() {
    @Override
    public int get(int pIndex) {
      switch (pIndex) {
        case DataArray.SlotOffset:
          return slotOffset;
        case DataArray.SlotSelected:
          return selectedSlot;
        case DataArray.AttuneProgress:
          return attuneProgress;
        case DataArray.DeattuneProgress:
          return deattuneProgress;
      }

      return -1;
    }

    @Override
    public void set(int pIndex, int pValue) {
      switch (pIndex) {
        case DataArray.SlotOffset:
          setSlotOffset(pValue);
          break;
        case DataArray.SlotSelected:
          selectedSlot = pValue;
          refreshRecipeSlot();
          break;
        case DataArray.AttuneProgress:
          attuneProgress = pValue;
          break;
        case DataArray.DeattuneProgress:
          deattuneProgress = pValue;
          break;
      }
    }

    @Override
    public int getCount() {
      return Constants.CrystalWorkbench.DataArray.Count;
    }
  };

  private boolean refreshDim = true;

  private void refreshRecipeSlot() {
    // TODO:
    // 1. Create the actual recipe output slot.
    // 2. Refresh its contents whenever the slot is changed.
    // 3. Handle on-take recipe matching.
    if (refreshDim) {
      List<? extends IRecipeSlot<CrystalWorkbenchRecipe>> recipes = isClientSide() ? CLIENT_RECIPE_SLOTS : RECIPE_SLOTS;
      for (IRecipeSlot<CrystalWorkbenchRecipe> slot : recipes) {
        if (!slot.getSlot().hasItem()) {
          continue;
        }

        CrystalWorkbenchRecipe recipe = slot.getRecipe();
        if (recipe == null) {
          continue;
        }

        slot.setDimmed(!recipe.matches(getWorkbench(), getPlayerWorld()));
      }

      refreshDim = false;
    }
  }

  private void setSlotOffset(int pValue) {
    this.slotOffset = pValue;
    this.selectedSlot = -1;
    if (!isClientSide()) {
      for (CrystalWorkbenchRecipeSlot slot : RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    } else {
      for (ClientCrystalWorkbenchRecipeRecipeSlot slot : CLIENT_RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    }
  }

  private final List<CrystalWorkbenchRecipeSlot> RECIPE_SLOTS = new ArrayList<>();
  private final List<ClientCrystalWorkbenchRecipeRecipeSlot> CLIENT_RECIPE_SLOTS = new ArrayList<>();

  public CrystalWorkbenchContainer(ContainerType<? extends CrystalWorkbenchContainer> type, int id, PlayerInventory inventory, PacketBuffer buffer) {
    super(type, id, 2, inventory, buffer.readBlockPos());
    this.addDataSlots(data);
    construct();
  }

  public CrystalWorkbenchContainer(int id, PlayerInventory playerInventory, CrystalWorkbenchBlockEntity tile) {
    super(ModContainers.CRYSTAL_WORKBENCH.get(), id, 2, playerInventory, tile);
    this.addDataSlots(data);
    construct();
  }

  public CrystalWorkbenchCrafting getWorkbench() {
    if (workbench == null) {
      workbench = new CrystalWorkbenchCrafting(this, getBlockEntity(), getBlockEntityInventory());
    }
    return workbench;
  }

  protected void construct() {
    createInventorySlots();
    createPlayerSlots(166, 224, 23);
    createRecipeSlots();
    createOutputSlot();
    if (!isClientSide() && getBlockEntity() != null) {
      ResourceLocation recipe = DataStorage.getSelectedRecipe(getPlayer().getUUID(), getBlockEntity().getEntityId());
      if (recipe != null) {
        int index = ResolvingRecipes.CRYSTAL_WORKBENCH.lookup(recipe);
        if (index != -1) {
          int mod = index % Constants.CrystalWorkbench.RecipeSlots;
          int page = (index / Constants.CrystalWorkbench.RecipeSlots);
          setData(DataArray.SlotOffset, page);
          setData(DataArray.SlotSelected, mod);
        }
      }
    }
    addSlotListener(new RefreshContainerListener());
  }

  protected void createRecipeSlots() {
    int slotIndex = 0;
    for (int col = 0; col < 7; col++) {
      if (isClientSide()) {
        ClientCrystalWorkbenchRecipeRecipeSlot slot = new ClientCrystalWorkbenchRecipeRecipeSlot(slotIndex, col * 18 + 41, 70);
        CLIENT_RECIPE_SLOTS.add(slot);
        this.addSlot(slot);
      } else {
        CrystalWorkbenchRecipeSlot slot = new CrystalWorkbenchRecipeSlot(slotIndex, col * 18 + 41, 70);
        RECIPE_SLOTS.add(slot);
        this.addSlot(slot);
      }
      slotIndex++;
    }
  }

  protected void createOutputSlot() {

  }

  @Override
  protected CrystalWorkbenchBlockEntity resolveBlockEntity(TileEntity be) {
    if (be instanceof CrystalWorkbenchBlockEntity) {
      return (CrystalWorkbenchBlockEntity) be;
    } else {
      return null;
    }
  }

  protected void createInventorySlots() {
    int slotIndex = 0;
    for (int row = 0; row < 2; ++row) {
      for (int col = 0; col < 9; ++col) {
        int x = 23 + col * 18;
        int y = 105 + row * 18;
        Slot slot;
        if (isClientSide()) {
          slot = new CrystalWorkbenchSlot(this.getEmptyInventory(), slotIndex, x, y);
        } else {
          slot = new CrystalWorkbenchSlot(this.getBlockEntityInventory(), slotIndex, x, y);
        }
        ingredientSlots.add(slot);
        this.addSlot(slot);
        slotIndex++;
      }
    }
  }

  @Nullable
  @Override
  public CrystalWorkbenchInventory getEmptyInventory() {
    return CrystalWorkbenchInventory.getEmpty();
  }

  @Override
  public List<Slot> getIngredientSlots() {
    return ingredientSlots;
  }

  @Override
  protected boolean skipSlot(Slot slot) {
    return slot instanceof IRecipeSlot;
  }

  @Override
  public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (!isClientSide() && slotId >= 0) {
      Slot slot = getSlot(slotId);
      if (slot instanceof IRecipeSlot) {
        IRecipeSlot<?> recipeSlot = (IRecipeSlot<?>) slot;
        if (slot.hasItem()) {
          setData(DataArray.SlotSelected, recipeSlot.getIndex());
          this.refreshDim = true;
        }
      }
    }

    return super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  // Only ever called on the server
  public boolean clickMenuButton(PlayerEntity player, int slot) {
    if (slot == 1 || slot == 2) {
      this.refreshDim = true;
      int size = ResolvingRecipes.CRYSTAL_WORKBENCH.size();
      int displayed = Constants.CrystalWorkbench.RecipeSlots;
      int count = size / displayed + ((size % displayed == 0) ? 1 : 0);
      if (slot == 1) {
        if (slotOffset - 1 < 0) {
          // wrap around
          setSlotOffset(count);
        } else {
          setSlotOffset(--slotOffset);
        }
      } else {
        if (slotOffset + 1 > count) {
          setSlotOffset(0);
        } else {
          setSlotOffset(++slotOffset);
        }
      }
      setData(DataArray.SlotOffset, slotOffset);
      setData(DataArray.SlotSelected, selectedSlot);
    }

    return false;
  }

  public int getSelectedSlot() {
    return selectedSlot;
  }

  @Nullable
  public IRecipeSlot<?> getSelectedRecipeSlot() {
    if (selectedSlot < 0) {
      return null;
    }

    if (isClientSide()) {
      return CLIENT_RECIPE_SLOTS.get(selectedSlot);
    } else {
      return RECIPE_SLOTS.get(selectedSlot);
    }
  }

  @Override
  public void removed(PlayerEntity player) {
    if (!isClientSide() && getBlockEntity() != null) {
      IRecipeSlot<?> slot = getSelectedRecipeSlot();
      if (slot != null) {
        DataStorage.setSelectedRecipe(player.getUUID(), getBlockEntity().getEntityId(), slot.getRegistryName());
      }
    }
    super.removed(player);
  }

  protected class RefreshContainerListener implements IContainerListener {
    @Override
    public void refreshContainer(Container pContainerToSend, NonNullList<ItemStack> pItemsList) {
      CrystalWorkbenchContainer.this.refreshDim = true;
      CrystalWorkbenchContainer.this.refreshRecipeSlot();
    }

    @Override
    public void slotChanged(Container pContainerToSend, int pSlotInd, ItemStack pStack) {
      CrystalWorkbenchContainer.this.refreshDim = true;
      CrystalWorkbenchContainer.this.refreshRecipeSlot();
    }

    @Override
    public void setContainerData(Container pContainer, int pVarToUpdate, int pNewValue) {
      CrystalWorkbenchContainer.this.refreshRecipeSlot();
    }
  }
}
