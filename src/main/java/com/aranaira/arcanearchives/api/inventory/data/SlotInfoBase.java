package com.aranaira.arcanearchives.api.inventory.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;

public abstract class SlotInfoBase<V> {
  protected final String key;
  protected CompoundNBT tag;

  public SlotInfoBase(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  public abstract Class<?> getType();

  public abstract CompoundNBT getTag();

  protected CompoundNBT getBaseTag() {
    if (tag == null) {
      tag = new CompoundNBT();
      tag.putString("key", key);
    }
    return tag;
  }

  public String asString() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a string.");
  }

  public int asInt() {
    throw new InvalidTypeException(getKey() + " cannot be converted into an integer.");
  }

  public float asFloat() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a float.");
  }

  public boolean asBoolean() {
    throw new InvalidTypeException(getKey() + " cannot be converted into a boolean.");
  }

  public abstract V asObject();

  public static SlotInfoBase<?> fromNBT (CompoundNBT tag) {
    String key = tag.getString("key");
    INBT value = tag.get("value");
    byte id = value.getId();
    if (id == Constants.NBT.TAG_STRING) {
      return new StringDataEntry(key, tag.getString("value"));
    } else if (id == Constants.NBT.TAG_INT) {
      return new IntegerDataEntry(key, tag.getInt("value"));
    } else if (id == Constants.NBT.TAG_FLOAT) {
      return new FloatDataEntry(key, tag.getFloat("value"));
    } else if (id == Constants.NBT.TAG_BYTE) {
      return new BooleanDataEntry(key, tag.getBoolean("value"));
    } else {
      throw new RuntimeException("Invalid NBT tag stored in data entry: " + tag);
    }
  }

  public static class StringDataEntry extends SlotInfoBase<String> {
    private String value;

    public StringDataEntry(String key, String value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return String.class;
    }

    @Override
    public CompoundNBT getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putString("value", value);
      }
      return tag;
    }

    @Override
    public String asString() {
      return value;
    }

    @Override
    public String asObject() {
      return asString();
    }
  }

  public static class IntegerDataEntry extends SlotInfoBase<Integer> {
    private int value;

    public IntegerDataEntry(String key, int value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Integer.class;
    }

    @Override
    public CompoundNBT getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putInt("value", value);
      }
      return tag;
    }

    @Override
    public int asInt() {
      return value;
    }

    @Override
    public Integer asObject() {
      return asInt();
    }
  }

  public static class FloatDataEntry extends SlotInfoBase<Float> {
    private float value;

    public FloatDataEntry(String key, float value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Float.class;
    }

    @Override
    public CompoundNBT getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putFloat("value", value);
      }
      return tag;
    }

    @Override
    public float asFloat() {
      return value;
    }

    @Override
    public Float asObject() {
      return asFloat();
    }
  }

  public static class BooleanDataEntry extends SlotInfoBase<Boolean> {
    private boolean value;

    public BooleanDataEntry(String key, boolean value) {
      super(key);
      this.value = value;
    }

    @Override
    public Class<?> getType() {
      return Boolean.class;
    }

    @Override
    public CompoundNBT getTag() {
      if (tag == null) {
        tag = getBaseTag();
        tag.putBoolean("value", value);
      }
      return tag;
    }

    @Override
    public boolean asBoolean() {
      return this.value;
    }

    @Override
    public Boolean asObject() {
      return asBoolean();
    }
  }
}
