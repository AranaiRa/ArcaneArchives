package com.aranaira.arcanearchives.core.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.core.recipes.CrystalWorkbenchRecipe;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import static com.aranaira.arcanearchives.ArcaneArchives.REGISTRATE;

public class ModRecipes {
  public static final RegistryEntry<CrystalWorkbenchRecipe.Serializer> CRYSTAL_WORKBENCH = REGISTRATE.object("crystal_workbench").recipeSerializer(CrystalWorkbenchRecipe.Serializer::new).register();

  public static class Types {
    public static IRecipeType<CrystalWorkbenchRecipe> CRYSTAL_WORKBENCH;

    public static void register() {
      CRYSTAL_WORKBENCH = register(new ResourceLocation(ArcaneArchives.MODID, "crystal_workbench"));
    }

    private static <T extends IRecipe<?>> IRecipeType<T> register(final ResourceLocation key) {
      return Registry.register(Registry.RECIPE_TYPE, key, new IRecipeType<T>() {
        public String toString() {
          return key.toString();
        }
      });
    }
  }

  public static void load() {

  }
}
