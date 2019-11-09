package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.*;
import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.itemblocks.MonitoringCrystalItem;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTankItem;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTroveItem;
import com.aranaira.arcanearchives.items.itemblocks.StorageShapedQuartzItem;
import com.aranaira.arcanearchives.items.templates.ItemBlockTemplate;
import com.aranaira.arcanearchives.tileentities.BrazierTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantCraftingTableTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantResonatorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class BlockRegistry {

  //Blocks
  public static final StorageRawQuartz STORAGE_RAW_QUARTZ = new StorageRawQuartz();
  public static final StorageShapedQuartz STORAGE_SHAPED_QUARTZ = new StorageShapedQuartz();
  public static final RadiantChest RADIANT_CHEST = new RadiantChest();
  public static final RadiantCraftingTable RADIANT_CRAFTING_TABLE = new RadiantCraftingTable();
  public static final RadiantLantern RADIANT_LANTERN = new RadiantLantern();
  public static final RadiantResonator RADIANT_RESONATOR = new RadiantResonator();
  public static final RawQuartzCluster RAW_QUARTZ = new RawQuartzCluster();
  public static final QuartzSliver QUARTZ_SLIVER = new QuartzSliver();
  public static final MonitoringCrystal MONITORING_CRYSTAL = new MonitoringCrystal();
  public static final Brazier BRAZIER_OF_HOARDING = new Brazier();
  public static final BrazierFire BRAZIER_FIRE = new BrazierFire();

  public static void init() {
  }
}
