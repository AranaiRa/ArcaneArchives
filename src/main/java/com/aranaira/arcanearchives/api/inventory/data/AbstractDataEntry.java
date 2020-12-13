package com.aranaira.arcanearchives.api.inventory.data;

public abstract class AbstractDataEntry {
  protected final String key;

  public AbstractDataEntry(String key) {
    this.key = key;
  }

  public String getKey() {
    return this.key;
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

  public static class StringDataEntry extends AbstractDataEntry {
    private String value;

    public StringDataEntry(String key, String value) {
      super(key);
      this.value = value;
    }

    @Override
    public String asString() {
      return value;
    }
  }

  public static class IntegerDataEntry extends AbstractDataEntry {
    private int value;

    public IntegerDataEntry(String key, int value) {
      super(key);
      this.value = value;
    }

    @Override
    public int asInt() {
      return value;
    }
  }

  public static class FloatDataEntry extends AbstractDataEntry {
    private float value;

    public FloatDataEntry(String key, float value) {
      super(key);
      this.value = value;
    }

    @Override
    public float asFloat() {
      return value;
    }
  }

  public static class BooleanDataEntry extends AbstractDataEntry {
    private boolean value;

    public BooleanDataEntry(String key, boolean value) {
      super(key);
      this.value = value;
    }

    @Override
    public boolean asBoolean() {
      return this.value;
    }
  }

  public static class DynamicDataEntry<T> extends AbstractDataEntry {
    private final Class<T> clazz;
    private T value;

    public DynamicDataEntry(Class<T> clazz, String key, T value) {
      super(key);
      this.clazz = clazz;
      this.value = value;
    }

    public T getValue() {
      return value;
    }
  }
}
