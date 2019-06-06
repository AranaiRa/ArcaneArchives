package com.aranaira.arcanearchives.items.gems.pendeloque;

import com.aranaira.arcanearchives.init.ItemRegistry;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import vazkii.botania.common.item.ItemTemperanceStone;

import javax.annotation.Nonnull;
import java.util.List;

public class RivertearItem extends ArcaneGemItem {
    public static final String NAME = "item_rivertear";

    public RivertearItem () {
        super(NAME, GemCut.PENDELOQUE, GemColor.BLUE, 25, 100);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.rivertear"));
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public EnumActionResult onItemUse (EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if(GemUtil.getCharge(player.getHeldItem(hand)) > 0) {
                world.setBlockState(pos.offset(facing), Blocks.WATER.getDefaultState(), 11);
                GemUtil.consumeCharge(player.getHeldItemMainhand(), 1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem)
    {
        World world = entityItem.world;

        if (!world.isRemote && entityItem.isInLava())
        {
            //int amountToRestore = GemUtil.getMaxCharge(entityItem.getItem()) - GemUtil.getCharge(entityItem.getItem());
            //GemUtil.restoreCharge(entityItem.getItem(), amountToRestore);
            world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY + 0.5, entityItem.posZ,
                    new ItemStack(ItemRegistry.RIVERTEAR)));
            entityItem.setDead();

            return true;
        }
        return super.onEntityItemUpdate(entityItem);
    }

    /*@Override
    public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            if(GemUtil.getCharge(player.getHeldItem(hand)) > 0) {
                Vec3d start = new Vec3d(player.posX, player.posY+1, player.posZ);
                Vec3d dir = player.getLookVec();
                Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

                RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

                if(ray != null) {
                    BlockPos pos = ray.getBlockPos();
                    EnumFacing facing = ray.sideHit;

                    Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

                    world.setBlockState(pos.offset(facing), Blocks.WATER.getDefaultState(), 11);
                    world.scheduleUpdate(pos.offset(facing), Blocks.WATER, 20);

                    PacketArcaneGem packet = new PacketArcaneGem(cut, color, start, end);
                    NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, start.x, start.y, start.z, 160);
                    NetworkHandler.CHANNEL.sendToAllAround(packet, tp);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }*/
}
