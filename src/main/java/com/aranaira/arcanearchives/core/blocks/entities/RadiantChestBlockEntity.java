package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.blockentities.IInventoryBlockEntity;
import com.aranaira.arcanearchives.api.data.StoredInventory;
import com.aranaira.arcanearchives.core.inventory.container.RadiantChestContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.RadiantChestInventory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class RadiantChestBlockEntity extends ArcaneArchivesIdentifiedBlockEntity implements IInventoryBlockEntity<RadiantChestInventory>, INamedContainerProvider {
  private final StoredInventory<RadiantChestInventory> inventory = new StoredInventory<>(this::getEntityId, RadiantChestInventory::new, RadiantChestInventory.EmptyRadiantChestInventory::new, 54);

  public RadiantChestBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public RadiantChestInventory getBlockEntityInventory() {
    RadiantChestInventory result = inventory.getInventory(this.level);
    if (result == null) {
      return getEmptyInventory();
    }
    return result;
  }

  @Override
  public RadiantChestInventory getEmptyInventory() {
    return inventory.getEmpty();
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    return super.save(compound);
  }

  @Override
  public void load(BlockState state, CompoundNBT compound) {
    super.load(state, compound);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("arcanearchives.container.radiant_chest");
  }

  @Nullable
  @Override
  public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
    return new RadiantChestContainer(windowId, playerInventory, this);
  }
}
