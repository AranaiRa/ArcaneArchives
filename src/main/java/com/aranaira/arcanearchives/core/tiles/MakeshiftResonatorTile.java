package com.aranaira.arcanearchives.core.tiles;

import com.aranaira.arcanearchives.api.tiles.IArcaneArchivesTile;
import com.aranaira.arcanearchives.core.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.core.init.ModBlocks;
import com.aranaira.arcanearchives.core.init.ModItems;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MakeshiftResonatorTile extends TileEntity implements IArcaneArchivesTile, ITickableTileEntity {
  private boolean filled = false;
  private int countdown = 100;

  public MakeshiftResonatorTile(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Nullable
  @Override
  public UUID getTileId() {
    return null;
  }

  public void setFilled () {
    this.filled = true;
  }

  @Override
  public void tick() {
    if (world != null && !world.isRemote && filled) {
      if (countdown > 0) {
        countdown--;
      } else {
        List<ItemStack> spawn = new ArrayList<>();
        spawn.add(new ItemStack(Items.STICK, 2 + world.rand.nextInt(3)));
        spawn.add(new ItemStack(ModItems.RADIANT_DUST.get(), 2 + world.rand.nextInt(3)));
        if (world.rand.nextInt(4) == 0) {
          spawn.add(new ItemStack(Items.GOLD_NUGGET, 1 + world.rand.nextInt(2)));
        }
        if (world.rand.nextBoolean()) {
          spawn.add(new ItemStack(Blocks.SMOOTH_STONE_SLAB, 1));
        }
        if (world.rand.nextBoolean()) {
          spawn.add(new ItemStack(Blocks.SMOOTH_STONE_SLAB, 1));
        }
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        world.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
        for (ItemStack stack : spawn) {
          int x = pos.getX() + world.rand.nextInt(1) - 1;
          int z = pos.getZ() + world.rand.nextInt(1) - 1;
          Block.spawnAsEntity(world, new BlockPos(x, pos.getY(), z), stack);
        }
        world.destroyBlock(getPos(), false);
      }
    }
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    if (world != null && !world.isRemote && state.getBlock() == ModBlocks.MAKESHIFT_RESONATOR.get()) {
      if (state.get(MakeshiftResonatorBlock.FILLED)) {
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
  public CompoundNBT write(CompoundNBT compound) {
    compound.putInt("countdown", countdown);
    compound.putBoolean("filled", filled);
    return super.write(compound);
  }
}
