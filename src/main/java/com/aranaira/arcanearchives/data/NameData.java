package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

// TODO: Update this for the new three things
public class NameData extends WorldSavedData {
  public static final String DATA = "ArcaneArchives-NameData";
  public static final Random random = new Random();
  public static int TOTAL_SEGMENTS = 36;

  private Map<UUID, NetworkName> map = new HashMap<>();
  private Set<int[]> usedNames = new HashSet<>();

  public NameData () {
    this(DATA);
  }

  public NameData(String name) {
    super(name);
  }

  public NetworkName getOrGenerateName(UUID id) {

    NetworkName name = map.get(id);
    if (name != null) {
      return name;
    }

    for (int tries = 100; tries > 0; tries--) {
      // TODO
      int[] triplet = new int[]{random.nextInt(TOTAL_SEGMENTS), random.nextInt(TOTAL_SEGMENTS), random.nextInt(TOTAL_SEGMENTS)};
      if (usedNames.contains(triplet)) {
        continue;
      }

      map.put(id, new NetworkName(triplet));
      markDirty();
      Objects.requireNonNull(DataHelper.getWorld().getMapStorage()).saveAllData();
      return map.get(id);
    }

    ArcaneArchives.logger.error("Unable to generate a network name for network ID " + id + " after 100 itertions");

    return null;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    map.clear();

    for (String key : nbt.getKeySet()) {
      UUID thisUuid = UUID.fromString(key);
      int[] triplet = nbt.getIntArray(key);
      usedNames.add(triplet);
      map.put(thisUuid, new NetworkName(triplet[0], triplet[1], triplet[2]));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    for (Map.Entry<UUID, NetworkName> entry : map.entrySet()) {
      compound.setIntArray(entry.getKey().toString(), entry.getValue().asArray());
    }

    return compound;
  }

  @SuppressWarnings("WeakerAccess")
  public static class NetworkName {
    @SideOnly(Side.CLIENT)
    private static String[] FIELDS = null;

    @SideOnly(Side.CLIENT)
    private String calculated;

    private int field1;
    private int field2;
    private int field3;

    public NetworkName(int[] triplet) {
      this(triplet[0], triplet[1], triplet[2]);
    }

    public NetworkName(int field1, int field2, int field3) {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    public int getField1() {
      return field1;
    }

    public int getField2() {
      return field2;
    }

    public int getField3() {
      return field3;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
      if (FIELDS == null) {
        FIELDS = I18n.format("arcanearchives.network.name").split(",");
      }

      if (calculated == null) {
        calculated = FIELDS[field1] + FIELDS[field2] + FIELDS[field3];
      }

      return calculated;
    }

    public int[] asArray() {
      return new int[]{field1, field2, field3};
    }
  }
}
