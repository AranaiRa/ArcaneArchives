package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.api.inventory.slot.CappedSlot;
import com.aranaira.arcanearchives.api.inventory.slot.IRecipeSlot;
import com.aranaira.arcanearchives.api.reference.Constants;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.init.ModContainers;
import com.aranaira.arcanearchives.core.init.ResolvingRecipes;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.inventory.slot.ClientCrystalWorkbenchRecipeRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchResultSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchSlot;
import com.aranaira.arcanearchives.core.network.Networking;
import com.aranaira.arcanearchives.core.network.packets.RecipeSyncPacket;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import noobanidus.libs.noobutil.inventory.ILargeInventory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.aranaira.arcanearchives.api.reference.Constants.CrystalWorkbench.DataArray;

public class CrystalWorkbenchContainer extends AbstractLargeContainer<CrystalWorkbenchInventory, CrystalWorkbenchBlockEntity> {
  private final List<Slot> ingredientSlots = new ArrayList<>();
  private final List<Slot> playerSlots = new ArrayList<>();
  private final List<Slot> allSlots = new ArrayList<>();
  private CrystalWorkbenchCrafting workbench = null;
  private CrystalWorkbenchResultSlot result = null;

  // Container Synchronized
  private int attuneProgress = 0;
  private int deattuneProgress = 0;
  private int slotOffset = 0;
  private int selectedSlot = -1;

  private ResourceLocation selectedRecipe = Constants.Recipes.NoRecipe;

