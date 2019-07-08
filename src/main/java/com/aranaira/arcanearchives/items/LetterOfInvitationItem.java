package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.data.HiveSaveData;
import com.aranaira.arcanearchives.data.HiveSaveData.Hive;
import com.aranaira.arcanearchives.data.NetworkHelper;
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

public class LetterOfInvitationItem extends LetterTemplate {
	public static final String NAME = "letter_invitation";

	public LetterOfInvitationItem () {
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
		UUID network = tag.getUniqueId("creator");
		UUID playerId = entity.getUniqueID();
		EntityPlayer player = (EntityPlayer) entity;
		if (network.equals(playerId)) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.yours"), true);
			return stack;
		}

		HiveSaveData saveData = NetworkHelper.getHiveData(world);
		Hive hive = saveData.getHiveByOwner(network);
		if (saveData.addMember(hive, playerId)) {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.joined", tag.getString("creator_name")), true);
			saveData.alertMembers(world, hive, player.getUniqueID(), true);
		} else {
			player.sendStatusMessage(new TextComponentTranslation("arcanearchives.network.hive.failed"), true);
			return stack;
		}

		saveData.markDirty();
		world.getMapStorage().saveAllData();

		stack.shrink(1);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("creator_name")) {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.letter_invitation"));
		} else {
			tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.letter_invitation.named", tag.getString("creator_name")));
		}
	}
}
