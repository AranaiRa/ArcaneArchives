package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.AATags;
import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.aranaira.arcanearchives.block.*;
import com.aranaira.arcanearchives.item.block.AdjustableAbstractNetworkedBlockItem;
import com.aranaira.arcanearchives.item.block.CrystalWorkbenchBlockItem;
import com.aranaira.arcanearchives.recipe.CrystalWorkbenchRecipeBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import noobanidus.libs.noobutil.loot.condition.LootConditions;

import java.util.function.ToIntFunction;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModBlocks {
  private static final ToIntFunction<BlockState> lightFunction = value -> 15;

  public static final RegistryEntry<CrystalWorkbenchBlock> CRYSTAL_WORKBENCH = REGISTRATE.block("crystal_workbench", Material.GLASS, CrystalWorkbenchBlock::new)
      .properties(o -> o.noOcclusion().lightLevel(lightFunction).strength(3f).harvestTool(ToolType.PICKAXE))
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
      .item(CrystalWorkbenchBlockItem::new)
      .tag(AATags.Items.CRYSTAL_WORKBENCH)
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .loot(withTileAndNetworkId())
      .tag(AATags.Blocks.CRYSTAL_WORKBENCH)
      .register();

  public static final RegistryEntry<RadiantChestBlock> RADIANT_CHEST = REGISTRATE.block("radiant_chest", Material.WOOD, RadiantChestBlock::new)
      .properties(o -> o.noOcclusion().lightLevel(lightFunction).strength(3f).harvestTool(ToolType.AXE))
      .blockstate(NonNullBiConsumer.noop())
      .item(AdjustableAbstractNetworkedBlockItem::new)
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 1)
            .addIngredient(ModItems.RADIANT_DUST.get(), 1)
            .addIngredient(Tags.Items.CHESTS_WOODEN, 1)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "radiant_chest"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Blocks.MAGMA_BLOCK, 2)
            .addIngredient(Items.MAGMA_CREAM, 4)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "magma_block"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Blocks.OBSIDIAN, 4)
            .addIngredient(Items.LAVA_BUCKET)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "obsidian"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Blocks.CRAFTING_TABLE, 4)
            .addIngredient(Blocks.CRAFTING_TABLE)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "crafting_table"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Blocks.ANVIL, 1)
            .addIngredient(Blocks.IRON_BLOCK, 3)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "anvil"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Blocks.ZOMBIE_HEAD, 2)
            .addIngredient(Blocks.SKELETON_SKULL)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "zombie_head"));
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(Items.IRON_INGOT, 4)
            .addIngredient(Tags.Items.ORES_IRON)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "iron_ingot"));
      })
      .loot(withTileAndNetworkId())
      .register();

  public static final RegistryEntry<MakeshiftResonatorBlock> MAKESHIFT_RESONATOR = REGISTRATE.block("makeshift_resonator", Material.WOOD, MakeshiftResonatorBlock::new)
      .properties(o -> o.noOcclusion().lightLevel(lightFunction).strength(3f).harvestTool(ToolType.PICKAXE))
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
          .unlockedBy("has_gold_nugget", RegistrateRecipeProvider.hasItem(Tags.Items.NUGGETS_GOLD)).save(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "makeshift_resonator")))
      .register();

  public static final RegistryEntry<RadiantResonatorBlock> RADIANT_RESONATOR = REGISTRATE.block("radiant_resonator", Material.WOOD, RadiantResonatorBlock::new)
      .properties(o -> o.noOcclusion().lightLevel(lightFunction).strength(3f).harvestTool(ToolType.PICKAXE))
      .blockstate(NonNullBiConsumer.noop())
      .item(AdjustableAbstractNetworkedBlockItem::new)
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 1)
            .addIngredient(Items.GOLD_INGOT, 4)
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "radiant_resonator"));
      })
      .loot(withTileAndNetworkId())
      .register();

  public static final RegistryEntry<RadiantCrystalBlock> RADIANT_CRYSTAL = REGISTRATE.block("radiant_crystal", Material.GLASS, RadiantCrystalBlock::new)
      .properties(o -> o.noOcclusion().lightLevel(lightFunction).strength(3f).harvestTool(ToolType.PICKAXE))
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .loot((ctx, p) -> {
        ctx.add(p, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).when(LootConditions.HAS_SILK_TOUCH).otherwise(ItemLootEntry.lootTableItem(ModItems.RADIANT_QUARTZ.get()).apply(SetCount.setCount(ConstantRange.exactly(1)))))));
      })
      .register();

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> withTileId() {
    return (ctx, p) -> {
      ctx.add(p, LootTable.lootTable().withPool(RegistrateBlockLootTables.withExplosionDecay(p, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy(Identifiers.tileId, Identifiers.blockEntityTag(Identifiers.tileId)))))));
    };
  }

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> withNetworkId() {
    return (ctx, p) -> {
      ctx.add(p, LootTable.lootTable().withPool(RegistrateBlockLootTables.withExplosionDecay(p, LootPool.lootPool().setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(p).apply(CopyName.copyName(CopyName.Source.BLOCK_ENTITY)).apply(CopyNbt.copyData(CopyNbt.Source.BLOCK_ENTITY).copy(Identifiers.networkId, Identifiers.blockEntityTag(Identifiers.networkId)))))));
    };
  }

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> withTileAndNetworkId() {
    return (ctx, p) -> {
      ctx.add(p,
          LootTable
              .lootTable()
              .withPool(
                  RegistrateBlockLootTables
                      .withExplosionDecay(p,
                          LootPool
                              .lootPool()
                              .setRolls(
                                  ConstantRange
                                      .exactly(1)
                              )
                              .add(
                                  ItemLootEntry
                                      .lootTableItem(p)
                                      .apply(
                                          CopyName
                                              .copyName(
                                                  CopyName.Source.BLOCK_ENTITY
                                              )
                                      )
                                      .apply(
                                          CopyNbt
                                              .copyData(
                                                  CopyNbt.Source.BLOCK_ENTITY
                                              )
                                              .copy(
                                                  Identifiers.networkId,
                                                  Identifiers.blockEntityTag(Identifiers.networkId))
                                      )
                                      .apply(
                                          CopyNbt
                                              .copyData(
                                                  CopyNbt.Source.BLOCK_ENTITY
                                              )
                                              .copy(
                                                  Identifiers.tileId,
                                                  Identifiers.blockEntityTag(Identifiers.tileId))
                                      )
                              )
                      )
              )
      );
    };
  }

  public static void load() {

  }
}
