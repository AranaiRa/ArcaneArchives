package com.aranaira.arcanearchives.init;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRegisterEvent;
import com.aranaira.arcanearchives.api.cwb.ICrystalWorkbenchRegistry;
import com.aranaira.arcanearchives.recipe.cw.CrystalRecipeImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModRecipes {
  public static final List<Builder> REGISTRY = new ArrayList<>();

  public static final Supplier<CrystalWorkbenchRecipe> RADIANT_DUST = registerGeneric("radiant_dust", () -> ingredients((ModItems.RadiantQuartz)), () -> new ItemStack(ModItems.RadiantDust, 2));

  @SubscribeEvent
  public static void onRegister(CrystalWorkbenchRegisterEvent event) {
    ICrystalWorkbenchRegistry registry = event.getRegistry();
    REGISTRY.forEach(o -> registry.register(o.get()));
  }

  public static Supplier<CrystalWorkbenchRecipe> register(String registryName, Supplier<List<IngredientStack>> inputs, Supplier<ItemStack> output) {
    Builder builder = new Builder(inputs, output, new ResourceLocation(ArcaneArchives.MODID, registryName));
    REGISTRY.add(builder);
    return builder;
  }

  public static Supplier<CrystalWorkbenchRecipe> registerGeneric(String registryName, Supplier<List<Object>> inputs, Supplier<ItemStack> output) {
    Builder builder = new BuilderGeneric(inputs, output, new ResourceLocation(ArcaneArchives.MODID, registryName));
    REGISTRY.add(builder);
    return builder;
  }

  public static List<Object> ingredients(Object... ingredients) {
    return Arrays.asList(ingredients);
  }

  public static void load() {
  }

  public static class Builder implements Supplier<CrystalWorkbenchRecipe> {
    protected final Supplier<List<IngredientStack>> inputs;
    protected final Supplier<ItemStack> output;
    protected final ResourceLocation registryName;
    protected CrystalRecipeImpl recipe = null;

    public Builder(Supplier<List<IngredientStack>> inputs, Supplier<ItemStack> output, ResourceLocation registryName) {
      this.inputs = inputs;
      this.output = output;
      this.registryName = registryName;
    }

    @Override
    public CrystalWorkbenchRecipe get() {
      if (recipe == null) {
        recipe = new CrystalRecipeImpl(inputs.get(), output.get());
        recipe.setRegistryName(this.registryName);
      }

      return recipe;
    }
  }

  public static class BuilderGeneric extends Builder {
    private final Supplier<List<Object>> inputs;

    public BuilderGeneric(Supplier<List<Object>> inputs, Supplier<ItemStack> output, ResourceLocation registryName) {
      super(null, output, registryName);
      this.inputs = inputs;
    }

    @Override
    public CrystalWorkbenchRecipe get() {
      if (recipe == null) {
        recipe = new CrystalRecipeImpl(output.get(), inputs.get());
        recipe.setRegistryName(this.registryName);
      }

      return recipe;
    }
  }
}
