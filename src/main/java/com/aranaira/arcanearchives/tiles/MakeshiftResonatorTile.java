package com.aranaira.arcanearchives.tiles;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.blocks.MakeshiftResonatorBlock;
import com.aranaira.arcanearchives.init.ModItems;
import com.aranaira.arcanearchives.reference.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
    IBlockState state = world.getBlockState(pos);
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
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);

    compound.setInteger(Tags.MakeshiftResonator.CURRENT_TICK, growth);

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    if (compound.hasKey(Tags.MakeshiftResonator.CURRENT_TICK)) {
      growth = compound.getInteger(Tags.MakeshiftResonator.CURRENT_TICK);
    }
  }

/*	public int getPercentageComplete () {
		return (int) Math.floor(growth / (double) ServerSideConfig.ResonatorTickTime * 100D);
	}*/

  @Override
  @Nonnull
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound compound = writeToNBT(new NBTTagCompound());

    return new SPacketUpdateTileEntity(pos, 0, compound);
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return (oldState.getBlock() != newSate.getBlock());
  }
}
