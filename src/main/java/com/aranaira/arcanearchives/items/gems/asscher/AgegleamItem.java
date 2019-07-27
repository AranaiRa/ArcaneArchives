package com.aranaira.arcanearchives.items.gems.asscher;


import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.items.gems.GemUtil;
import com.aranaira.arcanearchives.items.gems.GemUtil.AvailableGemsHandler;
import com.aranaira.arcanearchives.util.ItemUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class AgegleamItem extends ArcaneGemItem {
	public static final String NAME = "agegleam";
	public static final int CHARGE_TICKS = 3600;

	public AgegleamItem () {
		super(NAME, GemCut.ASSCHER, GemColor.GREEN, 30, 150);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.agegleam"));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.recharge.agegleam"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if (!world.isRemote) {
			AvailableGemsHandler handler = GemUtil.getHeldGem(player, hand);
			if (handler.getHeld() != null) {
				int chargeCost = 0;
				double boxRadius = 3.5;
				AxisAlignedBB aabb = new AxisAlignedBB(player.posX - boxRadius, player.posY - boxRadius, player.posZ - boxRadius, player.posX + boxRadius, player.posY + boxRadius, player.posZ + boxRadius);
				for (EntityAgeable entity : player.world.getEntitiesWithinAABB(EntityAgeable.class, aabb)) {
					if (entity.isChild()) {
						entity.setGrowingAge(entity.getGrowingAge() + 8000);
						chargeCost += 1;
					}
				}

				if (chargeCost > 0) {
					GemUtil.consumeCharge(handler.getHeld(), chargeCost);
				}
			}
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public static boolean processRechargeTime (GemUtil.GemStack stack) {
		boolean thresholdPassed = false;

		NBTTagCompound nbt = ItemUtils.getOrCreateTagCompound(stack.getStack());
		if (!nbt.hasKey("recharge")) {
			nbt.setInteger("recharge", CHARGE_TICKS);
		} else {
			int timer = nbt.getInteger("recharge");
			timer--;
			if (timer == 0) {
				GemUtil.restoreCharge(stack, 1);
				nbt.setInteger("recharge", CHARGE_TICKS);
			} else {
				nbt.setInteger("recharge", timer);
			}
		}

		return thresholdPassed;
	}
}
