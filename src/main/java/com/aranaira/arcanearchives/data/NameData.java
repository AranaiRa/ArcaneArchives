package com.aranaira.arcanearchives.data;

import com.aranaira.arcanearchives.ArcaneArchives;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class NameData extends WorldSavedData {
  public static final String DATA = "ArcaneArchives-NameData";
  public static final int TOTAL_SEGMENTS = 36;
  public static final Random random = new Random();

  private Map<UUID, NetworkName> map = new HashMap<>();
  private Set<int[]> usedNames = new HashSet<>();

  public NameData(String name) {
    super(name);
  }

  public NetworkName getOrGenerateName (UUID id) {

    NetworkName name = map.get(id);
    if (name != null) {
      return name;
    }

    for (int tries = 100; tries > 0; tries--) {
      int[] pair = new int[]{random.nextInt(TOTAL_SEGMENTS), random.nextInt(TOTAL_SEGMENTS)};
      if (usedNames.contains(pair)) {
        continue;
      }

      map.put(id, new NetworkName(pair));
      markDirty();
      return map.get(id);
    }

    ArcaneArchives.logger.error("Unable to generate a network name for network ID " + id + " after 100 itertions");

    return null;

    // TODO: Generate string
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    map.clear();

    for (String key : nbt.getKeySet()) {
      UUID thisUuid = UUID.fromString(key);
      int[] pair = nbt.getIntArray(key);
      usedNames.add(pair);
      map.put(thisUuid, new NetworkName(pair[0], pair[1]));
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    for (Map.Entry<UUID, NetworkName> entry : map.entrySet()) {
      compound.setIntArray(entry.getKey().toString(), entry.getValue().asArray());
    }

    return compound;
  }

  public static class NetworkName {
    @SideOnly(Side.CLIENT)
    private static List<String> firstFields = null;
    @SideOnly(Side.CLIENT)
    private static List<String> secondFields = null;
    @SideOnly(Side.CLIENT)
    private static String separator = null;

    private int field1;
    private int field2;

    @SideOnly(Side.CLIENT)
    private String calculated = null;

    public NetworkName(int[] pair) {
      this(pair[0], pair[1]);
    }

    public NetworkName(int field1, int field2) {
      this.field1 = field1;
      this.field2 = field2;
    }

    public int getField1() {
      return field1;
    }

    public int getField2() {
      return field2;
    }

    @SideOnly(Side.CLIENT)
    public String getName () {
      if (firstFields == null || secondFields == null || separator == null) {
        separator = I18n.format("arcanearchives.network.name.separator");
        firstFields = Arrays.asList(I18n.format("arcanearchives.network.name.first").split(","));
        secondFields = Arrays.asList(I18n.format("arcanearchives.network.name.second").split(","));
      }

      if (calculated == null) {
        calculated = firstFields.get(field1) + "-" + secondFields.get(field2);
      }

      return calculated;
    }

    public int[] asArray() {
      return new int[]{field1, field2};
    }
  }
}
