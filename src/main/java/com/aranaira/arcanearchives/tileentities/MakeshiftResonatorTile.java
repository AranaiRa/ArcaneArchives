package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MakeshiftResonatorTile extends BaseTile implements ITickable {
  public static Random rand = new Random();

  private int growth = 0;

  public MakeshiftResonatorTile() {
  }

  @Override
  public void update() {
    if (world == null) {
      return;
    }
    BlockState state = world.getBlockState(pos);
    if (!state.getValue(MakeshiftResonatorBlock.FILLED)) {
      return;
    }
    int ticksRequired = 100; // ServerSideConfig.ResonatorTickTime;

    if (growth < ticksRequired) {
      growth++;
      ArcaneArchives.logger.info("Growth: " + growth);
    } else {
      List<ItemStack> toSpawn = new ArrayList<>();
      toSpawn.add(new ItemStack(Items.STICK, 2 + rand.nextInt(3)));
      toSpawn.add(new ItemStack(ModItems.RadiantDust, 1 + rand.nextInt(3)));
      if (rand.nextInt(4) == 0) {
        toSpawn.add(new ItemStack(Items.GOLD_NUGGET, 1));
      }
      if (rand.nextBoolean()) {
        toSpawn.add(new ItemStack(Blocks.STONE_SLAB, 1, 3));
      }
      if (rand.nextBoolean()) {
        toSpawn.add(new ItemStack(Blocks.STONE_SLAB, 1, 3));
      }
      if (!world.isRemote) {
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
      }
      world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1.0D, 0.0D, 0.0D);
      for (ItemStack stack : toSpawn) {
        int x = pos.getX() + rand.nextInt(1) - 1;
        int z = pos.getZ() + rand.nextInt(1) - 1;
        BlockPos randomised = new BlockPos(x, pos.getY(), z);
        if (!world.isRemote) {
          Block.spawnAsEntity(world, randomised, stack);
        }
      }
      if (!world.isRemote) {
        world.setBlockToAir(pos);
      }
    }
  }

  @Nonnull
  @Override
  public CompoundNBT writeToNBT(CompoundNBT compound) {
    super.writeToNBT(compound);

    compound.setInteger(Tags.MakeshiftResonator.currentTick, growth);

    return compound;
  }

  @Override
  public void readFromNBT(CompoundNBT compound) {
    super.readFromNBT(compound);

    if (compound.hasKey(Tags.MakeshiftResonator.currentTick)) {
      growth = compound.getInteger(Tags.MakeshiftResonator.currentTick);
    }
  }

/*	public int getPercentageComplete () {
		return (int) Math.floor(growth / (double) ServerSideConfig.ResonatorTickTime * 100D);
	}*/

  @Override
  @Nonnull
  public SUpdateTileEntityPacket getUpdatePacket() {
    CompoundNBT compound = writeToNBT(new CompoundNBT());

    return new SUpdateTileEntityPacket(pos, 0, compound);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return writeToNBT(new CompoundNBT());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
    return (oldState.getBlock() != newSate.getBlock());
  }
}
