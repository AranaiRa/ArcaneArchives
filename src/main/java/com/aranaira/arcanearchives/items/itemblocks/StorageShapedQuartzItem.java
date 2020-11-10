/*package com.aranaira.arcanearchives.items.itemblocks;

import com.aranaira.arcanearchives.blocks.templates.TemplateBlock;
import com.aranaira.arcanearchives.items.IUpgradeItem;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantTroveTileEntity;
import com.aranaira.arcanearchives.types.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class StorageShapedQuartzItem extends ItemBlockTemplate implements IUpgradeItem {
	public StorageShapedQuartzItem (@Nonnull TemplateBlock block) {
		super(block);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation (ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public UpgradeType getUpgradeType (ItemStack stack) {
		return UpgradeType.SIZE;
	}

	@Override
	public int getUpgradeSize (ItemStack stack) {
		return 4;
	}

	@Override
	public int getSlotIsUpgradeFor (ItemStack stack) {
		return 2;
	}

	public static List<Class<?>> UPGRADE_FOR = Arrays.asList(RadiantTroveTileEntity.class, RadiantTankTileEntity.class);

	@Override
	public List<Class<?>> upgradeFor () {
		return UPGRADE_FOR;
	}
}*/
