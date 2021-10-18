package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.container.IPartitionedPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.blockentities.IArcaneArchivesBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.types.IInvWrapper;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class ArcaneCrafting<H extends IArcaneInventory, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IArcaneArchivesBlockEntity> extends IInvWrapper<H> implements ICrafter<H, C, T> {
  private final C container;
  private final T blockentity;

  public ArcaneCrafting(C container, T blockentity, H handler) {
    super(handler);
    this.container = container;
    this.blockentity = blockentity;
  }

  @Override
  public C getContainer() {
    return container;
  }

  @Override
  public T getBlockEntity() {
    return blockentity;
  }

  @Override
  @Nullable
  public UUID getTileId() {
    return getBlockEntity().getEntityId();
  }

  @Override
  public PlayerEntity getPlayer () {
    return container.getPlayer();
  }

  @Override
  public H getHandler() {
    return super.getHandler();
  }
}
