package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

public class PlayerSaveData extends WorldSavedData {
  public static final String PREFIX = "ArcaneArchives-PlayerSavedData-";

  private String id = null;

  public boolean receivedBook = false;

  public static String ID(PlayerEntity player) {
    return PREFIX + player.getCachedUniqueIdString();
  }

  public String getId() {
    return id;
  }

  public PlayerSaveData(String string) {
    super(string);
    this.id = string;
  }

  public PlayerSaveData(PlayerEntity player) {
    super(ID(player));
    this.id = ID(player);
  }

  public PlayerSaveData() {
    super(PREFIX + "INVALID");
    ArcaneArchives.logger.error("Created PlayerSaveData for an invalid entity.");
  }

  @Override
  public void readFromNBT(CompoundNBT nbt) {
    this.receivedBook = nbt.getBoolean(Tags.PlayerSaveData.receivedBook);
  }

  @Override
  public CompoundNBT writeToNBT(CompoundNBT compound) {
    compound.setBoolean(Tags.PlayerSaveData.receivedBook, receivedBook);
    return compound;
  }
}
