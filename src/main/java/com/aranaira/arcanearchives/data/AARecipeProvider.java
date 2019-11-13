package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.init.ModBlocks;
import epicsquid.mysticallib.data.DeferredRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class AARecipeProvider extends DeferredRecipeProvider {
  public AARecipeProvider(DataGenerator generatorIn) {
    super(generatorIn, ArcaneArchives.MODID);
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    ShapedRecipeBuilder.shapedRecipe(ModBlocks.RESONATOR.get(), 1)
        .patternLine("GIG")
        .patternLine("WBW")
        .patternLine("W W")
        .key('G', Tags.Items.INGOTS_GOLD)
        .key('I', Items.IRON_BARS)
        .key('W', ItemTags.LOGS)
        .key('B', Items.WATER_BUCKET)
        .addCriterion("has_gold", this.hasItem(Items.GOLD_INGOT))
        .build(consumer);

  }
}
