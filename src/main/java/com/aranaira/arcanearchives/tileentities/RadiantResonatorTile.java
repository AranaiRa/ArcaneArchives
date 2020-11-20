/*package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.QuartzCluster;
import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.config.ServerSideConfig;
import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.init.SoundRegistry;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import com.aranaira.arcanearchives.types.MachineSound;
import com.aranaira.arcanearchives.util.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RadiantResonatorTile extends NetworkedBaseTile implements ITickable, ISoundTile {
  private int growth = 0;
  private int ticks = 0;

  public RadiantResonatorTile() {
  }

  @Override
  public void update() {
    ticks++;

    Network network = getNetwork();
    if (network == null) {
      return;
    }

    int ticksRequired = ServerSideConfig.ResonatorTickTime;

    markDirty();

    if (world.isAirBlock(pos.up())) {
      if (growth < ticksRequired) {
        growth++;
      } else {
        growth = 0;
        world.setBlockState(pos.up(), ModBlocks.QuartzCluster.getDefaultState());

        BlockPos up = pos.up();
        List<OcelotEntity> entities = world.getEntitiesWithinAABB(OcelotEntity.class, new AxisAlignedBB(up.getX() - 0.9, up.getY() - 0.9, up.getZ() - 0.9, up.getX() + 0.9, up.getY() + 0.9, up.getZ() + 0.9));
        for (OcelotEntity ocelot : entities) {
          ocelot.motionY += MathUtils.rand.nextFloat() * 5F;
          ocelot.motionX += (MathUtils.rand.nextFloat() - 0.5f) * 3F;
          ocelot.motionZ += (MathUtils.rand.nextFloat() - 0.5f) * 3F;
        }

        if (ConfigHandler.soundConfig.resonatorComplete && ConfigHandler.soundConfig.useSounds) {
          world.playSound(null, pos, SoundRegistry.RESONATOR_COMPLETE, SoundCategory.BLOCKS, 1f, 1f);
        }
      }
    }

    if (ticks % 50 == 0) {
      this.stateUpdate();
    }

    if (world.isRemote) {
      updateSound(pos);
    }
  }

  @Nonnull
  @Override
  public CompoundNBT writeToNBT(CompoundNBT compound) {
    super.writeToNBT(compound);

    compound.setInteger(Tags.Resonator.growth, growth);

    return compound;
  }

  @Override
  public void readFromNBT(CompoundNBT compound) {
    super.readFromNBT(compound);

    if (compound.hasKey(Tags.Resonator.growth)) {
      growth = compound.getInteger(Tags.Resonator.growth);
    }
  }

  public int getPercentageComplete() {
    return (int) Math.floor(growth / (double) ServerSideConfig.ResonatorTickTime * 100D);
  }

*//*  @Override
  public void breakBlock(@Nullable IBlockState state, boolean harvest) {
    super.breakBlock(state, harvest);

    this.breaking = true;
    if (world.isRemote) {
      updateSound();
    }

    ServerNetwork network = DataHelper.getServerNetwork(networkId);
    if (network != null) {
      network.removeTile(this);
    }
  }*//*

  public TickResult canTick() {
    if (world.isAirBlock(pos.up())) {
      return TickResult.TICKING;
    } else {
      BlockState up = world.getBlockState(pos.up());
      if (up.getBlock() instanceof QuartzCluster) {
        return TickResult.HARVEST_WAITING;
      } else {
        return TickResult.OBSTRUCTION;
      }
    }
  }

  @Override
  public boolean shouldPlaySound() {
    boolean breaking = false;
    return ConfigHandler.soundConfig.resonatorTicking && ISoundTile.super.shouldPlaySound() && canTick() == TickResult.TICKING && world.isAirBlock(pos.up());
    // TODO: Breaking?
  }

  @Override
  public boolean isTileValid() {
    return !isInvalid();
  }

  @Override
  public float getVolume() {
    return ConfigHandler.soundConfig.resonatorVolume;
  }

  @OnlyIn(Dist.CLIENT)
  private MachineSound sound;

  @Nullable
  @Override
  public MachineSound setMachineSound(MachineSound sound) {
    return this.sound = sound;
  }

  @Nullable
  @Override
  public MachineSound getMachineSound() {
    return this.sound;
  }

  @Override
  public boolean hasSound() {
    return true;
  }

  @Override
  public ResourceLocation getSound() {
    return new ResourceLocation(ArcaneArchives.MODID, "resonator.loop");
  }

  public enum TickResult {
    OBSTRUCTION("obstruction", TextFormatting.RED), HARVEST_WAITING("harvestable", TextFormatting.GOLD), TICKING("resonating", TextFormatting.GREEN);

    private String key;
    private TextFormatting format;

    TickResult(String key, TextFormatting format) {
      this.key = key;
      this.format = format;
    }

    public String getKey() {
      return "arcanearchives.data.tooltip.resonator_status." + key;
    }

    public TextFormatting getFormat() {
      return format;
    }
  }
}*/
