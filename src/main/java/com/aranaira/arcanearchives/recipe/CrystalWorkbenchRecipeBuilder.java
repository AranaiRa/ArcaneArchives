package com.aranaira.arcanearchives.recipe;

import com.aranaira.arcanearchives.init.ModRecipes;
import com.aranaira.arcanearchives.recipe.inventory.CrystalWorkbenchCrafting;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class CrystalWorkbenchRecipeBuilder {
  private final Item result;
  private final int count;
  private final List<IngredientStack> ingredients = new ArrayList<>();
  private final List<Processor<CrystalWorkbenchCrafting>> processors = new ArrayList<>();

  protected CrystalWorkbenchRecipeBuilder(IItemProvider result, int count) {
    this.result = result.asItem();
    this.count = count;
  }

  public static CrystalWorkbenchRecipeBuilder crystalWorkbenchRecipe(IItemProvider result, int count) {
    return new CrystalWorkbenchRecipeBuilder(result, count);
  }

  public static CrystalWorkbenchRecipeBuilder crystalWorkbenchRecipe(IItemProvider result) {
    return new CrystalWorkbenchRecipeBuilder(result, 1);
  }

  public CrystalWorkbenchRecipeBuilder addIngredient (ITag<Item> ingredient) {
    addIngredient(ingredient, 1);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient (ITag<Item> ingredient, int count) {
    addIngredient(Ingredient.of(ingredient), count);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(Ingredient ingredient) {
    addIngredient(ingredient, 1);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(Ingredient ingredient, int count) {
    addIngredient(new IngredientStack(ingredient, count));
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient (IItemProvider item) {
    addIngredient(item, 1);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient (IItemProvider item, int count) {
    addIngredient(new IngredientStack(Ingredient.of(item), count));
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(IngredientStack ingredientStack) {
    this.ingredients.add(ingredientStack);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addProcessor (Processor<CrystalWorkbenchCrafting> processor) {
    this.processors.add(processor);
    return this;
  }

  public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation recipeName) {
    consumer.accept(new Result(recipeName, result, count, ingredients, processors));
  }

  public static class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final List<IngredientStack> ingredients;
    private final List<Processor<CrystalWorkbenchCrafting>> processors;

    public Result(ResourceLocation id, Item result, int count, List<IngredientStack> ingredients, List<Processor<CrystalWorkbenchCrafting>> processors) {
      this.id = id;
      this.result = result;
      this.count = count;
      this.ingredients = ingredients;
      this.processors = processors;
      if (this.ingredients.isEmpty()) {
        throw new IllegalArgumentException("ingredients for recipe " + id + " cannot be empty");
      }
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
      JsonArray array = new JsonArray();

      for (IngredientStack ingredient : this.ingredients) {
        array.add(ingredient.serialize());
      }

      json.add("ingredients", array);
      JsonObject item = new JsonObject();
      item.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
      if (count > 1) {
        item.addProperty("count", count);
      }
      json.add("result", item);

      JsonArray processors = new JsonArray();
      for (Processor<CrystalWorkbenchCrafting> processor : this.processors) {
        processors.add(Objects.requireNonNull(processor.getRegistryName()).toString());
      }
      json.add("processors", processors);
    }

    @Override
    public ResourceLocation getId() {
      return id;
    }

    @Override
    public IRecipeSerializer<?> getType() {
      return ModRecipes.Serializers.CRYSTAL_WORKBENCH.get();
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return null;
    }
  }
}
