package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.items.templates.LetterTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class LetterOfResignationItem extends LetterTemplate {
	public static final String NAME = "item_letterofresignation";

	public LetterOfResignationItem () {
		super(NAME);
	}

	@Override
	public ItemStack letterTriggered (ItemStack stack, World world, EntityLivingBase entity) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("creator_name")) {
			// TODO: Something with a sort of error or something
			entity.sendMessage(new TextComponentString("Invalid letter! Oops?"));
			return stack;
		}
		EntityPlayer player = (EntityPlayer) entity;
		if (NetworkHelper.abandonNetwork(player, world)) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.left"), true);
		} else {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.left_failed"), true);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.letterofresignation"));
	}
}
