package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.core.blocks.CrystalWorkbenchBlock;
import com.aranaira.arcanearchives.core.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.core.blocks.RadiantChestBlock;
import com.aranaira.arcanearchives.core.blocks.RadiantResonatorBlock;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipeBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModBlocks {
  public static final RegistryEntry<CrystalWorkbenchBlock> CRYSTAL_WORKBENCH = REGISTRATE.block("crystal_workbench", Material.GLASS, CrystalWorkbenchBlock::new)
      .properties(o -> o.noOcclusion())
      .blockstate(NonNullBiConsumer.noop())
      .recipe((ctx, p) ->
          ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
              .pattern("DGP")
              .pattern("WCW")
              .pattern("QWQ")
              // TODO: Custom tag
              .define('D', Tags.Items.STONE)
              .define('G', Tags.Items.GLASS_PANES)
              // TODO: Custom tag
              .define('P', Items.PAPER)
              // TODO: Custom tag?
              .define('C', Blocks.CRAFTING_TABLE)
              // TODO: Custom tag
              .define('Q', ModItems.RADIANT_DUST.get())
              // TODO: Magical wood?
              .define('W', ItemTags.LOGS)
              .unlockedBy("has_radiant_dust", RegistrateRecipeProvider.hasItem(ModItems.RADIANT_DUST.get()))
              .save(p)
      )
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .loot((ctx, p) -> {
        ctx.add(p, LootTable.lootTable().withPool(RegistrateBlockLootTables.withExplosionDecay(p, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy(Identifiers.tileId, "BlockEntityTag." + Identifiers.tileId))))));
      })
      .register();

  public static final RegistryEntry<RadiantChestBlock> RADIANT_CHEST = REGISTRATE.block("radiant_chest", Material.WOOD, RadiantChestBlock::new)
      .properties(AbstractBlock.Properties::noOcclusion)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 1)
            .build(p, new ResourceLocation(ArcaneArchives.MODID, "radiant_chest"));
      })
      .loot((ctx, p) -> {
        ctx.add(p, LootTable.lootTable().withPool(RegistrateBlockLootTables.withExplosionDecay(p, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy(Identifiers.tileId, "BlockEntityTag." + Identifiers.tileId))))));
      })
      .register();

  public static final RegistryEntry<MakeshiftResonatorBlock> MAKESHIFT_RESONATOR = REGISTRATE.block("makeshift_resonator", Material.WOOD, MakeshiftResonatorBlock::new)
      .properties(AbstractBlock.Properties::noOcclusion/*.setOpaque((x, y, z) -> false)*/)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
          .pattern("SGS")
          .pattern("KTK")
          .pattern("K K")
          .define('S', Items.STONE_SLAB)
          .define('G', Tags.Items.NUGGETS_GOLD)
          .define('K', Tags.Items.RODS_WOODEN)
          .define('T', Tags.Items.STRING)
          .unlockedBy("has_gold_nugget", RegistrateRecipeProvider.hasItem(Tags.Items.NUGGETS_GOLD)).save(p, new ResourceLocation(ArcaneArchives.MODID, "makeshift_resonator")))
      .register();

  public static final RegistryEntry<RadiantResonatorBlock> RADIANT_RESONATOR = REGISTRATE.block("radiant_resonator", Material.WOOD, RadiantResonatorBlock::new)
      .properties(AbstractBlock.Properties::noOcclusion)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 1)
            .build(p, new ResourceLocation(ArcaneArchives.MODID, "radiant_resonator"));
      })
      .loot((ctx, p) -> {
        ctx.add(p, LootTable.lootTable().withPool(RegistrateBlockLootTables.withExplosionDecay(p, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy(Identifiers.tileId, "BlockEntityTag." + Identifiers.tileId))))));
      })
      .register();

  public static void load() {

  }
}
