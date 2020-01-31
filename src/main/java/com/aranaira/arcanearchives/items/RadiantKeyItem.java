/*package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.types.enums.UpgradeType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class RadiantKeyItem extends ItemTemplate implements IUpgradeItem {
	public static final String NAME = "radiant_key";

	public RadiantKeyItem () {
		super(NAME);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.radiant_key"));
	}

	@Override
	public UpgradeType getUpgradeType (ItemStack stack) {
		return UpgradeType.LOCK;
	}

	@Override
	public int getUpgradeSize (ItemStack stack) {
		return -1;
	}

	@Override
	public int getSlotIsUpgradeFor (ItemStack stack) {
		return -1;
	}

	@Override
	public List<Class<?>> upgradeFor () {
		return null;
	}
}*/
