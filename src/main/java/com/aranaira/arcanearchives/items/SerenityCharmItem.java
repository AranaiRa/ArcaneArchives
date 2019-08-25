package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.blocks.Brazier;
import com.aranaira.arcanearchives.blocks.RadiantResonator;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class SerenityCharmItem extends ItemTemplate implements IUpgradeItem {
	public static final String NAME = "serenity_charm";

	public SerenityCharmItem () {
		super(NAME);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}

	@Override
	public UpgradeType getUpgradeType (ItemStack stack) {
		return UpgradeType.MUTE;
	}

	@Override
	public int getUpgradeSize (ItemStack stack) {
		return -1;
	}

	@Override
	public int getSlotIsUpgradeFor (ItemStack stack) {
		return -1;
	}

	public static List<Class<?>> UPGRADE_FOR = Arrays.asList(RadiantResonator.class, Brazier.class);

	@Override
	public List<Class<?>> upgradeFor () {
		return UPGRADE_FOR;
	}
}
