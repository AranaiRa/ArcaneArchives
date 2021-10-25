package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipeBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
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
            .addProcessor(ModProcessors.CRYSTAL_WORKBENCH_UUID_PROCESSOR.get())
            .addProcessor(ModProcessors.CRYSTAL_WORKBENCH_CONTAINER_PROCESSOR.get())
            .build(p, new ResourceLocation(ArcaneArchives.MODID, "radiant_dust_from_raw_radiant_quartz"));
      })
      .register();

  public static void load() {

  }
}
