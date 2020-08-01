package com.aranaira.arcanearchives.types;

import com.aranaira.arcanearchives.containers.ManifestContainer;
import com.aranaira.arcanearchives.util.ItemUtils;
import com.aranaira.arcanearchives.util.ManifestUtils.CollatedEntry;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.Iterators;
import io.netty.buffer.ByteBuf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class ManifestList extends ForwardingList<CollatedEntry> implements ISerializeByteBuf<ManifestList> {
  private static Map<String, ModContainer> modList = null;
  private ManifestContainer listener = null;
  private String filterText;
  private ItemStack searchItem;
  private SortingDirection sortingDirection = null;
  private SortingType sortingType = null;
  private List<CollatedEntry> reference;

  public ManifestList() {
    this(new ArrayList<>());
  }

  @Override
  protected List<CollatedEntry> delegate() {
    return this.reference;
  }

  public ManifestList(List<CollatedEntry> reference) {
    this(reference, null);
  }

  public ManifestList(List<CollatedEntry> reference, String filterText) {
    this.reference = reference;
    this.filterText = filterText;
  }

  public static ManifestList deserialize(ByteBuf buf) {
    ManifestList list = new ManifestList();
    return list.fromBytes(buf);
  }

  private static Map<String, ModContainer> getModList() {
    if (modList == null) {
      modList = Loader.instance().getIndexedModList();
    }
    return modList;
  }

  @Nullable
  private static String getModName(ItemStack stack) {
    if (!stack.isEmpty()) {
      String modId = stack.getItem().getCreatorModId(stack);
      if (modId == null) {
        return null;
      }

      ModContainer mod = getModList().get(modId);
      if (mod == null) {
        return null;
      }

      return mod.getName();
    }

    return null;
  }

  private static String adjustModName(ItemStack stack) {
    String name = getModName(stack);
    if (name != null) {
      return name.replace(" ", "").toLowerCase();
    } else {
      return "";
    }
  }

  @SuppressWarnings({"ConstantConditions", "Guava"})
  // TODO: Soft client-only?
  public com.google.common.base.Predicate<CollatedEntry> filter() {
    return (entry) -> {
      if (entry == null) {
        return false;
      }

      if (filterText == null && searchItem == null) {
        return true;
      }

      ItemStack stack = entry.getStack();
      if (searchItem != null) {
        return ItemUtils.areStacksEqualIgnoreSize(searchItem, stack);
      }

      String filter = filterText == null ? "" : filterText.toLowerCase();
      final boolean mod = filter.startsWith("@");
      if (mod) {
        filter = filter.replaceFirst("@", "");
        return adjustModName(stack).contains(filter) || stack.getItem().getRegistryName().getNamespace().toLowerCase().contains(filter);
      } else {
        if (stack.getItem().getRegistryName().getPath().toLowerCase().contains(filter) || stack.getDisplayName().toLowerCase().contains(filter)) {
          return true;
        }

        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        if (!enchants.isEmpty()) {
          for (Enchantment ench : enchants.keySet()) {
            if (ench == null) {
              continue; // Yes, this IS actually possible
            }

            if (ench.getTranslatedName(0).toLowerCase().contains(filter)) {
              return true;
            }
          }
        }
      }

      return false;
    };
  }

  // TODO: MAY OR MAY NOT WORK
  public ManifestListIterable filtered() {
    return new ManifestListIterable(new ManifestIterator(Iterators.filter(super.iterator(), filter())));
  }
 /*    }).collect(Collectors.toCollection(ManifestList::new));
    filtered.sortingDirection = sortingDirection;
    filtered.sortingType = sortingType;
    filtered.searchItem = searchItem;
    filtered.filterText = filterText;
    return filtered;
  }*/

  @Override
  @Nonnull
  public ManifestIterator iterator() {
    return new ManifestIterator(super.iterator());
  }

  public void setListener(ManifestContainer containerManifest) {
    this.listener = containerManifest;
  }

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

  public void setItemDirectionType (ItemStack stack, SortingDirection direction, SortingType type) {
    setSortingDirection(direction);
    setSortingType(type);
    setSearchItem(stack);
  }

  public ManifestList sorted() {
    ManifestList copy = new ManifestList(this.delegate(), filterText);
    copy.setItemDirectionType(searchItem, sortingDirection, sortingType);
    copy.sort((o1, o2) -> {
      if (sortingType == SortingType.NAME) {
        if (sortingDirection == SortingDirection.ASCENDING) {
          return o1.finalStack.getDisplayName().compareTo(o2.finalStack.getDisplayName());
        } else {
          return o2.finalStack.getDisplayName().compareTo(o1.finalStack.getDisplayName());
        }
      } else {
        if (sortingDirection == SortingDirection.ASCENDING) {
          return Integer.compare(o1.finalStack.getCount(), o2.finalStack.getCount());
        } else {
          return Integer.compare(o2.finalStack.getCount(), o1.finalStack.getCount());
        }
      }
    });

    return copy;
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

  public class ManifestListIterable implements Iterable<CollatedEntry> {
    private ManifestIterator iterator;

    public ManifestListIterable(ManifestIterator iter) {
      this.iterator = iter;
    }

    public int getSlot() {
      return iterator.getSlot();
    }

    @Override
    @Nonnull
    public Iterator<CollatedEntry> iterator() {
      return iterator;
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
