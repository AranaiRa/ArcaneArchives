package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.blocks.entities.MakeshiftResonatorBlockEntity;
import com.aranaira.arcanearchives.core.blocks.entities.RadiantChestBlockEntity;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.tileentity.TileEntityType;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModblockEntities {
  public static final RegistryEntry<TileEntityType<RadiantChestBlockEntity>> RADIANT_CHEST = REGISTRATE.tileEntity("radiant_chest", RadiantChestBlockEntity::new).validBlocks(ModBlocks.RADIANT_CHEST).register();

  public static final RegistryEntry<TileEntityType<MakeshiftResonatorBlockEntity>> MAKESHIFT_RESONATOR = REGISTRATE.tileEntity("makeshift_resonator", MakeshiftResonatorBlockEntity::new).validBlocks(ModBlocks.MAKESHIFT_RESONATOR).register();

  public static final RegistryEntry<TileEntityType<CrystalWorkbenchBlockEntity>> CRYSTAL_WORKBENCH = REGISTRATE.tileEntity("crystal_workbench", CrystalWorkbenchBlockEntity::new).validBlock(ModBlocks.CRYSTAL_WORKBENCH).register();

  public static void load () {

  }
}
