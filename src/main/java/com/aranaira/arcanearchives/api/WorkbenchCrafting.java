package com.aranaira.arcanearchives.api;

import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WorkbenchCrafting {
  private final Container container;
  private final TileEntity tileEntity;
  private final IItemHandlerModifiable inventory;

  public WorkbenchCrafting(Container container, TileEntity tileEntity, IItemHandlerModifiable inventory) {
    this.container = container;
    this.tileEntity = tileEntity;
    this.inventory = inventory;
  }

  public Container getContainer() {
    return container;
  }

  public TileEntity getTileEntity() {
    return tileEntity;
  }

  public IItemHandlerModifiable getInventory() {
    return inventory;
  }
}
