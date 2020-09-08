package com.aranaira.arcanearchives.util.ticker;

import com.aranaira.arcanearchives.tileentities.NetworkedBaseTile;
import com.aranaira.arcanearchives.tilenetwork.Network;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class TileTicker implements ITicker<MinecraftServer, NetworkedBaseTile> {
  protected NetworkedBaseTile tile;
  protected int tick = 20;

  public TileTicker(NetworkedBaseTile tile) {
    this.tile = tile;
  }

  public TileTicker (NetworkedBaseTile tile, boolean outgoing) {
    this.tile = tile;
    this.tick = 0;
  }

  @Override
  public boolean expired() {
    return tick < -20;
  }

  @Override
  public boolean decaying () {
    return tick < 0;
  }

  @Override
  public boolean invalid(MinecraftServer type) {
    return false;
  }

  @Override
  public boolean run(MinecraftServer type) {
    if (tile.generatesNetworkId()) {
      tile.generateNetworkId();
    }

    Network network = tile.getNetwork();
    //noinspection ConstantConditions
    if (network == null || tile.getWorld() == null) {
      tick--;
    } else {
      network.add(tile);
      tile.onNetworkJoined(network);
      return true;
    }
    return false;
  }

  @Override
  public boolean decay (MinecraftServer type) {
    Network network = tile.getNetwork();
    if (network != null) {
      network.remove(tile);
    } else {
      tick--;
    }

    if (tick <= -20 || network != null) {
      World world = tile.getWorld();
      if (!world.isRemote) {
        world.destroyBlock(tile.getPos(), true);
      }
      return true;
    }

    return false;
  }

  @Override
  public NetworkedBaseTile getInternal() {
    return tile;
  }
}
