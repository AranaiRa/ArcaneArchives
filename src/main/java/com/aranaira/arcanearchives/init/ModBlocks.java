package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.CrystalWorkbenchBlock;
import com.aranaira.arcanearchives.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.blocks.MandalicKeystoneBlock;
import com.aranaira.arcanearchives.blocks.templates.OmniTemplateBlock;
import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ModBlocks {
  public static final List<Block> REGISTRY = new ArrayList<>();

  @SubscribeEvent
  public static void onRegister(Register<Block> event) {
    REGISTRY.forEach(event.getRegistry()::register);
  }

  public static CrystalWorkbenchBlock CrystalWorkbench = register("crystal_workbench", CrystalWorkbenchBlock::new, Material.IRON, (o) -> o.setHardness(3f).setLightLevel(16f / 16f).setHarvestTool("axe", 0).setTooltip("arcanearchives.tooltip.device.gemcutters_table", TextFormatting.GOLD));

  public static MakeshiftResonatorBlock MakeshiftResonator = register("makeshift_resonator", MakeshiftResonatorBlock::new, Material.IRON, (o) -> o.setHardness(3f).setLightLevel(6f / 16f).setHarvestTool("pickaxe", 0).setTooltip("arcanearchives.tooltip.device.wonky_resonator", TextFormatting.GOLD).setDefault(o.getDefaultState().withProperty(MakeshiftResonatorBlock.FILLED, false)));

  public static MandalicKeystoneBlock MandalicKeystone = register("mandalic_keystone", MandalicKeystoneBlock::new, (o) -> o.setHardness(1.7f).setHarvestTool("pickaxe", 0).setTooltip("arcanearchives.tooltip.item.mandalic_keystone", TextFormatting.GOLD));

  public static TemplateBlock StalwartStone = register("stalwart_stone", TemplateBlock::new, (o) -> o.setHardness(1.9f).setHarvestTool("pickaxe", 0).setResistance(24.0f).setTooltip("arcanearchives.tooltip.item.stalwart_stone", TextFormatting.GOLD));

  public static TemplateBlock StalwartWood = register("stalwart_wood", TemplateBlock::new, Material.WOOD, (o) -> o.setHardness(1.7f).setResistance(21.0f).setHarvestTool("axe", 0).setTooltip("arcanearchives.tooltip.item.stalwart_wood", TextFormatting.GOLD));

  public static TemplateBlock QuartzBlock = register("quartz_block", TemplateBlock::new, Material.ROCK, (o) -> o.setHardness(1.7f).setHarvestTool("pickaxe", 0).setLightLevel(1).setTooltip("arcanearchives.tooltip.item.storage_raw_quartz", TextFormatting.GOLD));

  // Sorta temporary
  public static OmniTemplateBlock QuartzCluster = register("quartz_cluster", OmniTemplateBlock::new, Material.ROCK, (o) -> o.setDefaultFacing(EnumFacing.UP).setHardness(1.4f).setTooltip("arcanearchives.tooltip.item.raw_quartz", TextFormatting.GOLD).setHarvestTool("pickaxe", 0).setLightLevel(1).setFullCube(false).setOpaqueCube(false));

  public static <T extends Block> T register(String registryName, Supplier<T> supplier) {
    return register(registryName, o -> supplier.get(), Material.ROCK, null, null);
  }

  public static <T extends Block> T register(String registryName, Supplier<T> supplier, Consumer<T> consumer) {
    return register(registryName, o -> supplier.get(), Material.ROCK, consumer, null);
  }

  public static <T extends Block> T register(String registryName, Supplier<T> supplier, Supplier<? extends ItemBlock> itemBlock) {
    return register(registryName, o -> supplier.get(), Material.ROCK, null, itemBlock);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Material mat) {
    return register(registryName, supplier, mat, null, null);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Material mat, Consumer<T> consumer) {
    return register(registryName, supplier, mat, consumer, null);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Material mat, Supplier<? extends ItemBlock> itemBlock) {
    return register(registryName, supplier, mat, null, itemBlock);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier) {
    return register(registryName, supplier, Material.ROCK, null, null);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Consumer<T> consumer) {
    return register(registryName, supplier, Material.ROCK, consumer, null);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Supplier<? extends ItemBlock> itemBlock) {
    return register(registryName, supplier, Material.ROCK, null, itemBlock);
  }

  public static <T extends Block> T register(String registryName, Function<Material, T> supplier, Material material, @Nullable Consumer<T> consumer, @Nullable Supplier<? extends ItemBlock> itemBlock) {
    T block = supplier.apply(material);
    block.setTranslationKey(registryName);
    block.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
    block.setCreativeTab(ArcaneArchives.TAB);
    ItemBlock item;
    if (itemBlock != null) {
      item = itemBlock.get();
    } else {
      item = new ItemBlock(block);
    }
    if (item.getBlock() != Blocks.AIR) {
      item.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
      if (block instanceof TemplateBlock) {
        ((TemplateBlock) block).setItemBlock(item);
      }
      ModItems.add(item);
    }
    if (consumer != null) {
      consumer.accept(block);
    }
    REGISTRY.add(block);
    return block;
  }

  public static void load() {

  }

  public static class Boxes {

  }
}
