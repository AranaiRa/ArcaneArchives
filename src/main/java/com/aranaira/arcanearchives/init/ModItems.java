package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.aranaira.arcanearchives.item.ManifestItem;
import com.aranaira.arcanearchives.recipe.CrystalWorkbenchRecipeBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModItems {
  public static final ItemEntry<Item> RADIANT_QUARTZ = REGISTRATE.item("radiant_quartz", Item::new)
      .properties(o -> o)
      .register();

  public static final ItemEntry<Item> RADIANT_DUST = REGISTRATE.item("radiant_dust", Item::new)
      .properties(o -> o)
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 2)
            .addIngredient(RADIANT_QUARTZ.get(), 1)
            .addProcessor(ModProcessors.CRYSTAL_WORKBENCH_DOMAIN_PROCESSOR.get())
            .addProcessor(ModProcessors.CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR.get())
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "radiant_dust_from_raw_radiant_quartz"));
      })
      .register();

  public static final ItemEntry<ManifestItem> MANIFEST = REGISTRATE.item("manifest", ManifestItem::new)
      .properties(o -> o.stacksTo(1))
      .recipe((ctx, p) -> {
        CrystalWorkbenchRecipeBuilder.crystalWorkbenchRecipe(ctx.getEntry(), 1)
            .addIngredient(Items.PAPER, 1)
            .addIngredient(ModItems.RADIANT_DUST.get(), 1)
            .addProcessor(ModProcessors.CRYSTAL_WORKBENCH_DOMAIN_PROCESSOR.get())
            .build(p, new ResourceLocation(ArcaneArchivesAPI.MODID, "manifest"));
      })
      .register();

  public static void load() {

  }
}
