package com.aranaira.arcanearchives.core.inventory.container;

import com.aranaira.arcanearchives.core.init.ModContainers;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.inventory.slot.CrystalWorkbenchSlot;
import com.aranaira.arcanearchives.core.inventory.slot.RecipeHandlerSlot;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CrystalWorkbenchContainer extends AbstractLargeContainer<CrystalWorkbenchInventory, CrystalWorkbenchBlockEntity> {
  private final List<Slot> ingredientSlots = new ArrayList<>();

  public CrystalWorkbenchContainer(ContainerType<? extends CrystalWorkbenchContainer> type, int id, PlayerInventory inventory, PacketBuffer buffer) {
    super(type, id, 2, inventory, buffer.readBlockPos());
    createInventorySlots();
    /*    createRecipeSlots();*/
    createPlayerSlots(166, 224, 23);
  }

  public CrystalWorkbenchContainer(int id, PlayerInventory playerInventory, CrystalWorkbenchBlockEntity tile) {
    super(ModContainers.CRYSTAL_WORKBENCH.get(), id, 2, playerInventory, tile);
    createInventorySlots();
/*    createRecipeSlots();*/
    createPlayerSlots(166, 224, 23);
  }

  protected void createRecipeSlots () {
    int slotIndex = slots.size();
    for (int col = 6; col > -1; col--) {
      this.addSlot(new RecipeHandlerSlot(slotIndex, col * 18 + 41, 70, getBlockEntity()));
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
    return slot instanceof RecipeHandlerSlot;
  }
}
