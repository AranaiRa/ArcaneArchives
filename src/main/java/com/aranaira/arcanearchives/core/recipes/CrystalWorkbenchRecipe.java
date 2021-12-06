package com.aranaira.arcanearchives.core.recipes;

import com.aranaira.arcanearchives.api.crafting.ingredients.CountableIngredientStack;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientInfo;
import com.aranaira.arcanearchives.api.crafting.ingredients.IngredientStack;
import com.aranaira.arcanearchives.api.crafting.processors.IProcessor;
import com.aranaira.arcanearchives.api.crafting.processors.Processor;
import com.aranaira.arcanearchives.api.crafting.recipes.ICrystalWorkbenchRecipe;
import com.aranaira.arcanearchives.core.init.ModRecipes;
import com.aranaira.arcanearchives.core.init.ModRegistries;
import com.aranaira.arcanearchives.core.inventory.container.CrystalWorkbenchContainer;
import com.aranaira.arcanearchives.core.inventory.handlers.CrystalWorkbenchInventory;
import com.aranaira.arcanearchives.core.recipes.inventory.CrystalWorkbenchCrafting;
import com.aranaira.arcanearchives.core.blocks.entities.CrystalWorkbenchBlockEntity;
import com.aranaira.arcanearchives.core.util.ArcaneRecipeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CrystalWorkbenchRecipe implements ICrystalWorkbenchRecipe<CrystalWorkbenchInventory, CrystalWorkbenchContainer, CrystalWorkbenchBlockEntity, CrystalWorkbenchCrafting> {
  private final List<Processor<CrystalWorkbenchCrafting>> processors = new ArrayList<>();
  private final NonNullList<IngredientStack> ingredientStacks;
  private final ItemStack result;
  private final ResourceLocation recipeId;

  public CrystalWorkbenchRecipe(NonNullList<IngredientStack> ingredientStacks, ItemStack result, ResourceLocation recipeId) {
    this.ingredientStacks = ingredientStacks;
    this.result = result;
    this.recipeId = recipeId;
  }

  @Override
  public List<Processor<CrystalWorkbenchCrafting>> getProcessors() {
    return processors;
  }

  @Override
  public void addProcessor(Processor<CrystalWorkbenchCrafting> processor) {
    processors.add(processor);
  }

  @Override
  public NonNullList<IngredientStack> getIngredientStacks() {
    return ingredientStacks;
  }

  @Override
  public List<IngredientInfo> getIngredientInfo(CrystalWorkbenchCrafting container) {
    return container.getIngredientInfo(this);
  }

  @Override
  public boolean matches(CrystalWorkbenchCrafting inv, @Nullable World worldIn) {
    if (this.ingredientStacks.isEmpty()) {
      return false;
    }

    List<CountableIngredientStack> ingredients = this.ingredientStacks.stream().map(CountableIngredientStack::new).collect(Collectors.toList());

    outer: for (Slot slot : inv.getCombinedIngredientSlots()) {
      if (!slot.hasItem()) {
        continue;
      }

      ItemStack inSlot = slot.getItem();

      for (CountableIngredientStack ingredient : ingredients) {
        if (ingredient.apply(inSlot)) {
          ingredient.supply(inSlot.getCount());
          continue outer;
        }
      }
    }

    for (CountableIngredientStack ingredient : ingredients) {
      if (!ingredient.filled()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public ItemStack assemble(CrystalWorkbenchCrafting inv) {
    ItemStack resultCopy = result.copy();
    for (Processor<CrystalWorkbenchCrafting> processor : getProcessors()) {
      resultCopy = processor.processOutput(resultCopy, getIngredientStacks(), inv.getCombinedItems(), inv);
    }
    return resultCopy;
  }

  @Override
  public ItemStack getResultItem() {
    return result;
  }

  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.CRYSTAL_WORKBENCH.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return ModRecipes.Types.CRYSTAL_WORKBENCH;
  }

  public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CrystalWorkbenchRecipe> {

    @Override
    public CrystalWorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
      JsonArray incoming = JSONUtils.getAsJsonArray(json, "ingredients");
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(incoming.size(), IngredientStack.EMPTY);
      for (int i = 0; i < incoming.size(); i++) {
        JsonElement ing = incoming.get(i);
        if (ing.isJsonObject()) {
          ingredients.set(i, IngredientStack.deserialize(ing.getAsJsonObject()));
        } else {
          throw new JsonSyntaxException("Invalid ingredient: " + ing);
        }
      }

      ItemStack result;
      if (!json.has("result")) {
        throw new JsonSyntaxException("Missing result, expected to find a string or object");
      }

      if (json.get("result").isJsonObject()) {
        result = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
      } else {
        ResourceLocation rl = new ResourceLocation(JSONUtils.getAsString(json, "result"));
        Item item = ForgeRegistries.ITEMS.getValue(rl);
        if (item == null) {
          throw new JsonSyntaxException("Missing result, item: " + rl + " doesn't exist.");
        }
        int count;
        if (!json.has("count")) {
          count = 1;
        } else {
          count = JSONUtils.getAsInt(json, "count");
        }
        result = new ItemStack(item, count);
      }

      CrystalWorkbenchRecipe recipe = new CrystalWorkbenchRecipe(ingredients, result, recipeId);

      if (json.has("processors")) {
        JsonArray jsonProcessors = JSONUtils.getAsJsonArray(json, "processors");
        for (JsonElement processor : jsonProcessors) {
          ResourceLocation proc = new ResourceLocation(processor.getAsString());
          try {
            //noinspection unchecked
            recipe.addProcessor((Processor<CrystalWorkbenchCrafting>) ModRegistries.PROCESSOR_REGISTRY.getValue(proc));
          } catch (ClassCastException e) {
            throw new JsonSyntaxException("Invalid processor in processors array: " + proc, e);
          }
        }
      }

      return recipe;
    }

    @Nullable
    @Override
    public CrystalWorkbenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
      int ingCount = buffer.readVarInt();
      NonNullList<IngredientStack> ingredients = NonNullList.withSize(ingCount, IngredientStack.EMPTY);
      for (int i = 0; i < ingredients.size(); i++) {
        ingredients.set(i, IngredientStack.read(buffer));
      }

      ItemStack result = buffer.readItem();

      CrystalWorkbenchRecipe recipe = new CrystalWorkbenchRecipe(ingredients, result, recipeId);

      int processorCount = buffer.readVarInt();
      for (int i = 0; i < processorCount; i++) {
        ResourceLocation rl = buffer.readResourceLocation();
        IProcessor<?> processor = ModRegistries.PROCESSOR_REGISTRY.getValue(rl);
        if (processor != null) {
          //noinspection unchecked
          recipe.addProcessor((Processor<CrystalWorkbenchCrafting>) processor);
        }
      }

      // IngredientInfo?

      return recipe;
    }

    @Override
    public void toNetwork(PacketBuffer buffer, CrystalWorkbenchRecipe recipe) {
      buffer.writeVarInt(recipe.getIngredientStacks().size());
      for (IngredientStack ingredientStack : recipe.getIngredientStacks()) {
        ingredientStack.write(buffer);
      }
      buffer.writeItem(recipe.getResultItem());
      buffer.writeVarInt(recipe.getProcessors().size());
      for (Processor<CrystalWorkbenchCrafting> processor : recipe.getProcessors()) {
        buffer.writeResourceLocation(Objects.requireNonNull(processor.getRegistryName()));
      }
    }
  }
}
