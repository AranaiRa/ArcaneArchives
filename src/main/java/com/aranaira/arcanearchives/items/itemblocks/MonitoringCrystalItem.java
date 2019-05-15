package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.blocks.templates.BlockTemplate;
import com.aranaira.arcanearchives.items.templates.ItemBlockTemplate;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MonitoringCrystalItem extends ItemBlockTemplate {
	public MonitoringCrystalItem (@Nonnull BlockTemplate block) {
		super(block);
	}

	@Override
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + "Placed on inventories to relay their contents to a manifest.");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public EnumRarity getRarity (ItemStack stack) {
		return EnumRarity.RARE;
	}
}
