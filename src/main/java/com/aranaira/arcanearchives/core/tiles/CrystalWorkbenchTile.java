package com.aranaira.arcanearchives.core.tiles;

import com.aranaira.arcanearchives.api.data.StoredInventory;
import com.aranaira.arcanearchives.api.tiles.IInventoryTile;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class CrystalWorkbenchTile extends ArcaneArchivesIdentifiedTile implements IInventoryTile<CrystalWorkbenchInventory>, INamedContainerProvider {
  private final StoredInventory<CrystalWorkbenchInventory> inventory = new StoredInventory<>(this::getTileId, CrystalWorkbenchInventory::new, CrystalWorkbenchInventory.EmptyArcaneWorkbenchInventory::new, 18);

  public CrystalWorkbenchTile(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Nullable
  @Override
  public CrystalWorkbenchInventory getTileInventory() {
    CrystalWorkbenchInventory result = inventory.getInventory(this.level);
    if (result == null) {
      return inventory.getEmpty();
    }
    return result;
  }

  @Override
  public CrystalWorkbenchInventory getEmptyInventory() {
    return inventory.getEmpty();
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("arcanearchives.container.crystal_workbench");
  }

  @Nullable
  @Override
  public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
    return new CrystalWorkbenchContainer(windowId, playerInventory, this);
  }
}
