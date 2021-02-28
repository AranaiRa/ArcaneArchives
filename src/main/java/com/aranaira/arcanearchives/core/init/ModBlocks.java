package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.core.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.core.blocks.RadiantChestBlock;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModBlocks {
  public static final RegistryEntry<RadiantChestBlock> RADIANT_CHEST = REGISTRATE.block("radiant_chest", RadiantChestBlock::new)
      .properties(o -> o.notSolid())
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .register();

  public static final RegistryEntry<MakeshiftResonatorBlock> MAKESHIFT_RESONATOR = REGISTRATE.block("makeshift_resonator", Material.WOOD, MakeshiftResonatorBlock::new)
      .properties(o -> o.notSolid()/*.setOpaque((x, y, z) -> false)*/)
      .blockstate(NonNullBiConsumer.noop())
      .item()
      .model((ctx, p) -> p.blockItem(ctx::getEntry))
      .build()
      .recipe((ctx, p) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry(), 1)
          .patternLine("SGS")
            .patternLine("KTK")
          .patternLine("K K")
          .key('S', Items.STONE_SLAB)
          .key('G', Tags.Items.NUGGETS_GOLD)
          .key('K', Tags.Items.RODS_WOODEN)
          .key('T', Tags.Items.STRING)
          .addCriterion("has_gold_nugget", RegistrateRecipeProvider.hasItem(Tags.Items.NUGGETS_GOLD)).build(p, new ResourceLocation(ArcaneArchives.MODID, "makeshift_resonator")))
      .register();

  public static void load() {

  }
}
