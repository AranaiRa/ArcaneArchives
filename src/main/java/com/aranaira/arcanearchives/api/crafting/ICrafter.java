package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.container.IPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import noobanidus.libs.noobutil.types.IIInvWrapper;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.UUID;

public interface ICrafter<H extends IArcaneInventory, C extends Container & IPlayerContainer, T extends TileEntity & IArcaneArchivesTile> extends IInventory, IIInvWrapper<H> {

  C getContainer();
  T getTile();
  UUID getTileId();
  PlayerEntity getPlayer();
  @Override
  H getHandler();
}
