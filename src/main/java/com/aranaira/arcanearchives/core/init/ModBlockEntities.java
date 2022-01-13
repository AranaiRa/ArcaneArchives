package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.blocks.entities.*;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModBlockEntities {
  public static final RegistryEntry<TileEntityType<RadiantChestBlockEntity>> RADIANT_CHEST = REGISTRATE.tileEntity("radiant_chest", RadiantChestBlockEntity::new).validBlocks(ModBlocks.RADIANT_CHEST).register();

  public static final RegistryEntry<TileEntityType<MakeshiftResonatorBlockEntity>> MAKESHIFT_RESONATOR = REGISTRATE.tileEntity("makeshift_resonator", MakeshiftResonatorBlockEntity::new).validBlocks(ModBlocks.MAKESHIFT_RESONATOR).register();

  public static final RegistryEntry<TileEntityType<CrystalWorkbenchBlockEntity>> CRYSTAL_WORKBENCH = REGISTRATE.tileEntity("crystal_workbench", CrystalWorkbenchBlockEntity::new).validBlock(ModBlocks.CRYSTAL_WORKBENCH).register();

  public static final RegistryEntry<TileEntityType<RadiantResonatorBlockEntity>> RADIANT_RESONATOR = REGISTRATE.tileEntity("radiant_resonator", RadiantResonatorBlockEntity::new).validBlock(ModBlocks.RADIANT_RESONATOR).register();

  public static final RegistryEntry<TileEntityType<SimpleNetworkIdentifiedBlock>> SIMPLE = REGISTRATE.tileEntity("simple", SimpleNetworkIdentifiedBlock::new).validBlock(ModBlocks.RADIANT_CRYSTAL).register();

  public static void load () {

  }
}
