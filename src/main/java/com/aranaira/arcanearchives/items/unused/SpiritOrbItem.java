package com.aranaira.arcanearchives.items.unused;

import com.aranaira.arcanearchives.items.templates.ItemMultistateTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class SpiritOrbItem extends ItemMultistateTemplate
{
	public static ItemStack EMPTY, CROWN_MOTE, CROWN_JUVE, CROWN_ELD, MATTER_MOTE, MATTER_JUVE, MATTER_ELD, POWER_MOTE, POWER_JUVE, POWER_ELD, SPACE_MOTE, SPACE_JUVE, SPACE_ELD, TIME_MOTE, TIME_JUVE, TIME_ELD;

	public SpiritOrbItem() {
		super("item_spiritorb");

		EMPTY = addItem(0, "empty");

		CROWN_MOTE = addItem(1, "crown_mote");
		CROWN_JUVE = addItem(2, "crown_juve");
		CROWN_ELD = addItem(3, "crown_eld");

		MATTER_MOTE = addItem(4, "matter_mote");
		MATTER_JUVE = addItem(5, "matter_juve");
		MATTER_ELD = addItem(6, "matter_eld");

		POWER_MOTE = addItem(7, "power_mote");
		POWER_JUVE = addItem(8, "power_juve");
		POWER_ELD = addItem(9, "power_eld");

		SPACE_MOTE = addItem(10, "space_mote");
		SPACE_JUVE = addItem(11, "space_juve");
		SPACE_ELD = addItem(12, "space_eld");

		TIME_MOTE = addItem(13, "time_mote");
		TIME_JUVE = addItem(14, "time_juve");
		TIME_ELD = addItem(15, "time_eld");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.RED + "" + TextFormatting.BOLD + I18n.format("arcanearchives.tooltip.notimplemented1"));
		tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.format("arcanearchives.tooltip.notimplemented2"));
	}
}
