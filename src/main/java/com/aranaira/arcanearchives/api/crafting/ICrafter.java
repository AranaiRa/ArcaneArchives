package com.aranaira.arcanearchives.api.crafting;

import com.aranaira.arcanearchives.api.container.IPartitionedPlayerContainer;
import com.aranaira.arcanearchives.api.inventory.IArcaneInventory;
import com.aranaira.arcanearchives.api.blockentities.IArcaneArchivesBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noobanidus.libs.noobutil.types.IIInvWrapper;

import java.util.List;
import java.util.UUID;

public interface ICrafter<H extends IArcaneInventory, C extends Container & IPartitionedPlayerContainer, T extends TileEntity & IArcaneArchivesBlockEntity> extends IInventory, IIInvWrapper<H> {

  C getContainer();
  T getBlockEntity();
  UUID getTileId();
  PlayerEntity getPlayer();
  @Override
  H getHandler();
  List<Slot> getCombinedIngredientSlots();
}
