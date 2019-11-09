package com.aranaira.arcanearchives.types.lists;

import com.aranaira.arcanearchives.inventory.ContainerManifest;
import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManifestList extends ReferenceList<CollatedEntry> implements ISerializeByteBuf<ManifestList> {
  private static Map<String, ModContainer> modList = null;
  private ContainerManifest listener = null;
  private String filterText;
  private ItemStack searchItem;
  private SortingDirection sortingDirection = null;
  private SortingType sortingType = null;

  public ManifestList(List<CollatedEntry> reference) {
    super(reference);
    this.filterText = null;
  }

  public ManifestList(List<CollatedEntry> reference, String filterText) {
    super(reference);
    this.filterText = filterText;
  }

  public static ManifestList deserialize(ByteBuf buf) {
    ManifestList list = new ManifestList();
    return list.fromBytes(buf);
  }

  public static Map<String, ModContainer> getModList() {
    if (modList == null) {
      modList = Loader.instance().getIndexedModList();
    }
    return modList;
  }

  public static String getModName(ItemStack stack) {
    if (stack.isEmpty()) {
      return "";
    }

    String modId = stack.getItem().getCreatorModId(stack);
    if (modId == null) {
      return "";
    }

    ModContainer mod = getModList().get(modId);
    if (mod == null) {
      return "";
    }

    return mod.getName();
  }

  public static String getAdustedModName(ItemStack stack) {
    String name = getModName(stack);
    if (name.isEmpty()) {
      return "";
    }

    return name.replace(" ", "").toLowerCase();
  }

  public ManifestList filtered() {
    if (filterText == null && searchItem == null) {
      return this;
    }

    String filter = "";

    if (filterText != null) {
      filter = filterText.toLowerCase();
    }

    boolean mod = false;

    if (filter.startsWith("@")) {
      mod = true;
      filter = filter.replaceFirst("@", "");
    }

    final boolean modFilter = mod;

    String finalFilter = filter;
    ManifestList filtered = stream().filter((entry) -> {
      if (entry == null) {
        return false;
      }

      ItemStack stack = entry.getStack();

      if (searchItem != null) {
        return ItemUtils.areStacksEqualIgnoreSize(searchItem, stack);
      }

      if (!modFilter) {
        String display = stack.getDisplayName().toLowerCase();
        if (display.contains(finalFilter)) {
          return true;
        }
        String registry = stack.getItem().getRegistryName().getPath().toLowerCase();
        if (registry.contains(finalFilter)) {
          return true;
        }
      } else if (modFilter) {
        String modName = getAdustedModName(stack);
        if (modName.contains(finalFilter)) {
          return true;
        }
        String resource = stack.getItem().getRegistryName().getNamespace().toLowerCase();
        if (resource.contains(finalFilter)) {
          return true;
        }
      }

      if (!modFilter) {
        // Other hooks to be added at a later point
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
          Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
          for (Map.Entry<Enchantment, Integer> ench : map.entrySet()) {
            if (ench.getKey() == null) {
              continue; // Yes, it is possible for this value to be null. WHO KNEW.
            }
            String enchName = ench.getKey().getTranslatedName(ench.getValue());
            if (enchName.toLowerCase().contains(finalFilter)) {
              return true;
            }
          }
        }
      }

      return false;
    }).collect(Collectors.toCollection(ManifestList::new));
    filtered.sortingDirection = sortingDirection;
    filtered.sortingType = sortingType;
    filtered.searchItem = searchItem;
    filtered.filterText = filterText;
    return filtered;
  }

  public ManifestList() {
    super(new ArrayList<>());
  }

  /**
   * Register provided {@link ContainerManifest} as a listener to {@link #deserializationFinished()} events
   *
   * @param containerManifest a {@link ContainerManifest}
   */
  public void setListener(ContainerManifest containerManifest) {
    this.listener = containerManifest;
  }

  /**
   * Call this after this ManifestList has finished being populated from an external source.
   * For now this means from a packet from the server
   * <p>
   * If a {@link ContainerManifest} listener has been registered to this manifest then
   * notify it that this {@link ManifestList} has been populated
   */
  public void deserializationFinished() {
    if (this.listener != null) {
      this.listener.ensureCapacity(size());
    }
  }

  @Nullable
  public CollatedEntry getEntryForSlot(int slot) {
    if (slot < size() && slot >= 0) {
      return get(slot);
    }
    return null;
  }

  public ItemStack getItemStackForSlot(int slot) {
    if (slot < size() && slot >= 0) {
      return get(slot).getStack();
    }
    return ItemStack.EMPTY;
  }

  public String getSearchText() {
    return this.filterText;
  }

  public ItemStack getSearchItem() {
    return this.searchItem;
  }

  public void setSearchText(String searchTerm) {
    this.filterText = searchTerm;
  }

  public void setSearchItem(ItemStack stack) {
    this.searchItem = stack;
  }

  public SortingDirection getSortingDirection() {
    return sortingDirection;
  }

  public void setSortingDirection(SortingDirection sortingDirection) {
    this.sortingDirection = sortingDirection;
  }

  public SortingType getSortingType() {
    return sortingType;
  }

  public void setSortingType(SortingType sortingType) {
    this.sortingType = sortingType;
  }

  @Override
  public ManifestListIterable iterable() {
    return new ManifestListIterable(new ManifestIterator(iterator()));
  }

  public ManifestList sorted() {
    ManifestList copy = new ManifestList(new ArrayList<>(), null);
    copy.filterText = filterText;
    copy.searchItem = searchItem;
    copy.sortingDirection = sortingDirection;
    copy.sortingType = sortingType;

    copy.addAll(this);
    copy.sort((o1, o2) -> {
      if (copy.sortingType == SortingType.NAME) {
        if (copy.sortingDirection == SortingDirection.ASCENDING) {
          return o1.finalStack.getDisplayName().compareTo(o2.finalStack.getDisplayName());
        } else {
          return o2.finalStack.getDisplayName().compareTo(o1.finalStack.getDisplayName());
        }
      } else {
        if (copy.sortingDirection == SortingDirection.ASCENDING) {
          return Integer.compare(o1.finalStack.getCount(), o2.finalStack.getCount());
        } else {
          return Integer.compare(o2.finalStack.getCount(), o1.finalStack.getCount());
        }
      }
    });

    return copy;
  }

  @Override
  public void clear() {
    super.clear();
  }

  @Override
  public ManifestList fromBytes(ByteBuf buf) {
    this.clear();
    int entries = buf.readInt();
    for (int i = 0; i < entries; i++) {
      this.add(CollatedEntry.deserialize(buf));
    }
    return this;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.size());
    for (CollatedEntry entry : this) {
      entry.toBytes(buf);
    }
  }

  public class ManifestListIterable extends ReferenceListIterable<CollatedEntry> {
    public ManifestListIterable(ManifestIterator iter) {
      super(iter);
    }

    public int getSlot() {
      return ((ManifestIterator) iter).getSlot();
    }
  }

  public class ManifestIterator implements Iterator<CollatedEntry> {
    private int slot = 0;
    private Iterator<CollatedEntry> iter;

    public ManifestIterator(Iterator<CollatedEntry> iter) {
      this.iter = iter;
    }

    public int getSlot() {
      return slot;
    }

    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public CollatedEntry next() {
      slot++;
      return iter.next();
    }
  }

  public enum SortingDirection {
    ASCENDING, DESCENDING
  }

  public enum SortingType {
    NAME, QUANTITY
  }
}
