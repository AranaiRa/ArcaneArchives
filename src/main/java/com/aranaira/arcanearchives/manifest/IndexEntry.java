package com.aranaira.arcanearchives.manifest;

import com.aranaira.arcanearchives.types.ISerializePacketBuffer;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.function.Predicate;

public class IndexEntry implements Predicate<ItemStack>, ISerializePacketBuffer<IndexEntry> {
  private final ItemStack reference;
  private final CompoundNBT tag;
  private final RegistryKey<World> key;
  private final BlockPos position;
  private final int packed;
  private final IndexDescriptor descriptor;
  private final Int2LongOpenHashMap slots = new Int2LongOpenHashMap();
  private long quantity;

  public IndexEntry(ItemStack reference, BlockPos position, RegistryKey<World> key, IndexDescriptor descriptor) {
    this.reference = reference.copy();
    this.reference.setCount(1);
    this.packed = RecipeItemHelper.pack(reference);
    this.tag = reference.getTag();
    this.quantity = 0;
    this.key = key;
    this.position = position;
    this.descriptor = descriptor;
  }

  protected IndexEntry(ItemStack reference, long quantity, BlockPos position, RegistryKey<World> key, IndexDescriptor descriptor) {
    this.reference = reference;
    this.tag = reference.getTag();
    this.packed = net.minecraft.item.crafting.RecipeItemHelper.pack(reference);
    this.quantity = quantity;
    this.key = key;
    this.position = position;
    this.descriptor = descriptor;
  }

  public int getPacked() {
    return packed;
  }

  public long getQuantity() {
    return quantity;
  }

  public ItemStack getReference() {
    return reference;
  }

  @Nullable
  public CompoundNBT getTag() {
    return tag;
  }

  @Override
  public boolean test(ItemStack stack) {
    CompoundNBT other = stack.getTag();
    if (tag == null && other != null) {
      return false;
    }
    return (reference.areCapsCompatible(stack) && (tag == null || tag.equals(other)));
  }

  public void add(ItemStack stack, int slot) {
    this.quantity += stack.getCount();
    long amount = slots.get(slot) + stack.getCount();
    slots.put(slot, amount);
  }

  public static IndexEntry fromPacket(PacketBuffer buf) {
    ItemStack stack = ItemStack.EMPTY;
    stack = buf.readItemStack();
    long quantity = buf.readLong();
    BlockPos position = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    // TODO: ???
    String dimension = buf.readString(500);
    RegistryKey<World> key = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(dimension));
    IndexDescriptor descriptor = IndexDescriptor.fromPacket(buf);
    return new IndexEntry(stack, quantity, position, key, descriptor);
  }

  public RegistryKey<World> getDimension() {
    return key;
  }

  public BlockPos getPosition() {
    return position;
  }

  public IndexDescriptor getDescriptor() {
    return descriptor;
  }

  @Override
  public void toPacket(PacketBuffer buf) {
    buf.writeItemStack(reference);
    buf.writeLong(quantity);
    buf.writeInt(position.getX());
    buf.writeInt(position.getY());
    buf.writeInt(position.getZ());
    buf.writeString(key.getLocation().toString());
    descriptor.toPacket(buf);
  }
}
