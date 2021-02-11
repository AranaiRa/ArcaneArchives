package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.tiles.RadiantChestTile;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModTiles {
  public static final RegistryEntry<TileEntityType<RadiantChestTile>> RADIANT_CHEST = REGISTRATE.tileEntity("radiant_chest_tile", RadiantChestTile::new).register();

  public static void load () {

  }
}
