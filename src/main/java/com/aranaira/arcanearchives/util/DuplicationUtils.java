package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.google.common.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class DuplicationUtils {
  public static Path FILE_PATH;

  private static List<ItemStack> oresToDuplicate = new ArrayList<>();
  private static List<Ingredient> ingredientsToDuplicate = new ArrayList<>();
  private static Type LIST_TYPE = new TypeToken<ArrayList<ItemStack>>() {
  }.getType();

  public static void init() {
    FILE_PATH = new File(new File(ArcaneArchives.configDirectory, ArcaneArchives.MODID), "ores.txt").toPath();
    if (!readData() || oresToDuplicate.isEmpty()) {
      generate();
      writeData();
    }
  }

  public static void generate() {
    for (ItemStack entry : FurnaceRecipes.instance().getSmeltingList().keySet()) {
      int[] ids;
      if (entry.getMetadata() == OreDictionary.WILDCARD_VALUE) {
        ItemStack copy = entry.copy();
        copy.setItemDamage(0);
        ids = OreDictionary.getOreIDs(copy);
      } else {
        ids = OreDictionary.getOreIDs(entry);
      }
      for (int id : ids) {
        String name = OreDictionary.getOreName(id);
        if (name.startsWith("ore")) {
          oresToDuplicate.add(entry);
          ingredientsToDuplicate.add(Ingredient.fromStacks(entry));
          break;
        }
      }
    }
  }

  public static List<ItemStack> getOresToDuplicate() {
    return oresToDuplicate;
  }

  public static boolean shouldDuplicate(ItemStack stack) {
    for (Ingredient ingredient : ingredientsToDuplicate) {
      if (ingredient.apply(stack)) {
        return true;
      }
    }

    return false;
  }

  private static boolean readData() {
    oresToDuplicate.clear();
    List<String> lines;
    try {
      lines = Files.readAllLines(FILE_PATH);
    } catch (IOException e) {
      return false;
    }
    for (String line : lines) {
      line = line.trim();
      if (line.startsWith("#")) {
        continue;
      }

      String[] split = line.split(":");
      // modid : item : meta : count
      ResourceLocation itemRL;
      int meta = 0;
      int count = 1;
      if (split.length < 2) {
        continue;
      }
      itemRL = new ResourceLocation(split[0], split[1]);
      if (split.length >= 3) {
        meta = Integer.parseInt(split[2]);
      }
      if (split.length == 4) {
        count = Integer.parseInt(split[3]);
      }

      Item item = ForgeRegistries.ITEMS.getValue(itemRL);
      if (item == null) {
        continue;
      }

      ItemStack stack = new ItemStack(item, count, meta);
      oresToDuplicate.add(stack);
      ingredientsToDuplicate.add(Ingredient.fromStacks(stack));
    }
    return true;
  }

  public static void writeData() {
    List<String> output = new ArrayList<>();
    output.add("# Format: modid:item:meta:count. Delete lines or comment them out with #.");
    for (ItemStack value : oresToDuplicate) {
      output.add(String.format("%s:%d:%d", Objects.requireNonNull(value.getItem().getRegistryName()).toString(), value.getMetadata(), value.getCount()));
    }
    try {
      Files.write(FILE_PATH, output);
    } catch (IOException e) {
      ArcaneArchives.logger.error("Unable to write ore information to configuration!");
    }
  }
}
