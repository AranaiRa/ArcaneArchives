package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.blocks.RadiantChestBlock;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModBlocks {
  public static final RegistryEntry<RadiantChestBlock> RADIANT_CHEST = REGISTRATE.block("radiant_chest", RadiantChestBlock::new)
      .properties(o -> o)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .register();

  public static final RegistryEntry<MakeshiftResonatorBlock> MAKESHIFT_RESONATOR = REGISTRATE.block("makeshift_resonator", MakeshiftResonatorBlock::new)
      .properties(o -> o/*.setOpaque((x, y, z) -> false)*/)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .register();

  public static void load() {

  }
}
