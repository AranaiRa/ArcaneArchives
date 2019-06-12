package com.aranaira.arcanearchives.items.gems.asscher;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.gems.ArcaneGemItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CleansegleamItem extends ArcaneGemItem {
	public static final String NAME = "cleansegleam";

	public CleansegleamItem() {
		super(NAME, GemCut.ASSCHER, GemColor.BLUE, 30, 150);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("arcanearchives.tooltip.gemcharge") + ": " + getTooltipData(stack));
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.gem.cleansegleam"));
	}

	@Override
	public boolean hasToggleMode () {
		//return true if Betweenlands is loaded
		return false;
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World world, EntityPlayer player, EnumHand hand) {
		if(!world.isRemote) {
			ArrayList<Potion> toRemove = new ArrayList<>();
			for(PotionEffect effect : player.getActivePotionEffects()) {
				if(effect.getEffectName() == MobEffects.HUNGER.getName())
					toRemove.add(effect.getPotion());
				if(effect.getEffectName() == MobEffects.NAUSEA.getName())
					toRemove.add(effect.getPotion());
				if(effect.getEffectName() == MobEffects.POISON.getName())
					toRemove.add(effect.getPotion());
			}

			if(toRemove.size() > 0) GemUtil.consumeCharge(player.getHeldItemMainhand(), 1);

			for(Potion remove : toRemove) {
				player.removePotionEffect(remove);
			}
		}
	    return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