  private final IIntArray data = new IIntArray() {
    @Override
    public int get(int pIndex) {
      switch (pIndex) {
        case DataArray.SlotOffset:
          return slotOffset;
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

  private void refreshRecipesAndSlot() {
    List<? extends IRecipeSlot<CrystalWorkbenchRecipe>> recipes = isClientSide() ? CLIENT_RECIPE_SLOTS : RECIPE_SLOTS;
    for (IRecipeSlot<CrystalWorkbenchRecipe> slot : recipes) {
      if (!slot.getSlot().hasItem()) {
        continue;
      }

      CrystalWorkbenchRecipe recipe = slot.getRecipe();
      if (recipe == null) {
        continue;
      }

      boolean matches = recipe.matches(getWorkbench(), getPlayerWorld());
      slot.setDimmed(!matches);

      if (recipe.getId().equals(this.selectedRecipe) && result != null && matches) {
        this.result.setRecipe(recipe);
      } else if (result != null) {
        this.result.setRecipe(null);
      }
    }
  }

  private void setSlotOffset(int pValue) {
    this.slotOffset = pValue;
    if (!isClientSide()) {
      for (CrystalWorkbenchRecipeSlot slot : RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    } else {
      for (ClientCrystalWorkbenchRecipeRecipeSlot slot : CLIENT_RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    }
    setSelectedSlotFromRecipe();
  }


  public CrystalWorkbenchCrafting getWorkbench() {
    if (workbench == null) {
      workbench = new CrystalWorkbenchCrafting(this, getBlockEntity(), getBlockEntityInventory());
    }
    return workbench;
  }

  protected void construct() {
    createInventorySlots();
    createPlayerSlots(166, 224, 23, playerSlots);
    createRecipeSlots();
    createOutputSlot();
    if (!isClientSide() && getBlockEntity() != null) {
      ResourceLocation recipe = DataStorage.getSelectedRecipe(getPlayer().getUUID(), getBlockEntity().getEntityId());
      if (recipe != null) {
        int index = ResolvingRecipes.CRYSTAL_WORKBENCH.lookup(recipe);
        if (index != -1) {
          int page = (index / Constants.CrystalWorkbench.RecipeSlots);
          setData(DataArray.SlotOffset, page);
          result.setRecipe(ResolvingRecipes.CRYSTAL_WORKBENCH.getRecipe(index));
        }
        setRecipe(recipe);
      }
    }
    if (isClientSide() && this.getEmptyInventory() != null) {
      this.getEmptyInventory().addListener(CrystalWorkbenchContainer.this::inventoryChanged);
    } else if (this.getBlockEntityInventory() != null) {
      this.getBlockEntityInventory().addListener(CrystalWorkbenchContainer.this::inventoryChanged);
    }
    for (Slot slot : this.playerSlots) {
      ((CappedSlot) slot).addListener(CrystalWorkbenchContainer.this::inventoryChanged);
    }
    allSlots.addAll(ingredientSlots);
    allSlots.addAll(playerSlots);
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

  // TODO: Convert this to potential use CraftResultInventory, IWorldPosCallable
  // TODO: and WorkbenchContainer::slotChangedCraftingGrid
  protected void createOutputSlot() {
    result = new CrystalWorkbenchResultSlot(getPlayer(), this::getWorkbench, new Inventory(1), 0, 95, 18);
/*   {
      @Override
      public ItemStack onTake(PlayerEntity pPlayer, ItemStack pStack) {
        ItemStack result = super.onTake(pPlayer, pStack);
        if (getRecipe() != null && getRecipe().matches(CrystalWorkbenchContainer.this.getWorkbench(), pPlayer.level)) {
          setRecipe(getRecipe());
        }
        return result;
      }
    };*/
    this.addSlot(result);
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
    if (getBlockEntity() == null) {
      return null;
    }
    return getBlockEntity().getEmptyInventory();
  }

  @Override
  public void inventoryChanged(ILargeInventory inventory, int slot) {
    refreshRecipesAndSlot();
  }

  @Override
  public void inventoryChanged(IInventory inventory, int slot) {
    refreshRecipesAndSlot();
  }

  @Override
  public List<Slot> getIngredientSlots() {
    return ingredientSlots;
  }

  @Override
  public List<Slot> getCombinedIngredientSlots() {
    return allSlots;
  }

  @Override
  protected boolean skipSlot(Slot slot) {
    return slot instanceof IRecipeSlot;
  }

  @SuppressWarnings("unchecked")
  @Override
  public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
    if (!isClientSide() && slotId >= 0) {
      Slot slot = getSlot(slotId);
      if (slot instanceof IRecipeSlot) {
        IRecipeSlot<CrystalWorkbenchRecipe> recipeSlot = (IRecipeSlot<CrystalWorkbenchRecipe>) slot;
        if (recipeSlot.getIndex() != selectedSlot && slot.hasItem() && recipeSlot.getRecipe() != null && recipeSlot.getRecipe().matches(getWorkbench(), getPlayerWorld())) {
          setRecipe(recipeSlot.getRegistryName());
          result.setRecipe(recipeSlot.getRecipe());
          broadcastChanges();
        }
      }
    }

    return super.clicked(slotId, dragType, clickTypeIn, player);
  }

  public void setSelectedSlotFromRecipe() {
    if (this.selectedRecipe == null || this.selectedRecipe == Constants.Recipes.NoRecipe) {
      return;
    }

    boolean set = false;

    for (IRecipeSlot<?> slot : isClientSide() ? CLIENT_RECIPE_SLOTS : RECIPE_SLOTS) {
      if (!slot.getSlot().hasItem()) {
        continue;
      }

      ResourceLocation inSlot = slot.getRegistryName();
      if (this.selectedRecipe.equals(inSlot)) {
        set = true;
        selectedSlot = slot.getIndex();
        break;
      }
    }
    if (!set) {
      selectedSlot = -1;
    }
  }

  @Override
  // Only ever called on the server
  public boolean clickMenuButton(PlayerEntity player, int slot) {
    if (slot == 1 || slot == 2) {
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
      setSelectedSlotFromRecipe();
      refreshRecipesAndSlot();
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
    if (!isClientSide() && getBlockEntity() != null && selectedRecipe != null && selectedRecipe != Constants.Recipes.NoRecipe) {
      DataStorage.setSelectedRecipe(player.getUUID(), getBlockEntity().getEntityId(), selectedRecipe);
    }
    super.removed(player);
  }

  @Override
  public List<Slot> getPlayerSlots() {
    return playerSlots;
  }

  public ResourceLocation getRecipe() {
    return this.selectedRecipe;
  }

  public void setRecipe(ResourceLocation recipe) {
    this.selectedRecipe = recipe;

    if (!isClientSide()) {
      Networking.sendTo(new RecipeSyncPacket(recipe, this.containerId), (ServerPlayerEntity) getPlayer());
    } else {
      this.result.setRecipe(ResolvingRecipes.CRYSTAL_WORKBENCH.getRecipe(recipe));
    }

    setSelectedSlotFromRecipe();
  }
}
