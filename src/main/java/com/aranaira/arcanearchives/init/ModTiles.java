package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.tileentities.MakeshiftResonatorTile;
import com.aranaira.arcanearchives.tileentities.MandalicKeystoneTile;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ModTiles {
  public static final Map<ResourceLocation, Class<? extends TileEntity>> REGISTRY = new HashMap<>();

  @SubscribeEvent
  public static void onRegister(Register<Block> event) {
    REGISTRY.forEach((key, value) -> GameRegistry.registerTileEntity(value, key));
  }

  static {
    register("crystal_workbench", () -> CrystalWorkbenchTile.class);
    register("makeshift_resonator", () -> MakeshiftResonatorTile.class);
    register("spell_stone", () -> MandalicKeystoneTile.class);
  }

  public static <T extends TileEntity> void register(String registryName, Supplier<Class<? extends T>> supplier) {
    Class<? extends T> tile = supplier.get();
    ResourceLocation rl = new ResourceLocation(ArcaneArchives.MODID, registryName);
    if (REGISTRY.containsKey(rl)) {
      throw new IllegalStateException("Key " + rl.toString() + " already contained in TileEntity registration queue.");
    }
    REGISTRY.put(rl, tile);
  }

  public static void load() {
  }
}
