package com.aranaira.arcanearchives.block.entity;

import com.aranaira.arcanearchives.api.block.entity.IDomainBlockEntity;
import com.aranaira.arcanearchives.api.block.entity.IDomainEntityType;
import com.aranaira.arcanearchives.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.inventory.handlers.RadiantChestInventory;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;
import noobanidus.libs.noobutil.util.EnumUtil;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public enum DomainEntityType implements IDomainEntityType {
  CRYSTAL_WORKBENCH(CrystalWorkbenchBlockEntity.class, CrystalWorkbenchInventory::new, CrystalWorkbenchInventory.class, CrystalWorkbenchBlockEntity.INVENTORY_SLOTS),
  RADIANT_CHEST(RadiantChestBlockEntity.class, RadiantChestInventory::new, RadiantChestInventory.class, RadiantChestBlockEntity.INVENTORY_SLOTS),
  RADIANT_RESONATOR(RadiantResonatorBlockEntity.class)
  ;

  private final Class<? extends IDomainBlockEntity> typeClass;
  private final boolean hasInventory;
  @Nullable
  private final IntFunction<? extends AbstractLargeItemHandler> inventoryBuilder;
  @Nullable
  private final Class<? extends AbstractLargeItemHandler> inventoryClass;
  private final int inventorySize;

  DomainEntityType(Class<? extends IDomainBlockEntity> typeClass, @Nullable IntFunction<? extends AbstractLargeItemHandler> inventoryBuilder, @Nullable Class<? extends AbstractLargeItemHandler> inventoryClass, int inventorySize) {
    this.hasInventory = true;
    this.typeClass = typeClass;
    this.inventoryBuilder = inventoryBuilder;
    this.inventoryClass = inventoryClass;
    this.inventorySize = inventorySize;
  }

  DomainEntityType(Class<? extends IDomainBlockEntity> typeClass) {
    this.typeClass = typeClass;
    this.hasInventory = false;
    this.inventoryBuilder = null;
    this.inventoryClass = null;
    this.inventorySize = -1;
  }

  @Override
  public Class<? extends IDomainBlockEntity> getTypeClass() {
    return typeClass;
  }

  @Override
  public boolean hasInventory() {
    return hasInventory;
  }

  @Nullable
  @Override
  public IntFunction<? extends AbstractLargeItemHandler> getInventoryBuilder() {
    return inventoryBuilder;
  }

  @Nullable
  @Override
  public Class<? extends AbstractLargeItemHandler> getInventoryClass() {
    return inventoryClass;
  }

  @Override
  public int getInventorySize() {
    return inventorySize;
  }

  @Override
  public int getOrdinal() {
    return ordinal();
  }

  @Nullable
  public static DomainEntityType byOrdinal (int ordinal) {
    return EnumUtil.fromOrdinal(DomainEntityType.class, ordinal);
  }

  @Nullable
  public static DomainEntityType byClass (Class<? extends IDomainBlockEntity> clazz) {
    for (DomainEntityType type : values()) {
      if (type.typeClass.equals(clazz)) {
        return type;
      }
    }

    return null;
  }
}
