package com.aranaira.arcanearchives.client.tracking;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.MathUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ManifestTracking {
  private static Int2ObjectOpenHashMap<Set<Vec3d>> positionsByDimension = new Int2ObjectOpenHashMap<>();
  private static Int2ObjectOpenHashMap<Long2ObjectOpenHashMap<List<ItemStack>>> reference = new Int2ObjectOpenHashMap<>();
  private static Set<ItemStack> allTracked = new HashSet<>();

  // TODO: Only fired on the server
/*  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    clear();
  }*/

  public static void clear() {
    reference.clear();
    allTracked = null;
    positionsByDimension = null;
  }

/*  public static void add(CollatedEntry entry) {
    for (EntryDescriptor innerEntry : entry.descriptions) {
      add(entry.getStack(), innerEntry.dimension, innerEntry.pos);
    }
  }

  public static void remove(CollatedEntry entry) {
    for (EntryDescriptor innerEntry : entry.descriptions) {
      remove(entry.getStack(), innerEntry.dimension, innerEntry.pos);
    }
  }*/

  public static void add(ItemStack stack, int dimension, BlockPos pos) {
    List<ItemStack> dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
    if (dim == null) {
      dim = new ArrayList<>();
      getDimension(dimension).put(pos.toLong(), dim);
    }

    dim.add(stack);
    allTracked = null;
    positionsByDimension = null;
  }

  public static void remove(ItemStack stack, int dimension, BlockPos pos) {
    List<ItemStack> dim = getDimension(dimension).getOrDefault(pos.toLong(), null);
    if (dim != null) {
      dim.removeIf(t -> ItemUtils.areStacksEqualIgnoreSize(t, stack));
    }
    allTracked = null;
    positionsByDimension = null;
  }

  private static Long2ObjectOpenHashMap<List<ItemStack>> getDimension(int dimension) {
    return reference.computeIfAbsent(dimension, k -> new Long2ObjectOpenHashMap<>());
  }

  private static Set<Vec3d> getDimensionPositions(int dimension) {
    if (positionsByDimension == null) {
      positionsByDimension = new Int2ObjectOpenHashMap<>();
      for (Entry<Long2ObjectOpenHashMap<List<ItemStack>>> entry : reference.int2ObjectEntrySet()) {
        Set<Vec3d> positions = entry.getValue().entrySet().stream().filter(t -> !t.getValue().isEmpty()).map(b -> MathUtils.vec3dFromLong(b.getKey())).collect(Collectors.toSet());
        positionsByDimension.put(entry.getIntKey(), positions);
      }
    }
    return positionsByDimension.computeIfAbsent(dimension, k -> new HashSet<>());
  }

  public static void remove(int dimension, BlockPos pos) {
    remove(dimension, pos.toLong());
  }

  public static void remove(int dimension, long pos) {
    Long2ObjectOpenHashMap<List<ItemStack>> dim = getDimension(dimension);
    if (dim.containsKey(pos)) {
      dim.remove(pos);
    }
  }

  @Nullable
  public static List<ItemStack> get(int dimension, BlockPos pos) {
    return getDimension(dimension).getOrDefault(pos.toLong(), null);
  }

  public static Set<ItemStack> getAllTracked() {
    if (allTracked == null) {
      allTracked = new HashSet<>();
      for (int dimension : reference.keySet()) {
        Long2ObjectOpenHashMap<List<ItemStack>> dimensionEntry = getDimension(dimension);
        dimensionEntry.values().forEach(allTracked::addAll);
      }
    }
    return allTracked;
  }

  public static boolean matches(ItemStack stack) {
    return matches(stack, getAllTracked());
  }

  public static boolean matches(ItemStack stack, Collection<ItemStack> ingredients) {
    if (ingredients.isEmpty()) {
      return false;
    }

    if (stack.isEmpty()) {
      return false;
    }

    for (ItemStack otherStack : ingredients) {
      if (ItemUtils.areStacksEqualIgnoreSize(stack, otherStack)) {
        return true;
      }
    }

    return false;
  }

  public static Set<Vec3d> getPositions(int dimension) {
    return getDimensionPositions(dimension);
  }
}
