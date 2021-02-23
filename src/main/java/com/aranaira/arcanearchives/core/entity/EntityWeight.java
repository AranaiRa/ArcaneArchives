/*package com.aranaira.arcanearchives.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityWeight extends Entity {
  public static int LIFETIME_DEFAULT = 20 * 20; // 20 seconds

  private static final DataParameter<Integer> WEIGHT = EntityDataManager.createKey(EntityWeight.class, DataSerializers.VARINT);
  private static final DataParameter<Integer> LIFETIME = EntityDataManager.createKey(EntityWeight.class, DataSerializers.VARINT);

  public EntityWeight(World worldIn) {
    super(worldIn);
    isImmuneToFire = true;
  }

  public EntityWeight(World world, double x, double y, double z) {
    this(world);
    setPosition(x, y, z);
  }

  @Override
  protected void entityInit() {
    dataManager.register(WEIGHT, 0);
    dataManager.register(LIFETIME, LIFETIME_DEFAULT);
  }

  @Override
  protected void readEntityFromNBT(CompoundNBT compound) {
    setWeight(compound.getInteger(Tags.WEIGHT));
    setLifetime(compound.getInteger(Tags.LIFETIME));
  }

  @Override
  protected void writeEntityToNBT(CompoundNBT compound) {
    compound.setInteger(Tags.WEIGHT, getWeight());
    compound.setInteger(Tags.LIFETIME, getLifetime());
  }

  public int getWeight() {
    return dataManager.get(WEIGHT);
  }

  public void setWeight(int weight) {
    dataManager.set(WEIGHT, weight);
    dataManager.setDirty(WEIGHT);
  }

  public int getLifetime() {
    return dataManager.get(LIFETIME);
  }

  public void setLifetime(int lifetime) {
    dataManager.set(LIFETIME, lifetime);
    dataManager.setDirty(LIFETIME);
  }

  @Override
  public void onEntityUpdate() {
    int curLife = getLifetime();
    curLife--;
    if (curLife <= 0) {
      this.setDead();
    }
    setLifetime(curLife);
  }

  @Override
  public boolean hasCustomName() {
    return true;
  }

  @Override
  public String getCustomNameTag() {
    return "W: " + getWeight();
  }

  @Override
  public boolean getAlwaysRenderNameTag() {
    return true;
  }

  public static class Tags {
    public static final String WEIGHT = "weight";
    public static final String LIFETIME = "lifetime";

    public Tags() {

    }
  }
}*/
