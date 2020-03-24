package com.aranaira.arcanearchives.items.upgrades;

import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class UpgradeInfo implements IUpgrade {
  private final UpgradeType upgradeType;
  private final Function<ItemStack, Integer> upgradeSize;
  private final Function<ItemStack, Integer> upgradeSlot;
  private final Function<ItemStack, List<Class<? extends TileEntity>>> upgradeFor;

  public UpgradeInfo(UpgradeType upgradeType, Function<ItemStack, Integer> upgradeSize, Function<ItemStack, Integer> upgradeSlot, Function<ItemStack, List<Class<? extends TileEntity>>> upgradeFor) {
    this.upgradeType = upgradeType;
    this.upgradeSize = upgradeSize;
    this.upgradeSlot = upgradeSlot;
    this.upgradeFor = upgradeFor;
  }

  @Override
  public int getUpgradeSlot(ItemStack stack) {
    return upgradeSlot.apply(stack);
  }

  @Override
  public int getUpgradeSize(ItemStack stack) {
    return upgradeSize.apply(stack);
  }

  @Override
  public List<Class<? extends TileEntity>> getUpgradeClasses(ItemStack stack) {
    return upgradeFor.apply(stack);
  }

  public static class Builder {
    private UpgradeType type;
    private Predicate<ItemStack> sizeMatcher = ANY;
    private Predicate<ItemStack> slotMatcher = ANY;
    private Predicate<ItemStack> classMatcher = ANY;
    private int size = 1;
    private int slot = 0;
    private List<Class<? extends TileEntity>> classes = Collections.emptyList();

    public Builder(UpgradeType type) {
      this.type = type;
    }

    public Builder slot(int slot) {
      this.slot = slot;
      return this;
    }

    public Builder slot(ItemStack stack, int slot) {
      this.slot = slot;
      this.slotMatcher = Ingredient.fromStacks(stack);
      return this;
    }

    public Builder slot(Predicate<ItemStack> predicate, int slot) {
      this.slot = slot;
      this.slotMatcher = predicate;
      return this;
    }

    public Builder size(int size) {
      this.size = size;
      return this;
    }

    public Builder size(ItemStack stack, int size) {
      this.size = size;
      this.sizeMatcher = Ingredient.fromStacks(stack);
      return this;
    }

    public Builder size(Predicate<ItemStack> predicate, int size) {
      this.size = size;
      this.sizeMatcher = predicate;
      return this;
    }

    public Builder classes (Class<? extends TileEntity> ... classes) {
      this.classes = Arrays.asList(classes);
      return this;
    }

    public Builder classes (List<Class<? extends TileEntity>> classes) {
      this.classes = classes;
      return this;
    }

    public Builder classes(ItemStack stack, Class<? extends TileEntity> ... classes) {
      this.classes = Arrays.asList(classes);
      this.classMatcher = Ingredient.fromStacks(stack);
      return this;
    }

    public Builder classes(ItemStack stack, List<Class<? extends TileEntity>> classes) {
      this.classes = classes;
      this.classMatcher = Ingredient.fromStacks(stack);
      return this;
    }

    public Builder classes(Predicate<ItemStack> stack, Class<? extends TileEntity> ... classes) {
      this.classes = Arrays.asList(classes);
      this.classMatcher = stack;
      return this;
    }

    public Builder classes(Predicate<ItemStack> stack, List<Class<? extends TileEntity>> classes) {
      this.classes = classes;
      this.classMatcher = stack;
      return this;
    }

    public UpgradeInfo build () {
      Function<ItemStack, Integer> slotFunction = (itemstack) -> {
        if (slotMatcher.test(itemstack)) {
          return slot;
        } else {
          return -1;
        }
      };

      Function<ItemStack, Integer> sizeFunction = (itemstack) -> {
        if (sizeMatcher.test(itemstack)) {
          return size;
        } else {
          return -1;
        }
      };

      Function<ItemStack, List<Class<? extends TileEntity>>> classFunction = (itemstack) -> {
        if (classMatcher.test(itemstack)) {
          return classes;
        } else {
          return Collections.emptyList();
        }
      };

      return new UpgradeInfo(type, sizeFunction, slotFunction, classFunction);
    }
  }

  private static AnyIngredient ANY = new AnyIngredient();

  private static class AnyIngredient implements Predicate<ItemStack> {
    @Override
    public boolean test(ItemStack stack) {
      return true;
    }
  }
}
