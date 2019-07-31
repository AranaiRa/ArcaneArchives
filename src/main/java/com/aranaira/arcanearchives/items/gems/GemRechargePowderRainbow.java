package com.aranaira.arcanearchives.items.gems;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GemRechargePowderRainbow extends ItemTemplate {
	public static final String NAME = "full_spectrum_chromatic_powder";

	public GemRechargePowderRainbow () {
		super(NAME);
		setCreativeTab(null);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.full_spectrum_chromatic_powder"));
		tooltip.add(TextFormatting.RED + I18n.format("arcanearchives.tooltip.creativeonly"));
	}
}
