package com.aranaira.arcanearchives.items.gems.pendeloque;

import com.aranaira.arcanearchives.client.particles.ParticleGenerator;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketDoParticles;
import com.aranaira.arcanearchives.network.PacketRadiantAmphora;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class MountaintearItem extends ArcaneGemItem {
    public static final String NAME = "item_mountaintear";

    public MountaintearItem () {
        super(NAME, GemCut.PENDELOQUE, GemColor.ORANGE, 25, 100);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " +getTooltipData(stack));
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.mountaintear"));
        tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.creativeonly"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumRarity getRarity (ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            Vec3d start = new Vec3d(player.posX, player.posY+1, player.posZ);
            Vec3d dir = player.getLookVec();
            Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

            RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

            if(ray != null) {
                BlockPos pos = ray.getBlockPos();
                EnumFacing facing = ray.sideHit;

                Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

                PacketDoParticles packet = new PacketDoParticles(cut, color, start, end);
                NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, start.x, start.y, start.z, 160);
                NetworkHandler.CHANNEL.sendToAllAround(packet, tp);
                //ParticleGenerator.makeDefaultLine(world, start, end, 40, 2.0);
                //ParticleGenerator.makeDefaultBurst(world, end, 36, 1,0.6, 0.01, 0.03);
            }
        } else {
            /*Vec3d start = new Vec3d(player.posX, player.posY+1, player.posZ);
            Vec3d dir = player.getLookVec();
            Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

            RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

            if(ray != null) {
                BlockPos pos = ray.getBlockPos();
                EnumFacing facing = ray.sideHit;

                Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

                PacketDoParticles packet = new PacketDoParticles(start, end);
                NetworkHandler.CHANNEL.sendToServer(packet);
                //ParticleGenerator.makeDefaultLine(world, start, end, 40, 2.0);
                //ParticleGenerator.makeDefaultBurst(world, end, 36, 1,0.6, 0.01, 0.03);
            }*/
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
