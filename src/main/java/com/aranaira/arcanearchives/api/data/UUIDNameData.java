package com.aranaira.arcanearchives.api.data;

import com.aranaira.arcanearchives.api.reference.Identifiers;
import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import noobanidus.libs.noobutil.network.AbstractNetworkObject;

import java.util.*;
import java.util.stream.Collectors;

public class UUIDNameData extends WorldSavedData {
  public static final Random random = new Random();
  public static final String NETWORK_ID = "ArcaneArchives-Network-Names";
  public static final String TILE_ID = "ArcaneArchives-Tile-Names";

  private Set<byte[]> USED_NAMES = null;

  private final Map<UUID, Name> NAME_MAP = new HashMap<>();

  public UUIDNameData(String id) {
    super(id);
  }

  @Override
  public void load(CompoundNBT tag) {
    ListNBT names = tag.getList(Identifiers.Data.uniqueNames, Constants.NBT.TAG_COMPOUND);
    USED_NAMES = new HashSet<>();
    USED_NAMES.add(new byte[]{20, 20, 20});
    NAME_MAP.clear();
    for (int i = 0; i < names.size(); i++) {
      CompoundNBT name = names.getCompound(i);
      Name actualName = Name.fromNBT(name);
      USED_NAMES.add(actualName.getVal());
      NAME_MAP.put(actualName.getUuid(), actualName);
    }
    NAME_MAP.put(Name.EMPTY_NAME, Name.EMPTY);
  }

  @Override
  public CompoundNBT save(CompoundNBT pCompound) {
    ListNBT names = new ListNBT();
    for (Name actualName : NAME_MAP.values()) {
      if (actualName.isEmpty()) {
        continue;
      }
      names.add(actualName.serializeNBT());
    }
    CompoundNBT result = new CompoundNBT();
    result.put(Identifiers.Data.uniqueNames, names);
    return result;
  }

  public Name getOrGenerate(UUID id) {
    return NAME_MAP.computeIfAbsent(id, this::generate);
  }

  protected byte[] generate() {
    return new byte[]{(byte) random.nextInt(20), (byte) random.nextInt(20), (byte) random.nextInt(20)};
  }

  protected Name generate(UUID id) {
    int tries = 10000;
    byte[] potential = generate();
    if (USED_NAMES == null) {
      USED_NAMES = new HashSet<>(NAME_MAP.values().stream().map(Name::getVal).collect(Collectors.toSet()));
    }
    while (USED_NAMES.contains(potential)) {
      potential = generate();
      if (--tries <= 0) {
        throw new IllegalStateException("exceeded maximum number of tries in generating a new name");
      }
    }

    USED_NAMES.add(potential);
    return new Name(potential[0], potential[1], potential[2], id);
  }

  public static class Name extends AbstractNetworkObject<PacketBuffer> implements INBTSerializable<CompoundNBT> {
    public static final String NAME_PREFIX = "arcanearchives.network.id.part.";
    public static final UUID EMPTY_NAME = UUID.fromString("ccb71e56-6c95-4e02-9766-eaf75f39d37d");

    public static final Name EMPTY = new Name((byte)20, (byte)20, (byte)20, EMPTY_NAME);

    private byte[] val;
    private UUID uuid;

    protected Name() {
      this.val = new byte[]{20, 20, 20};
    }

    public Name(byte val1, byte val2, byte val3, UUID id) {
      this.val = new byte[]{val1, val2, val3};
      this.uuid = id;
    }

    protected Name(PacketBuffer incoming) {
      byte val1 = incoming.readByte();
      byte val2 = incoming.readByte();
      byte val3 = incoming.readByte();
      this.val = new byte[]{val1, val2, val3};
      uuid = incoming.readUUID();
    }

    @Override
    public void serialize(PacketBuffer buffer) {
      for (byte b : val) {
        buffer.writeByte(b);
      }
      buffer.writeUUID(uuid);
    }

    @Override
    public CompoundNBT serializeNBT() {
      CompoundNBT tag = new CompoundNBT();
      tag.putUUID("uuid", uuid);
      tag.putByteArray("name", val);
      return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
      byte[] array = nbt.getByteArray("name");
      byte val1 = array[0];
      byte val2 = array[1];
      byte val3 = array[2];
      Preconditions.checkArgument(val1 >= 0 && val1 < 20, "val1 must be in range 0-19");
      Preconditions.checkArgument(val2 >= 0 && val2 < 20, "val2 must be in range 0-19");
      Preconditions.checkArgument(val3 >= 0 && val3 < 20, "val3 must be in range 0-19");
      this.val = array;
      this.uuid = nbt.getUUID("uuid");
    }

    private ITextComponent cachedName = null;

    public ITextComponent component() {
      if (cachedName == null) {
        cachedName = new TranslationTextComponent(NAME_PREFIX + "name", new TranslationTextComponent(NAME_PREFIX + String.valueOf(val[0])), new TranslationTextComponent(NAME_PREFIX + String.valueOf(val[1])), new TranslationTextComponent(NAME_PREFIX + String.valueOf(val[2])));
      }
      return cachedName;
    }

    public byte[] getVal() {
      return val;
    }

    public static Name fromNBT(CompoundNBT nbt) {
      Name result = new Name();
      result.deserializeNBT(nbt);
      return result;
    }

    public static Name fromBuffer(PacketBuffer buffer) {
      return new Name(buffer);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Name name = (Name) o;

      return Arrays.equals(val, name.val);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(val);
    }

    public UUID getUuid() {
      return uuid;
    }

    public boolean isEmpty () {
      return this == EMPTY;
    }
  }
}
