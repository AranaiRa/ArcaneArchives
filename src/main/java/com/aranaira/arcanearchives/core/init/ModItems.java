package com.aranaira.arcanearchives.core.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModItems {
  public static final ItemEntry<Item> RADIANT_DUST = REGISTRATE.item("radiant_dust", Item::new)
      .properties(o -> o)
      .register();

  public static void load() {

  }
}
