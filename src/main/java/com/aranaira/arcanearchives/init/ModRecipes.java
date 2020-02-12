package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.api.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModRecipes {
  public static final List<CrystalWorkbenchRecipe> REGISTRY = new ArrayList<>();

  @SubscribeEvent
  public static void onRegister(RegistryEvent.Register<CrystalWorkbenchRecipe> event) {
    REGISTRY.forEach(event.getRegistry()::register);
  }

  public static <T extends CrystalWorkbenchRecipe> T register(String registryName, Builder<T> supplier, Supplier<List<IngredientStack>> inputs, Supplier<ItemStack> output) {
    T recipe = supplier.get();
    recipe.setRegistryName(new ResourceLocation(ArcaneArchives.MODID, registryName));
    REGISTRY.add(recipe);
    return recipe;
  }

  public static void load() {
  }

  public static class Builder<T extends CrystalWorkbenchRecipe> implements Supplier<T> {
    private List<IngredientStack> inputs;
    private ItemStack output;

    public Builder(List<IngredientStack> inputs, ItemStack output) {
      this.inputs = inputs;
      this.output = output;
    }

    @Override
    public T get() {
      return null;
    }
  }
}
