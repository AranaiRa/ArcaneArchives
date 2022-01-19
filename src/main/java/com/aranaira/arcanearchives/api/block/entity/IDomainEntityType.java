package com.aranaira.arcanearchives.api.block.entity;

import noobanidus.libs.noobutil.inventory.ILargeInventory;
import noobanidus.libs.noobutil.recipe.AbstractLargeItemHandler;

import javax.annotation.Nullable;
import java.util.function.IntFunction;

public interface IDomainEntityType {
  Class<? extends IDomainBlockEntity> getTypeClass();
  boolean hasInventory ();

  @Nullable
  IntFunction<? extends AbstractLargeItemHandler> getInventoryBuilder ();
  @Nullable
  Class<? extends AbstractLargeItemHandler> getInventoryClass();
  int getInventorySize ();

  int getOrdinal ();
}
