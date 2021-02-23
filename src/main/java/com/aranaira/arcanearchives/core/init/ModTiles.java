package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.tiles.MakeshiftResonatorTile;
import com.aranaira.arcanearchives.core.tiles.RadiantChestTile;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModTiles {
  public static final RegistryEntry<TileEntityType<RadiantChestTile>> RADIANT_CHEST = REGISTRATE.tileEntity("radiant_chest_tile", RadiantChestTile::new).validBlocks(ModBlocks.RADIANT_CHEST).register();

  public static final RegistryEntry<TileEntityType<MakeshiftResonatorTile>> MAKESHIFT_RESONATOR = REGISTRATE.tileEntity("makeshift_resonator_tile", MakeshiftResonatorTile::new).validBlocks(ModBlocks.MAKESHIFT_RESONATOR).register();

  public static void load () {

  }
}
