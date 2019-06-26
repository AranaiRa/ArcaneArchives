package com.aranaira.arcanearchives.items;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.util.types.UpgradeType;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class ContainmentFieldItem extends ItemTemplate implements IUpgradeItem {
	public static final String NAME = "containment_field";

	public ContainmentFieldItem () {
		super(NAME);
	}

	@Override
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.containment_field"));
	}

	@Override
	public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public UpgradeType getUpgradeType (ItemStack stack) {
		return UpgradeType.SIZE;
	}

	@Override
	public int getUpgradeSize (ItemStack stack) {
		return 3;
	}

	@Override
	public int getSlotIsUpgradeFor (ItemStack stack) {
		return 2;
	}

	public static List<Class<?>> UPGRADE_FOR = Collections.singletonList(RadiantTankTileEntity.class);

	@Override
	public List<Class<?>> upgradeFor () {
		return UPGRADE_FOR;
	}
}
