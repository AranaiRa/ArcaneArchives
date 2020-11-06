// TODO: This

package com.aranaira.arcanearchives.util;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.tileentities.MonitoringCrystalTile;
import com.aranaira.arcanearchives.tileentities.TrackingNetworkedBaseTile;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.tilenetwork.NetworkEntry;
import com.aranaira.arcanearchives.tilenetwork.PlayerConfigAggregator;
import com.aranaira.arcanearchives.tilenetwork.PlayerNetworkConfig;
import com.aranaira.arcanearchives.types.BlockPosDimension;
import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import com.aranaira.arcanearchives.types.SlotIterable;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.items.IItemHandler;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Deprecated
public class ManifestUtils {
  public static void resolveItemEntries(List<ItemEntry> entries, ItemEntry entry) {
    if (entry.stack.getMaxStackSize() == 1) {
      ItemStack stack = entry.stack.copy();
      stack.setCount(1);
      for (int i = 0; i < entry.stack.getCount(); i++) {
        entries.add(entry.copy(stack.copy()));
      }
    } else {
      entries.add(entry);
    }
  }

  public static List<CollatedEntry> parsePreManifest(Map<Integer, List<ItemEntry>> preManifest, Network network, EntityPlayer player) {
    // We need to collate all of these entries into the following:
    // Unique entries should be collapsed (along with positions & descriptions)
    // Out of range entries should be collapsed (etc)
    // NBT-based differences should be adhered to
    Map<ItemStack, List<ItemEntry>> phase1 = new HashMap<>();
    PlayerNetworkConfig config = PlayerConfigAggregator.byPlayer(player);

    for (int packed : preManifest.keySet()) {
      phase:
      for (ItemEntry entry : preManifest.get(packed)) {
        for (ItemStack keyStack : phase1.keySet()) {
          if (ItemUtils.areStacksEqualIgnoreSize(entry.stack, keyStack)) {
            List<ItemEntry> phaseList = phase1.computeIfAbsent(keyStack, k -> new ArrayList<>());
            resolveItemEntries(phaseList, entry);
            continue phase;
          }
        }
        ItemStack keyStack = entry.stack.copy();
        keyStack.setCount(1);
        List<ItemEntry> entries = new ArrayList<>();
        resolveItemEntries(entries, entry);
        phase1.put(keyStack, entries);
      }
    }

    int dimension = player.dimension;

    List<CollatedEntry> phase2 = new ArrayList<>();
    for (Entry<ItemStack, List<ItemEntry>> entry : phase1.entrySet()) {
      CollatedEntry inRange = new CollatedEntry(entry.getKey());
      inRange.inRange = true;
      CollatedEntry outOfRange = new CollatedEntry(entry.getKey());
      outOfRange.outOfRange = true;
      CollatedEntry outOfDimension = new CollatedEntry(entry.getKey());
      outOfDimension.outOfDimension = true;
      List<ItemEntry> entries = entry.getValue();
      for (ItemEntry itemEntry : entries) {
        EntryDescriptor descriptor = new EntryDescriptor(itemEntry.stack.getCount(), itemEntry.description, itemEntry.pos, itemEntry.dimension);
        if (itemEntry.dimension != dimension) {
          outOfDimension.descriptions.add(descriptor);
          outOfDimension.consume(itemEntry.stack);
          // TODO:
        } else if (!config.inRange(player, itemEntry.pos)) {
          outOfRange.descriptions.add(descriptor);
          outOfRange.consume(itemEntry.stack);
        } else {
          inRange.descriptions.add(descriptor);
          inRange.consume(itemEntry.stack);
        }
      }
      if (!inRange.descriptions.isEmpty()) {
        if (inRange.finalStack.getCount() > 1) {
          inRange.finalStack.shrink(1);
        }
        phase2.add(inRange);
      }
      if (!outOfDimension.descriptions.isEmpty()) {
        if (outOfDimension.finalStack.getCount() > 1) {
          outOfDimension.finalStack.shrink(1);
        }
        phase2.add(outOfDimension);
      }
      if (!outOfRange.descriptions.isEmpty()) {
        if (outOfRange.finalStack.getCount() > 1) {
          outOfRange.finalStack.shrink(1);
        }
        phase2.add(outOfRange);
      }
    }

    return phase2;
  }

