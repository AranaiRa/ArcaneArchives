package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class SoundRegistry {
  public static SoundEvent RESONATOR_COMPLETE;
  public static SoundEvent RESONATOR_LOOP;
  public static SoundEvent BRAZIER_ABSORB;

  public static SoundEvent createSoundEvent(ResourceLocation name) {
    SoundEvent result = new SoundEvent(name);
    result.setRegistryName(name);
    return result;
  }

  @SubscribeEvent
  public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
    RESONATOR_COMPLETE = createSoundEvent(new ResourceLocation(ArcaneArchives.MODID, "resonator.complete"));
    RESONATOR_LOOP = createSoundEvent(new ResourceLocation(ArcaneArchives.MODID, "resonator.loop"));
    BRAZIER_ABSORB = createSoundEvent(new ResourceLocation(ArcaneArchives.MODID, "brazier.absorb"));
    event.getRegistry().register(RESONATOR_COMPLETE);
    event.getRegistry().register(RESONATOR_LOOP);
    event.getRegistry().register(BRAZIER_ABSORB);
  }
}
