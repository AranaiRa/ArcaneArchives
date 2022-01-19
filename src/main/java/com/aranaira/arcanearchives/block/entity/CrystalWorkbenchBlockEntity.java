package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.inventory.StoredInventory;
import com.aranaira.arcanearchives.api.data.UUIDNameData;
import com.aranaira.arcanearchives.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.inventory.handlers.CrystalWorkbenchInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import noobanidus.libs.noobutil.block.entities.IInventoryBlockEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class CrystalWorkbenchBlockEntity extends DomainIdentifiedBlockEntity implements IInventoryBlockEntity<CrystalWorkbenchInventory>, INamedContainerProvider {
  public static final int INVENTORY_SLOTS = 18;

  private final StoredInventory<CrystalWorkbenchInventory> inventory = new StoredInventory<>(this::getEntityId, CrystalWorkbenchInventory::new, CrystalWorkbenchInventory.EmptyArcaneWorkbenchInventory::new, INVENTORY_SLOTS);

  public CrystalWorkbenchBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Nullable
  @Override
  public CrystalWorkbenchInventory getBlockEntityInventory() {
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
    ArcaneArchivesAPI.LOG.info("Identifier: " + getEntityId());
    return new CrystalWorkbenchContainer(windowId, playerInventory, IWorldPosCallable.create(player.level, getBlockPos()));
  }

  @Nullable
  @Override
  public UUID getDomainId() {
    return getEntityId();
  }

  @Override
  public UUIDNameData.Name getDomainName() {
    return getEntityName();
  }
}
