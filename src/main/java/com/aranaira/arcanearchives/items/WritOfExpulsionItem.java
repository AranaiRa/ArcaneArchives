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

public class WritOfExpulsionItem extends LetterTemplate {
	public static final String NAME = "item_writofexpulsion";

	public WritOfExpulsionItem() {
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
		if (!tag.hasKey("expel_name")) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.expel_unnamed"), true);
			return stack;
		}
		UUID network = tag.getUniqueId("creator");
		UUID playerId = entity.getUniqueID();
		UUID expelId = tag.getUniqueId("expel");

		if (NetworkHelper.ejectPlayer(network, playerId, expelId, world)) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.expelled", tag.getString("expel_name")), true);
		} else {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.expel_failed"), true);
			return stack;
		}

		stack.shrink(1);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null && tag.hasKey("expel_name")) {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.writofexpulsion.named", tag.getString("expel_name")));
		} else {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.writofexpulsion"));
		}
	}
}
