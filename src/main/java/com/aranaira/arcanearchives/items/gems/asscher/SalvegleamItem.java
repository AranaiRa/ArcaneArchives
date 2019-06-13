package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class SalvegleamItem extends ArcaneGemItem {
	public static final String NAME = "salvegleam";
	public static final int PULSE_TICKS = 20;

	public SalvegleamItem() {
		super(NAME, GemCut.ASSCHER, GemColor.PINK, 30, 150);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.salvegleam"));
	}

	@Override
	public boolean hasToggleMode () {
		return true;
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {

		}
	    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	public static boolean canDoHealingPulse(ItemStack stack) {
		boolean thresholdPassed = false;

		NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
		if(!nbt.hasKey("pulse")) {
			thresholdPassed = true;
			nbt.setInteger("pulse", PULSE_TICKS);
		} else {
			int timer = nbt.getInteger("pulse");
			timer--;
			if(timer == 0) {
				thresholdPassed = true;
				nbt.setInteger("pulse", PULSE_TICKS);
			}
			else {
				nbt.setInteger("pulse", timer);
			}
		}
		stack.setTagCompound(nbt);

		return thresholdPassed;
	}
}
