package com.aranaira.arcanearchives.core.blocks.entities;

import com.aranaira.arcanearchives.api.blockentities.IArcaneArchivesBlockEntity;
import com.aranaira.arcanearchives.core.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.core.init.ModBlocks;
import com.aranaira.arcanearchives.core.init.ModItems;
import com.aranaira.arcanearchives.core.network.LightningRenderPacket;
import com.aranaira.arcanearchives.core.network.Networking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.*;

public class MakeshiftResonatorBlockEntity extends TileEntity implements IArcaneArchivesBlockEntity, ITickableTileEntity {
  private boolean filled = false;
  private int countdown = 100;

  public MakeshiftResonatorBlockEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public void setFilled() {
    this.filled = true;
  }

  @Override
  public void tick() {
    Vector3d start = Vector3d.atLowerCornerOf(getBlockPos());
    Vector3d start_sw = start.add(0, 0.9, 0);
    Vector3d start_ne = start.add(0.9, 0.95, 0.9);
    Vector3d start_nw = start.add(0, 0.9, 0.9);
    Vector3d start_se = start.add(0.9, 0.95, 0.9);
    List<Vector3d> starts = Arrays.asList(start_sw, start_ne, start_nw, start_se);

    if (level != null && !level.isClientSide && filled) {
      if (countdown > 0) {
        countdown--;
        if (countdown > 7) {
          double log = Math.log10(100D - countdown) - 1;
          if (level.random.nextFloat() < log) {
            int i = level.random.nextInt(starts.size());
            int x = i;
            while (x == i) {
              x = level.random.nextInt(starts.size());
            }
            Vector3d a = starts.get(i);
            Vector3d b = starts.get(x);
            if (countdown > 45) {
              if (level.random.nextInt(4) == 0) {
                b = b.subtract(0, 0.65, 0);
              }
            } else {
              if (level.random.nextInt(5) == 0) {
                b = start.add(0.5, 1.65, 0.5);
              }
            }
            Networking.sendToAllTracking(new LightningRenderPacket(LightningRenderPacket.LightningPreset.TOOL_AOE, Objects.hash(getBlockPos()), a, b, 10), this);
          }
        }
      } else {
        List<ItemStack> spawn = new ArrayList<>();
        spawn.add(new ItemStack(Items.STICK, 2 + level.random.nextInt(3)));
        spawn.add(new ItemStack(ModItems.RADIANT_DUST.get(), 2 + level.random.nextInt(1)));
        if (level.random.nextInt(4) == 0) {
          spawn.add(new ItemStack(Items.GOLD_NUGGET, 1 + level.random.nextInt(2)));
        }
        if (level.random.nextBoolean()) {
          spawn.add(new ItemStack(Blocks.STONE_SLAB, 1));
        }
        if (level.random.nextBoolean()) {
          spawn.add(new ItemStack(Blocks.STONE_SLAB, 1));
        }
        level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F);
        level.addParticle(ParticleTypes.EXPLOSION, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 0, 0, 0);
        for (ItemStack stack : spawn) {
          int x = worldPosition.getX() + level.random.nextInt(1) - 1;
          int z = worldPosition.getZ() + level.random.nextInt(1) - 1;
          Block.popResource(level, new BlockPos(x, worldPosition.getY(), z), stack);
        }
        level.destroyBlock(getBlockPos(), false);
      }
    }
  }

  @Override
  public void load(BlockState state, CompoundNBT nbt) {
    super.load(state, nbt);
    if (level != null && !level.isClientSide && state.getBlock() == ModBlocks.MAKESHIFT_RESONATOR.get()) {
      if (state.getValue(MakeshiftResonatorBlock.FILLED)) {
        this.filled = true;
      } else {
        this.filled = false;
      }
    } else {
      this.filled = nbt.getBoolean("filled");
    }
    this.countdown = nbt.getInt("countdown");
  }

  @Override
  public CompoundNBT save(CompoundNBT compound) {
    compound.putInt("countdown", countdown);
    compound.putBoolean("filled", filled);
    return super.save(compound);
  }

  @Override
  public TileEntity getTile() {
    return this;
  }
}
