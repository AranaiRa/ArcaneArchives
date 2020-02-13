package com.aranaira.arcanearchives.api.cwb;

import com.aranaira.arcanearchives.api.crafting.IngredientStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface ICrystalWorkbenchRecipe {
  int getIndex ();
  void setIndex (int index);

  MatchResult matches(WorkbenchCrafting inventory);

  ItemStack getActualResult(WorkbenchCrafting inventory, UUID workbenchId, @Nullable World world);

  ItemStack getResult();

  void addIngredientTransformer (IngredientTransformer transformer);

  List<IngredientTransformer> getIngredientTransformers ();

  // TODO
  default NonNullList<ItemStack> getRemainingIngredients(WorkbenchCrafting inventory, @Nullable EntityPlayer player) {
    NonNullList<ItemStack> ret = NonNullList.withSize(inventory.getInventory().getSlots(), ItemStack.EMPTY);
    for (int i = 0; i < ret.size(); i++) {
      ItemStack item = inventory.getInventory().getStackInSlot(i);
      for (IngredientTransformer transformer : getIngredientTransformers()) {
        ItemStack result = transformer.apply(inventory, player, item);
        if (result == null) {
          inventory.getInventory().setStackInSlot(i, ItemStack.EMPTY);
        } else {
          item = result;
        }
      }
      ret.set(i, item);
    }
    return ret;
  }

  List<IngredientStack> getIngredients();
}
