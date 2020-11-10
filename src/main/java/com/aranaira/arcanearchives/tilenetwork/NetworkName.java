package com.aranaira.arcanearchives.tilenetwork;

import com.aranaira.arcanearchives.types.ISerializeByteBuf;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("WeakerAccess")
public class NetworkName implements ISerializeByteBuf<NetworkName> {
  @OnlyIn(Dist.CLIENT)
  private static String[] FIELDS = null;

  @OnlyIn(Dist.CLIENT)
  private String calculated;

  private int field1;
  private int field2;
  private int field3;

  private NetworkName() {
    this(-1, -1, -1);
  }

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

  @OnlyIn(Dist.CLIENT)
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

  public static NetworkName fromBytes(ByteBuf buf) {
    NetworkName name = new NetworkName();
    name.field1 = buf.readInt();
    name.field2 = buf.readInt();
    name.field3 = buf.readInt();
    name.calculated = null;
    return name;
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(field1);
    buf.writeInt(field2);
    buf.writeInt(field3);
  }
}
