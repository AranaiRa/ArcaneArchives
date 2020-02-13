/*package com.aranaira.arcanearchives.recipe.gct;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.api.cwb.CrystalWorkbenchRecipe;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

public class GCTRecipe implements CrystalWorkbenchRecipe {
  private final List<IngredientStack> ingredients = new ArrayList<>();
  private final ItemStack result;
  private final ResourceLocation name;

  public GCTRecipe(String name, @Nonnull ItemStack result, Object... recipe) {
    this(new ResourceLocation(ArcaneArchives.MODID, name), result, recipe);
  }

  public GCTRecipe(ResourceLocation name, @Nonnull ItemStack result, List<IngredientStack> ingredients) {
    this.result = result;
    this.name = name;
    this.ingredients.addAll(ingredients);
  }

  public GCTRecipe(ResourceLocation name, @Nonnull ItemStack result, Object... recipe) {
    this.result = result.copy();
    this.name = name;
    int i = 0;
    for (Object stack : recipe) {
      if (stack instanceof ItemStack) {
        ingredients.add(new IngredientStack((ItemStack) stack));
      } else if (stack instanceof Item) {
        ingredients.add(new IngredientStack((Item) stack));
      } else if (stack instanceof Ingredient) {
        ingredients.add(new IngredientStack((Ingredient) stack));
      } else if (stack instanceof String) {
        ingredients.add(new IngredientStack((String) stack));
      } else if (stack instanceof IngredientStack) {
        ingredients.add((IngredientStack) stack);
      } else if (stack instanceof Block) {
        ingredients.add(new IngredientStack((Block) stack));
      } else {
        ArcaneArchives.logger.warn(String.format("Unknown ingredient type for recipe %s, skipped: ingredient %d, %s", name.toString(), i, stack.toString()));
      }
      i++;
    }
  }

  @Override
  public int getIndex() {
    return GCTRecipeList.instance.indexOf(this);
  }

  @Override
  public ResourceLocation getName() {
    return name;
  }

  @Override
  public boolean matches(@Nonnull IItemHandler inv) {
    return new IngredientsMatcher(ingredients).matches(inv);
  }

  @Override
  public boolean craftable(EntityPlayer player, TileEntity craftingInventory) {
    return true;
  }

  @Override
  public Int2IntMap getMatchingSlots(@Nonnull IItemHandler inv) {
    return new IngredientsMatcher(ingredients).getMatchingSlots(inv);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return result.copy();
  }

  @Override
  public List<IngredientStack> getIngredients() {
    return ingredients;
  }

  // Only called on the server side, in theory
  @Override
  public ItemStack onCrafted(EntityPlayer player, ItemStack output) {
    return output;
  }

  // Also only called on the server side
  @Override
  public boolean handleItemResult(World world, EntityPlayer player, TileEntity craftingTile, ItemStack ingredient) {
    boolean doReturn = false;

    IFluidHandlerItem cap = ingredient.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
    if (cap != null && cap.getTankProperties().length > 0) {
      FluidStack toDrain = cap.getTankProperties()[0].getContents();
      cap.drain(toDrain, true);
      ingredient = cap.getContainer();
      doReturn = true;
    }

    if (ingredient.getItem() instanceof ItemFlintAndSteel) {
      ingredient.damageItem(1, player);
      doReturn = true;
    }

    if (doReturn) {
      ItemStack result = ingredient;
      if (craftingTile != null) {
        IItemHandler drawer = craftingTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        result = ItemHandlerHelper.insertItemStacked(drawer, ingredient, false);
      }
      if (!result.isEmpty()) {
        IItemHandler inventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        result = ItemHandlerHelper.insertItemStacked(inventory, result, false);
        if (!result.isEmpty()) {
          Block.spawnAsEntity(world, player.getPosition(), result);
        }
      }
    }

    return doReturn;
  }

  @Override
  public void consumeAndHandleInventory(CrystalWorkbenchRecipe recipe, IItemHandler inventory, EntityPlayer player, @Nullable TileEntity tile, @Nullable Runnable callback, @Nullable RecipeIngredientHandler handler) {
    for (Map.Entry entry : recipe.getMatchingSlots(inventory).int2IntEntrySet()) {
      ItemStack result = inventory.extractItem(entry.getIntKey(), entry.getIntValue(), false);
*//*      if (!player.world.isRemote) {
        if (handler != null && handler.apply(player.world, player, tile, result)) {
          if (callback != null) {
            callback.run();
          }
        }
      }*//*
    }
  }
*//**//*}*/
