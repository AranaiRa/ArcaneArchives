package com.aranaira.arcanearchives.types;

import net.minecraft.client.audio.LocatableSound;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class MachineSound extends LocatableSound implements ITickableSound {

  private boolean donePlaying;

  public MachineSound(@Nonnull ResourceLocation sound, float x, float y, float z, float volume, float pitch) {
    super(sound, SoundCategory.BLOCKS);
    this.xPosF = x;
    this.yPosF = y;
    this.zPosF = z;
    this.volume = volume;
    this.pitch = pitch;
    this.repeat = true;
  }

  @Override
  public void update() {
  }

  @Override
  public boolean isDonePlaying() {
    return donePlaying;
  }

  public void endPlaying() {
    donePlaying = true;
  }

  public void startPlaying() {
    donePlaying = false;
  }

  public MachineSound setVolume(float vol) {
    this.volume = vol;
    return this;
  }

  public MachineSound setPitch(float pitch) {
    this.pitch = pitch;
    return this;
  }
}
