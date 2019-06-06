package com.aranaira.arcanearchives.items.templates;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public abstract class LetterTemplate extends ItemTemplate {
	public LetterTemplate (String name) {
		super(name);
	}

	@Override
	public int getMaxItemUseDuration (ItemStack stack) {
		return 64;
	}

	@Override
	public EnumAction getItemUseAction (ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public ItemStack onItemUseFinish (ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (worldIn.isRemote) {
			return stack;
		}

		return letterTriggered(stack, worldIn, entityLiving);
	}

	public abstract ItemStack letterTriggered (ItemStack stack, World world, EntityLivingBase entity);
}
