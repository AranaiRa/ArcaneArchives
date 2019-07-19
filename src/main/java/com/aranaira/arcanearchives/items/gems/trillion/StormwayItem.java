package com.aranaira.arcanearchives.items.gems.trillion;

import com.aranaira.arcanearchives.items.gems.*;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.items.gems.GemUtil.GemStack;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class StormwayItem extends ArcaneGemItem {
	public static final String NAME = "stormway";

	public StormwayItem () {
		super(NAME, GemCut.TRILLION, GemColor.YELLOW, 30, 150);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.stormway"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.stormway"));
	}

	@Override
	public boolean hasToggleMode () {
		return true;
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate (EntityItem entityItem) {
		World world = entityItem.world;

		if (!world.isRemote && world.isRaining()) {
			if (world.canBlockSeeSky(new BlockPos(entityItem))) {
				if (GemUtil.getCharge(entityItem) < GemUtil.getMaxCharge(entityItem)) {
					GemUtil.restoreCharge(entityItem, -1);
					world.playSound(entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, 0.5f, false);
					return true;
				}
			}
		}
		return super.onEntityItemUpdate(entityItem);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			if (handler.getHeld() != null && GemUtil.getCharge(handler.getHeld()) > 0) {
				Vec3d start = new Vec3d(player.posX, player.posY + player.height, player.posZ);
				Vec3d dir = player.getLookVec();
				Vec3d rayTarget = new Vec3d(start.x + dir.x * 30, start.y + dir.y * 30, start.z + dir.z * 30);

				RayTraceResult ray = world.rayTraceBlocks(start, rayTarget, false, true, false);

				if (ray != null) {
					BlockPos pos = ray.getBlockPos();
					EnumFacing facing = ray.sideHit;
					if (world.canBlockSeeSky(pos.offset(facing))) {
						Vec3d end = new Vec3d(pos.offset(facing).getX(), pos.offset(facing).getY(), pos.offset(facing).getZ());

						world.spawnEntity(new EntityLightningBolt(world, end.x, end.y, end.z, false));

						GemUtil.consumeCharge(handler.getHeld(), 1);

						//PacketArcaneGem packet = new PacketArcaneGem(cut, color, start, end);
						//NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(player.dimension, start.x, start.y, start.z, 160);
						//NetworkHandler.CHANNEL.sendToAllTracking(packet, tp);
					}
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public static void setStrikeCooldownTimer (GemStack stack) {
		NBTTagCompound nbt = ItemUtils.getOrCreateTagCompound(stack.getStack());
		nbt.setLong("cooldown", System.currentTimeMillis());
	}

	public static boolean canBeStruck (GemStack stack) {
		NBTTagCompound nbt = ItemUtils.getOrCreateTagCompound(stack.getStack());
		if (nbt.hasKey("cooldown")) {
			return nbt.getLong("cooldown") + 1000 < System.currentTimeMillis();
		} else {
			return true;
		}
	}
}
