package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.tiles.CrystalWorkbenchTile;
import com.aranaira.arcanearchives.core.tiles.MakeshiftResonatorTile;
import com.aranaira.arcanearchives.core.tiles.RadiantChestTile;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModTiles {
  public static final RegistryEntry<TileEntityType<RadiantChestTile>> RADIANT_CHEST = REGISTRATE.tileEntity("radiant_chest", RadiantChestTile::new).validBlocks(ModBlocks.RADIANT_CHEST).register();

  public static final RegistryEntry<TileEntityType<MakeshiftResonatorTile>> MAKESHIFT_RESONATOR = REGISTRATE.tileEntity("makeshift_resonator", MakeshiftResonatorTile::new).validBlocks(ModBlocks.MAKESHIFT_RESONATOR).register();

  public static final RegistryEntry<TileEntityType<CrystalWorkbenchTile>> CRYSTAL_WORKBENCH = REGISTRATE.tileEntity("crystal_workbench", CrystalWorkbenchTile::new).validBlock(ModBlocks.CRYSTAL_WORKBENCH).register();

  public static void load () {

  }
}
