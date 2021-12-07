package com.aranaira.arcanearchives.api.crafting.recipes;

import com.aranaira.arcanearchives.api.ArcaneArchivesAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ResolvingRecipeType<C extends IInventory, T extends IRecipe<C>> extends JsonReloadListener {
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
  private final Supplier<IRecipeType<T>> type;
  private List<T> cache = null;
  private final Comparator<? super T> comparator;
  private final Object2IntOpenHashMap<ResourceLocation> reverseLookup = new Object2IntOpenHashMap<>();

  public ResolvingRecipeType(Supplier<IRecipeType<T>> type, Comparator<? super T> comparator) {
    super(GSON, "recipes");
    this.type = type;
    this.comparator = comparator;
  }

  public List<T> getRecipes() {
    if (cache == null) {
      cache = ArcaneArchivesAPI.getInstance().getRecipeManager().getAllRecipesFor(type.get());
      cache.sort(comparator);
      reverseLookup.clear();
      for (int i = 0; i < cache.size(); i++) {
        reverseLookup.put(cache.get(i).getId(), i);
      }
    }

    return cache;
  }

  @Nullable
  public T getRecipe (ResourceLocation location) {
    int index = lookup(location);
    if (index == -1) {
      return null;
    }
    return getRecipe(index);
  }

  public int size () {
    return getRecipes().size();
  }

  public T getRecipe(int index) {
    if (index < 0 || index >= getRecipes().size()) {
      throw new RuntimeException("Index " + index + " not in valid range for recipe type " + type + " [0," + getRecipes().size() + ")");
    }

    return getRecipes().get(index);
  }

  public boolean hasRecipe(int index) {
    return index < getRecipes().size();
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, IResourceManager pResourceManager, IProfiler pProfiler) {
    this.cache = null;
  }

  public int lookup (ResourceLocation recipeId) {
    getRecipes();
    return reverseLookup.getOrDefault(recipeId, -1);
  }
}