  public static Map<Integer, List<ItemEntry>> buildItemEntryList(Network network, EntityPlayer player) {
    List<NetworkEntry> tiles = network.getTrackedInventories();

    Int2ObjectOpenHashMap<List<ItemEntry>> preManifest = new Int2ObjectOpenHashMap<>();
    preManifest.defaultReturnValue(null);

    Set<BlockPosDimension> done = new HashSet<>();

    for (NetworkEntry ref : tiles) {
      if (ref == null) {
        continue;
      }

      TrackingNetworkedBaseTile<?> ite = ref.getTile();

      if (ite == null) {
        continue;
      }

      if (done.contains(BlockPosDimension.fromTile(ite))) {
        continue;
      }

      if (ite.isSingleItemInventory()) {
        ItemStack stack = ite.getSingleItemReference();

        List<ItemEntry> entry = getEntryList(preManifest, net.minecraft.item.crafting.RecipeItemHelper.pack(stack), player);
        entry.add(new ItemEntry(stack, ite.getPos(), ite.getDimension(), ite.getDescriptor()));
        done.add(BlockPosDimension.fromTile(ite));
      } else if (ite instanceof MonitoringCrystalTile) {
        MonitoringCrystalTile mce = (MonitoringCrystalTile) ite;
        if (mce.getTarget() == null) {
          continue;
        }

        BlockPosDimension target = new BlockPosDimension(mce.getTarget(), mce.getDimension());

        if (done.contains(target)) {
          if (player != null) {
            player.sendMessage(new TextComponentTranslation("arcanearchives.error.monitoring_crystal", target.getX(), target.getY(), target.getZ(), target.dimension));
          } else {
            ArcaneArchives.logger.error("Multiple Monitoring Crystals were found for network " + network.getNetworkId().toString() + " targetgeting " + String.format("%d/%d/%d in dimension %d", target.getX(), target.getY(), target.getZ(), target.dimension));
          }
        }

        IItemHandler handler = mce.getInventory();
        if (handler != null) {
          for (SlotIterable.SlotIterator.SlotStack ss : new SlotIterable(handler)) {
            ItemStack is = ss.getStack();
            if (!is.isEmpty()) {
              List<ItemEntry> entries = getEntryList(preManifest, net.minecraft.item.crafting.RecipeItemHelper.pack(is), player);
              entries.add(new ItemEntry(is, target.pos, target.dimension, mce.getDescriptor()));
            }
          }
        }

        done.add(target);
      } else {
        for (SlotIterable.SlotIterator.SlotStack ss : new SlotIterable(ite.getInventory())) {
          ItemStack is = ss.getStack();
          if (!is.isEmpty()) {
            List<ItemEntry> entries = getEntryList(preManifest, RecipeItemHelper.pack(is), player);
            entries.add(new ItemEntry(is, ite.getPos(), ite.getDimension(), ite.getDescriptor()));
          }
        }
        done.add(BlockPosDimension.fromTile(ite));
      }
    }

    return preManifest;
  }

  public static List<ItemEntry> getEntryList(Int2ObjectOpenHashMap<List<ItemEntry>> map, int packed, EntityPlayer player) {
    List<ItemEntry> list = map.get(packed);
    if (list == null) {
      list = new ArrayList<>();
      map.put(packed, list);
    }
    return list;
  }

  @SuppressWarnings("WeakerAccess")
  public static class ItemEntry {
    public ItemStack stack;
    public BlockPos pos;
    public int dimension;
    public String description;

    public ItemEntry(ItemStack stack, BlockPos pos, int dimension, String description) {
      this.stack = stack;
      this.pos = pos;
      this.dimension = dimension;
      this.description = description;
    }

    public ItemEntry copy(ItemStack stack) {
      return new ItemEntry(stack, this.pos, this.dimension, this.description);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static class CollatedEntry implements ISerializeByteBuf<CollatedEntry> {
    public ItemStack finalStack;
    public List<EntryDescriptor> descriptions = new ArrayList<>();
    public boolean outOfRange = false;
    public boolean outOfDimension = false;
    public boolean inRange = false;

    public CollatedEntry() {
    }

    public CollatedEntry(ItemStack finalStack) {
      this.finalStack = finalStack.copy();
      this.finalStack.setCount(0);
    }

    public void consume(ItemStack stack) {
      finalStack.setCount(finalStack.getCount() + stack.getCount());
    }

    public ItemStack getStack() {
      return finalStack;
    }

    // TODO: This is never used
    public static CollatedEntry deserialize(ByteBuf buf) {
      CollatedEntry entry = new CollatedEntry();
      return entry.fromBytes(buf);
    }

    @Override
    public CollatedEntry fromBytes(ByteBuf buf) {
      finalStack = ByteBufUtils.readItemStack(buf);
      finalStack.setCount(buf.readInt());
      outOfDimension = buf.readBoolean();
      outOfRange = buf.readBoolean();
      inRange = buf.readBoolean();
      descriptions = new ArrayList<>();
      int entries = buf.readInt();
      for (int i = 0; i < entries; i++) {
        descriptions.add(EntryDescriptor.deserialize(buf));
      }
      return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeItemStack(buf, finalStack);
      buf.writeInt(finalStack.getCount());
      buf.writeBoolean(outOfDimension);
      buf.writeBoolean(outOfRange);
      buf.writeBoolean(inRange);
      buf.writeInt(descriptions.size());
      for (EntryDescriptor entry : descriptions) {
        entry.toBytes(buf);
      }
    }

    // TODO: This is never used?
    public List<Vec3d> getVecPositions() {
      return descriptions.stream().map(EntryDescriptor::vec3d).collect(Collectors.toList());
    }

    public int getDimension() {
      return descriptions.isEmpty() ? 0 : descriptions.get(0).dimension;
    }
  }

  @SuppressWarnings("WeakerAccess")
  public static class EntryDescriptor implements ISerializeByteBuf<EntryDescriptor> {
    public int count;
    public String string;
    public BlockPos pos;
    public int dimension;

    public EntryDescriptor() {
    }

    public EntryDescriptor(int count, String string, BlockPos pos, int dimension) {
      this.count = count;
      this.string = string;
      this.pos = pos;
      this.dimension = dimension;
    }

    public static EntryDescriptor deserialize(ByteBuf buf) {
      EntryDescriptor entry = new EntryDescriptor();
      return entry.fromBytes(buf);
    }

    @Override
    public EntryDescriptor fromBytes(ByteBuf buf) {
      count = buf.readInt();
      string = ByteUtils.readOptionalUTF8(buf);
      pos = BlockPos.fromLong(buf.readLong());
      dimension = buf.readInt();
      return this;
    }

    @Override
    public void toBytes(ByteBuf buf) {
      buf.writeInt(count);
      ByteUtils.writeOptionalUTF8(buf, string);
      buf.writeLong(pos.toLong());
      buf.writeInt(dimension);
    }

    public Vec3d vec3d() {
      return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    // TODO: Never used
    public int getItemCount() {
      return count;
    }
  }
}
