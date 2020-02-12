package com.aranaira.arcanearchives.api.cwb;

import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class CrystalWorkbenchRecipe extends IForgeRegistryEntry.Impl<CrystalWorkbenchRecipe> implements ICrystalWorkbenchRecipe {
  protected List<IngredientStack> inputs;
  protected List<IngredientTransformer> transformers = new ArrayList<>();
  protected ItemStack output;
  protected int index;

  public CrystalWorkbenchRecipe(List<IngredientStack> inputs, ItemStack output) {
    this(inputs, output, true);
  }

  public CrystalWorkbenchRecipe(List<IngredientStack> inputs, ItemStack output, boolean forgeDefault) {
    this.inputs = inputs;
    this.output = output;
    if (forgeDefault) {
      transformers.add(new IngredientTransformer.DefaultForgeContainer());
    }
  }

  @Override
  public void addIngredientTransformer(IngredientTransformer transformer) {
    transformers.add(transformer);
  }

  @Override
  public List<IngredientTransformer> getIngredientTransformers() {
    return transformers;
  }

  @Override
  public MatchResult matches(WorkbenchCrafting inventory, World world) {
    return new MatchResult(this, inventory);
  }

  @Override
  public ItemStack getActualResult(WorkbenchCrafting inventory, UUID workbenchId, @Nullable World world) {
    ItemStack result = getResult().copy();
    NBTTagCompound tag = ItemUtils.getOrCreateTagCompound(result);
    tag.setUniqueId("network", workbenchId);
    return result;
  }

  @Override
  public ItemStack getResult() {
    return output;
  }

  @Override
  public List<IngredientStack> getIngredients() {
    return inputs;
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Override
  public void setIndex(int index) {
    this.index = index;
  }
}
