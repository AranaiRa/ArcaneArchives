package com.aranaira.arcanearchives.api.cwb;

import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface IArcaneArchivesRecipe {
  MatchResult matches(WorkbenchCrafting inventory);

  ItemStack getActualResult(WorkbenchCrafting inventory, UUID workbenchId);

  ItemStack getResult();

  void addIngredientTransformer(IngredientTransformer transformer);

  List<IngredientTransformer> getIngredientTransformers();

  // TODO
  NonNullList<ItemStack> getRemainingIngredients(WorkbenchCrafting inventory, @Nullable PlayerEntity player);

  List<IngredientStack> getIngredients();

  default ItemStack onCrafted (PlayerEntity player, ItemStack stack) {
    return stack;
  }
}
