package com.aranaira.arcanearchives.tileentities;

import com.aranaira.arcanearchives.init.ModBlocks;
import com.aranaira.arcanearchives.reference.Tags;
import com.aranaira.arcanearchives.tilenetwork.Network;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MandalicKeystoneTile extends NetworkedBaseTile implements ITickable {
  public static Random rand = new Random();

  private static Ingredient WOOD = null;
  private static Ingredient STONE = null;
  private static Ingredient GRAVEL = null;

  private int progress = 0;

  public MandalicKeystoneTile() {
    if (WOOD == null) {
      WOOD = new OreIngredient("logWood");
    }
    if (STONE == null) {
      STONE = new OreIngredient("stone");
    }
    if (GRAVEL == null) {
      GRAVEL = new OreIngredient("gravel");
    }
  }

  @Override
  public void update() {
    if (!world.isRemote && world.getTotalWorldTime() % 40 == 0) {
      List<EntityItem> items = getItems();
      if (!items.isEmpty()) {
        ItemStack testItem = items.get(0).getItem();

        //Gravel --> Flint
        if (GRAVEL.test(testItem)) {
          Random rng = new Random();
          int num = 0;
          for (int i = 0; i < testItem.getCount(); i++) {
            int r = rng.nextInt(4);
            if (r == 3) num += 2;
            else num += 1;
          }
          items.get(0).setItem(new ItemStack(Items.FLINT, num));
        }

        //Stone --> Stalwart Stone
        if (STONE.test(testItem)) {
          items.get(0).setItem(new ItemStack(ModBlocks.StalwartStone, testItem.getCount()));
        }

        //Wood --> Stalwart Wood
        if (WOOD.test(testItem)) {
          items.get(0).setItem(new ItemStack(ModBlocks.StalwartWood, testItem.getCount()));
        }
      }
    }
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);

    compound.setInteger(Tags.MakeshiftResonator.currentTick, progress);

    return compound;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    if (compound.hasKey(Tags.MakeshiftResonator.currentTick)) {
      progress = compound.getInteger(Tags.MakeshiftResonator.currentTick);
    }
  }

  private List<EntityItem> getItems() {
    List<EntityItem> result = new ArrayList<>();
    for (EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.up()))) {
      ItemStack inSlot = item.getItem();
      if (WOOD.test(inSlot) || STONE.test(inSlot) || GRAVEL.test(inSlot)) {
        result.add(item);
      }
    }

    return result;
  }

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
  public void onNetworkJoined(Network network) {
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    return (oldState.getBlock() != newSate.getBlock());
  }
}
