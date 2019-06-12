package com.aranaira.arcanearchives.items.gems.pendeloque;

import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketArcaneGem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
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
	public static final String NAME = "mountaintear";

	public MountaintearItem () {
		super(NAME, GemCut.PENDELOQUE, GemColor.ORANGE, 25, 100);
		setMaxStackSize(1);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.mountaintear"));
		tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.creativeonly"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onEntityItemUpdate (EntityItem entityItem) {
		World world = entityItem.world;

		if (!world.isRemote && entityItem.isInLava()) {
			if (GemUtil.getCharge(entityItem.getItem()) < GemUtil.getMaxCharge(entityItem.getItem())) {
				GemUtil.restoreCharge(entityItem.getItem(), -1);
				world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 0.5f, false);
				return true;
			}
		}
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			if (GemUtil.getCharge(player.getHeldItem(hand)) > 0) {
				Vec3d start = new Vec3d(player.posX, player.posY + player.height, player.posZ);
				Vec3d dir = player.getLookVec();
				Vec3d rayTarget = new Vec3d(start.x + dir.x * 40, start.y + dir.y * 40, start.z + dir.z * 40);

				RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

				if (ray != null) {
					BlockPos pos = ray.getBlockPos();
					EnumFacing facing = ray.sideHit;

					Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

					world.setBlockState(pos.offset(facing), Blocks.LAVA.getDefaultState(), 11);
					Blocks.LAVA.neighborChanged(Blocks.LAVA.getDefaultState(), world, pos.offset(facing), Blocks.LAVA, null);

					if (!player.capabilities.isCreativeMode) {
						GemUtil.consumeCharge(player.getHeldItemMainhand(), 1);
					}

					PacketArcaneGem packet = new PacketArcaneGem(cut, color, start, end);
					NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, start.x, start.y, start.z, 160);
					NetworkHandler.CHANNEL.sendToAllAround(packet, tp);
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
