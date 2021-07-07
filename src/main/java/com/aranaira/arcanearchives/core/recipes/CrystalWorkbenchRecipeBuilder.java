package com.aranaira.arcanearchives.core.recipes;

import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CrystalWorkbenchRecipeBuilder {
  private final Item result;
  private final int count;
  private final List<IngredientStack> ingredients = new ArrayList<>();

  protected CrystalWorkbenchRecipeBuilder(IItemProvider result, int count) {
    this.result = result.asItem();
    this.count = count;
  }

  public static CrystalWorkbenchRecipeBuilder builder(IItemProvider result, int count) {
    return new CrystalWorkbenchRecipeBuilder(result, count);
  }

  public static CrystalWorkbenchRecipeBuilder builder(IItemProvider result) {
    return new CrystalWorkbenchRecipeBuilder(result, 1);
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(Ingredient ingredient) {
    addIngredient(ingredient, 1);
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(Ingredient ingredient, int count) {
    addIngredient(new IngredientStack(ingredient, count));
    return this;
  }

  public CrystalWorkbenchRecipeBuilder addIngredient(IngredientStack ingredientStack) {
    this.ingredients.add(ingredientStack);
    return this;
  }

  public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation recipeName) {
    consumer.accept(new Result(recipeName, result, count, ingredients));
  }

  public static class Result implements IFinishedRecipe {
    private final ResourceLocation id;
    private final Item result;
    private final int count;
    private final List<IngredientStack> ingredients;

    public Result(ResourceLocation id, Item result, int count, List<IngredientStack> ingredients) {
      this.id = id;
      this.result = result;
      this.count = count;
      this.ingredients = ingredients;
    }

    @Override
    public void serialize(JsonObject json) {
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
    }

    @Override
    public ResourceLocation getID() {
      return id;
    }

    // TODO
    @Override
    public IRecipeSerializer<?> getSerializer() {
      return null;
    }

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
      return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementID() {
      return null;
    }
  }
}
