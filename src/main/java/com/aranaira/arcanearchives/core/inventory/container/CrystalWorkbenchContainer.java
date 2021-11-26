package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.api.data.DataStorage;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.init.ModContainers;
import com.aranaira.arcanearchives.core.init.ResolvingRecipes;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.inventory.slot.ClientCrystalWorkbenchRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchRecipeSlot;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchSlot;
import com.aranaira.arcanearchives.core.inventory.slot.IRecipeSlot;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CrystalWorkbenchContainer extends AbstractLargeContainer<CrystalWorkbenchInventory, CrystalWorkbenchBlockEntity> {
  private final List<Slot> ingredientSlots = new ArrayList<>();

  private int attuneProgress = 0;
  private int deattuneProgress = 0;
  private int selectedSlot = -1;
  private int slotOffset = 0;

  private final IIntArray data = new IIntArray() {
    @Override
    public int get(int pIndex) {
      if (pIndex == 0) {
        return slotOffset;
      } else if (pIndex == 1) {
        return attuneProgress;
      } else if (pIndex == 2) {
        return deattuneProgress;
      } else if (pIndex == 3) {
        return selectedSlot;
      }

      return -1;
    }

    @Override
    public void set(int pIndex, int pValue) {
      if (pIndex == 0) {
        setSlotOffset(pValue);
      } else if (pIndex == 1) {
        attuneProgress = pValue;
      } else if (pIndex == 2) {
        deattuneProgress = pValue;
      } else if (pIndex == 3) {
        selectedSlot = pValue;
      }
    }

    @Override
    public int getCount() {
      return 4;
    }
  };

  private void setSlotOffset(int pValue) {
    this.slotOffset = pValue;
    this.selectedSlot = -1;
    if (!isClientSide()) {
      for (CrystalWorkbenchRecipeSlot slot : RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    } else {
      for (ClientCrystalWorkbenchRecipeSlot slot : CLIENT_RECIPE_SLOTS) {
        slot.setOffset(pValue);
      }
    }
  }

  private final List<CrystalWorkbenchRecipeSlot> RECIPE_SLOTS = new ArrayList<>();
  private final List<ClientCrystalWorkbenchRecipeSlot> CLIENT_RECIPE_SLOTS = new ArrayList<>();

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

  protected void construct() {
    createInventorySlots();
    createPlayerSlots(166, 224, 23);
    createRecipeSlots();
    if (!isClientSide() && getBlockEntity() != null) {
      ResourceLocation recipe = DataStorage.getSelectedRecipe(getPlayer().getUUID(), getBlockEntity().getEntityId());
      if (recipe != null) {
        int index = ResolvingRecipes.CRYSTAL_WORKBENCH.lookup(recipe);
        if (index != -1) {
          int mod = index % IRecipeSlot.getRecipeCount();
          int page = (index / IRecipeSlot.getRecipeCount());
          setData(0, page);
          setData(3, mod);
        }
      }
    }
  }

  protected void createRecipeSlots() {
    int slotIndex = 0;
    for (int col = 0; col < 7; col++) {
      if (isClientSide()) {
        ClientCrystalWorkbenchRecipeSlot slot = new ClientCrystalWorkbenchRecipeSlot(slotIndex, col * 18 + 41, 70);
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
    if (slotId >= 0) {
      Slot slot = getSlot(slotId);
      if (slot instanceof IRecipeSlot) {
        IRecipeSlot<?> recipeSlot = (IRecipeSlot<?>) slot;
        if (slot.hasItem()) {
          setData(3, recipeSlot.getIndex());
        }
      }
    }

    return super.clicked(slotId, dragType, clickTypeIn, player);
  }

  @Override
  // Only ever called on the server
  public boolean clickMenuButton(PlayerEntity player, int slot) {
    if (slot == 1 || slot == 2) {
      int size = ResolvingRecipes.CRYSTAL_WORKBENCH.size();
      int displayed = IRecipeSlot.getRecipeCount();
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
      setData(0, slotOffset);
      setData(3, selectedSlot);
    }

    return false;
  }

  public int getSelectedSlot() {
    return selectedSlot;
  }

  @Nullable
  public IRecipeSlot<?> getSelectedRecipeSlot () {
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
}
