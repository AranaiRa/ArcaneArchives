package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.types.MachineSound;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface ISoundTile {
  default boolean shouldPlaySound() {
    return hasSound() && isTileValid();
  }

  boolean isTileValid();

  default boolean hasSound() {
    return false;
  }

  default ResourceLocation getSound() {
    return null;
  }

  default float getVolume() {
    return 1f;
  }

  default float getPitch() {
    return 1f;
  }

  @Nullable
  MachineSound setMachineSound(MachineSound sound);

  @Nullable
  MachineSound getMachineSound();

  @SuppressWarnings("ConstantConditions")
  @OnlyIn(Dist.CLIENT)
  default void updateSound(BlockPos pos) {
    /*    if (ConfigHandler.soundConfig.useSounds) {*/
    final ResourceLocation soundRL = getSound();
    MachineSound sound = getMachineSound();
    if (shouldPlaySound() && soundRL != null) {
      if (sound == null) {
        Minecraft.getInstance().getSoundHandler().play(setMachineSound(new MachineSound(soundRL, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, getVolume(), getPitch())));
      }
    } else if (sound != null) {
      sound.endPlaying();
      setMachineSound(null);
    }
  }
}
