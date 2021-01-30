package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.IArcaneArchivesTile;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.types.IInvWrapper;

public class WorkbenchCrafting<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends IInvWrapper<H> {
  private final C container;
  private final T tile;

  public WorkbenchCrafting(C container, T tile, H handler) {
    super(handler);
    this.container = container;
    this.tile = tile;
  }

  public C getContainer() {
    return container;
  }

  public T getTile() {
    return tile;
  }

  public PlayerEntity getPlayer () {
    return container.getPlayer();
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
